package org.ikigaidigital

import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.interest.BasicInterest
import org.ikigaidigital.domain.interest.Interest
import org.ikigaidigital.domain.interest.PremiumInterest
import org.ikigaidigital.domain.interest.StudentInterest
import java.math.BigDecimal
import java.math.RoundingMode

// Kept in the root package on purpose, like TimeDeposit: existing callers import it from here.
// The no-arg constructor still works thanks to the default strategies, so `TimeDepositCalculator()`
// and the `updateBalance` signature stay unchanged.
class TimeDepositCalculator(
    strategies: List<Interest> = listOf(
        BasicInterest(),
        StudentInterest(),
        PremiumInterest()
    )
) {

    private val strategiesByPlan: Map<PlanType, Interest> = strategies.associateBy { it.planType }

    init {
        require(strategiesByPlan.size == strategies.size) { "Only one interest strategy per plan type is allowed" }
    }

    fun updateBalance(xs: List<TimeDeposit>) {
        xs.forEach { it.balance += calculateInterest(it) }
    }

    private fun calculateInterest(deposit: TimeDeposit): Double {
        if (deposit.days <= 30) return 0.0

        val strategy = PlanType.fromValue(deposit.planType)?.let { strategiesByPlan[it] } ?: return 0.0
        return BigDecimal(strategy.monthlyInterest(deposit)).setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}
