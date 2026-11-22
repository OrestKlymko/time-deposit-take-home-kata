package org.ikigaidigital.application.service

import org.ikigaidigital.application.port.inbound.GetAllTimeDepositsUseCase
import org.ikigaidigital.application.port.inbound.TimeDepositOverview
import org.ikigaidigital.application.port.outbound.TimeDepositRepository
import org.ikigaidigital.application.port.outbound.WithdrawalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAllTimeDepositsService(
    private val timeDepositRepository: TimeDepositRepository,
    private val withdrawalRepository: WithdrawalRepository
) : GetAllTimeDepositsUseCase {

    @Transactional(readOnly = true)
    override fun getAll(): List<TimeDepositOverview> {
        val withdrawalsByDeposit = withdrawalRepository.findAll().groupBy { it.timeDepositId }
        return timeDepositRepository.findAll().map {
            TimeDepositOverview(it, withdrawalsByDeposit[it.id].orEmpty())
        }
    }
}
