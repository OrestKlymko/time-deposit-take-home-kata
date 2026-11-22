package org.ikigaidigital.application.service

import org.ikigaidigital.TimeDepositCalculator
import org.ikigaidigital.application.port.inbound.UpdateAllBalancesUseCase
import org.ikigaidigital.application.port.outbound.TimeDepositRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateAllBalancesService(
    private val timeDepositRepository: TimeDepositRepository,
    private val calculator: TimeDepositCalculator
) : UpdateAllBalancesUseCase {

    // Assumptions (the README leaves them open):
    // - `days` is not incremented here: like the original calculator, this only applies interest; tracking days is a separate process.
    // - Withdrawals do not affect the interest: the original rules use only plan type, days and balance.
    @Transactional
    override fun updateAllBalances() {
        val deposits = timeDepositRepository.findAll()
        calculator.updateBalance(deposits)
        timeDepositRepository.saveBalances(deposits)
    }
}
