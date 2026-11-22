package org.ikigaidigital.application.service

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.Withdrawal
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class GetAllTimeDepositsServiceTest {

    @Test
    fun `attaches each withdrawal to its own deposit`() {
        val first = TimeDeposit(1, "basic", 10000.00, 31)
        val second = TimeDeposit(2, "student", 5000.00, 100)
        val third = TimeDeposit(3, "premium", 2000.00, 50)
        val withdrawalA = Withdrawal(1, 1, BigDecimal("100.00"), LocalDate.of(2026, 1, 15))
        val withdrawalB = Withdrawal(2, 3, BigDecimal("50.00"), LocalDate.of(2026, 2, 1))
        val withdrawalC = Withdrawal(3, 1, BigDecimal("25.00"), LocalDate.of(2026, 3, 1))
        val service = GetAllTimeDepositsService(
            InMemoryTimeDepositRepository(listOf(first, second, third)),
            InMemoryWithdrawalRepository(listOf(withdrawalA, withdrawalB, withdrawalC))
        )

        val overviews = service.getAll()

        assertThat(overviews.map { it.deposit }).containsExactly(first, second, third)
        assertThat(overviews[0].withdrawals).containsExactly(withdrawalA, withdrawalC)
        assertThat(overviews[1].withdrawals).isEmpty()
        assertThat(overviews[2].withdrawals).containsExactly(withdrawalB)
    }
}
