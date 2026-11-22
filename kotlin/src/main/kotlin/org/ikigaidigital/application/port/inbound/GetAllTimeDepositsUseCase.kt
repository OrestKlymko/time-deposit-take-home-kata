package org.ikigaidigital.application.port.inbound

interface GetAllTimeDepositsUseCase {
    fun getAll(): List<TimeDepositOverview>
}
