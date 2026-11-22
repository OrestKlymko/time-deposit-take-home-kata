package org.ikigaidigital.application.service

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.TimeDepositCalculator
import org.junit.jupiter.api.Test

class UpdateAllBalancesServiceTest {

    @Test
    fun `applies interest to every deposit and saves the new balances`() {
        val repository = InMemoryTimeDepositRepository(
            listOf(
                TimeDeposit(1, "basic", 10000.00, 31),
                TimeDeposit(2, "premium", 10000.00, 46),
                TimeDeposit(3, "student", 10000.00, 20),
            )
        )

        UpdateAllBalancesService(repository, TimeDepositCalculator()).updateAllBalances()

        assertThat(repository.saved.map { it.id to it.balance })
            .containsExactly(1 to 10008.33, 2 to 10041.67, 3 to 10000.00)
    }
}
