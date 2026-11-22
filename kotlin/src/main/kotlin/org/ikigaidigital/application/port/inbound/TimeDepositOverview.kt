package org.ikigaidigital.application.port.inbound

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.Withdrawal

data class TimeDepositOverview(
    val deposit: TimeDeposit,
    val withdrawals: List<Withdrawal>
)
