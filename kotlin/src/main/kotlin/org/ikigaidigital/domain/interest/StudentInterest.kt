package org.ikigaidigital.domain.interest

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.PlanType

class StudentInterest : Interest {
    override val planType = PlanType.STUDENT

    override fun monthlyInterest(deposit: TimeDeposit): Double =
        if (deposit.days < 366) deposit.balance * 0.03 / 12 else 0.0
}
