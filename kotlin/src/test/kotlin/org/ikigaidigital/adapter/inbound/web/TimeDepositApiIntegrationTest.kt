package org.ikigaidigital.adapter.inbound.web

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TimeDepositApiIntegrationTest {

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine")
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `GET returns all time deposits with their withdrawals`() {
        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(6) }
            jsonPath("$[0].id") { value(1) }
            jsonPath("$[0].planType") { value("basic") }
            jsonPath("$[0].days") { value(45) }
            // Only presence is checked: the POST test may run first and change the balances.
            jsonPath("$[0].balance") { exists() }
            jsonPath("$[0].withdrawals.length()") { value(2) }
            jsonPath("$[0].withdrawals[0].amount") { value(1000.00) }
            jsonPath("$[0].withdrawals[0].date") { value("2026-01-15") }
            jsonPath("$[1].withdrawals.length()") { value(0) }
        }
    }

    @Test
    fun `POST applies monthly interest and persists the new balances`() {
        val before = balances()

        mockMvc.post("/time-deposits/balances").andExpect { status { isNoContent() } }

        val after = balances()
        // Expected deltas follow TimeDepositCalculator: balance * rate / 12, rounded HALF_UP.
        assertThat(after.getValue(1) - before.getValue(1)).isEqualByComparingTo("1028.81") // basic
        assertThat(after.getValue(2) - before.getValue(2)).isEqualByComparingTo("25.00")   // student < 1 year
        assertThat(after.getValue(3) - before.getValue(3)).isEqualByComparingTo("0")       // student > 1 year
        assertThat(after.getValue(4) - before.getValue(4)).isEqualByComparingTo("0")       // premium <= 45 days
        assertThat(after.getValue(5) - before.getValue(5)).isEqualByComparingTo("208.33")  // premium > 45 days
        assertThat(after.getValue(6) - before.getValue(6)).isEqualByComparingTo("0")       // first 30 days

        mockMvc.get("/time-deposits").andExpect {
            jsonPath("$[1].balance") { value(after.getValue(2).toDouble()) }
        }
    }

    private fun balances(): Map<Int, BigDecimal> =
        jdbcTemplate.query("SELECT id, balance FROM time_deposits") { rs, _ ->
            rs.getInt("id") to rs.getBigDecimal("balance")
        }.toMap()
}
