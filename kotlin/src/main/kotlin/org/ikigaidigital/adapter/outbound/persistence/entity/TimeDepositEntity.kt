package org.ikigaidigital.adapter.outbound.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

// The spec names the table `timeDeposits`; it is stored as `time_deposits` to follow
// PostgreSQL's snake_case convention (unquoted camelCase identifiers get lower-cased anyway).
@Entity
@Table(name = "time_deposits")
class TimeDepositEntity(
    @Id
    val id: Int,

    @Column(name = "plan_type", nullable = false)
    val planType: String,

    @Column(nullable = false)
    val days: Int,

    @Column(nullable = false, precision = 19, scale = 2)
    var balance: BigDecimal
)
