package org.ikigaidigital.domain.interest

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.PlanType

class PremiumInterest : Interest {
    override val planType = PlanType.PREMIUM

    override fun monthlyInterest(deposit: TimeDeposit): Double =
        if (deposit.days > 45) deposit.balance * 0.05 / 12 else 0.0
}
