package org.ikigaidigital.domain.interest

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.PlanType

class BasicInterest : Interest {
    override val planType = PlanType.BASIC

    override fun monthlyInterest(deposit: TimeDeposit): Double = deposit.balance * 0.01 / 12
}
