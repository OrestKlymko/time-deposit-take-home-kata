package org.ikigaidigital.domain.interest

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.domain.model.PlanType

interface Interest {
    val planType: PlanType

    fun monthlyInterest(deposit: TimeDeposit): Double
}
