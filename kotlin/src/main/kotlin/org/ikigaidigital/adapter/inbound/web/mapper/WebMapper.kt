package org.ikigaidigital.adapter.inbound.web.mapper

import org.ikigaidigital.adapter.inbound.web.api.model.TimeDepositDto
import org.ikigaidigital.adapter.inbound.web.api.model.WithdrawalDto
import org.ikigaidigital.application.port.inbound.TimeDepositOverview
import org.ikigaidigital.domain.model.Withdrawal

internal fun TimeDepositOverview.toDto() = TimeDepositDto(
    id = deposit.id,
    planType = deposit.planType,
    balance = deposit.balance,
    days = deposit.days,
    withdrawals = withdrawals.map { it.toDto() }
)

internal fun Withdrawal.toDto() = WithdrawalDto(
    id = id,
    amount = amount,
    date = date
)
