package org.ikigaidigital.application.service

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.application.port.outbound.TimeDepositRepository
import org.ikigaidigital.application.port.outbound.WithdrawalRepository
import org.ikigaidigital.domain.model.Withdrawal

// Fake outbound ports: the application layer is tested without a database or Spring.

class InMemoryTimeDepositRepository(private val deposits: List<TimeDeposit>) : TimeDepositRepository {

    var saved: List<TimeDeposit> = emptyList()
        private set

    override fun findAll(): List<TimeDeposit> = deposits

    override fun saveBalances(deposits: List<TimeDeposit>) {
        saved = deposits.map { it.copy() }
    }
}

class InMemoryWithdrawalRepository(private val withdrawals: List<Withdrawal>) : WithdrawalRepository {

    override fun findAll(): List<Withdrawal> = withdrawals
}
