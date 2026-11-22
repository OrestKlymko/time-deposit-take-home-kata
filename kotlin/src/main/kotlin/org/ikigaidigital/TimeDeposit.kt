package org.ikigaidigital

// Kept in the root package on purpose: this is the shared class from the original kata, and moving it
// to `domain` would change its fully qualified name, breaking existing callers.
data class TimeDeposit(
    val id: Int,
    val planType: String,
    var balance: Double,
    val days: Int
)