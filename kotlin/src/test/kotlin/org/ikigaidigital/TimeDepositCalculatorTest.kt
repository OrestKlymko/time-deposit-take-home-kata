package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.interest.BasicInterest
import org.ikigaidigital.domain.interest.Interest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Characterization tests pinning the original behavior of [TimeDepositCalculator.updateBalance]:
 * monthly interest = balance * rate / 12, rounded HALF_UP to 2 decimals.
 */
class TimeDepositCalculatorTest {

    private val calculator = TimeDepositCalculator()

    @Test
    fun `basic plan accrues 1 percent yearly interest monthly`() {
        val deposit = TimeDeposit(1, "basic", 1234567.00, 45)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1235595.81)
    }

    @ParameterizedTest(name = "{0} plan, {1} days: {2} -> {3}")
    @CsvSource(
        "basic,   30,  10000.00, 10000.00",
        "basic,   31,  10000.00, 10008.33",
        "student, 30,  10000.00, 10000.00",
        "student, 100, 10000.00, 10025.00",
        "student, 365, 10000.00, 10025.00",
        "student, 366, 10000.00, 10000.00",
        "premium, 30,  10000.00, 10000.00",
        "premium, 45,  10000.00, 10000.00",
        "premium, 46,  10000.00, 10041.67",
        "gold,    100, 10000.00, 10000.00",
    )
    fun `applies plan specific interest rules`(planType: String, days: Int, balance: Double, expected: Double) {
        val deposit = TimeDeposit(1, planType, balance, days)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(expected)
    }

    @Test
    fun `updates every deposit in the list`() {
        val deposits = listOf(
            TimeDeposit(1, "basic", 10000.00, 31),
            TimeDeposit(2, "premium", 10000.00, 46),
        )

        calculator.updateBalance(deposits)

        assertThat(deposits.map { it.balance }).containsExactly(10008.33, 10041.67)
    }

    @Test
    fun `uses the injected strategy for its plan type`() {
        val flatTen = object : Interest {
            override val planType = PlanType.BASIC
            override fun monthlyInterest(deposit: TimeDeposit) = 10.0
        }
        val deposit = TimeDeposit(1, "basic", 10000.00, 31)

        TimeDepositCalculator(listOf(flatTen)).updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(10010.00)
    }

    @Test
    fun `plan without a strategy earns no interest`() {
        val deposit = TimeDeposit(1, "premium", 10000.00, 100)

        TimeDepositCalculator(listOf(BasicInterest())).updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(10000.00)
    }

    @Test
    fun `rejects two strategies for the same plan type`() {
        assertThatThrownBy { TimeDepositCalculator(listOf(BasicInterest(), BasicInterest())) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
