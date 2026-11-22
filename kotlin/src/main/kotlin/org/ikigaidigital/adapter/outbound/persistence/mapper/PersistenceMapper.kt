package org.ikigaidigital.adapter.outbound.persistence.mapper

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.adapter.outbound.persistence.entity.TimeDepositEntity
import org.ikigaidigital.adapter.outbound.persistence.entity.WithdrawalEntity
import org.ikigaidigital.domain.model.Withdrawal
import java.math.BigDecimal
import java.math.RoundingMode

// TimeDeposit uses Double for balance (shared class, must not change), so the
// conversion between Double and the DECIMAL column happens at this boundary.
internal fun TimeDepositEntity.toDomain() = TimeDeposit(
    id = id,
    planType = planType,
    balance = balance.toDouble(),
    days = days
)

internal fun WithdrawalEntity.toDomain() = Withdrawal(
    id = id,
    timeDepositId = timeDepositId,
    amount = amount,
    date = date
)

// valueOf uses the shortest decimal representation of the double, avoiding binary noise.
internal fun Double.toBalanceColumn(): BigDecimal = BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP)
