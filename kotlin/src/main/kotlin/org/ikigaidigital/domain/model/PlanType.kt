package org.ikigaidigital.domain.model

enum class PlanType(val value: String) {
    STUDENT("student"),
    PREMIUM("premium"),
    BASIC("basic");

    companion object {
        fun fromValue(value: String): PlanType? = PlanType.entries.firstOrNull { it.value == value }
    }
}
