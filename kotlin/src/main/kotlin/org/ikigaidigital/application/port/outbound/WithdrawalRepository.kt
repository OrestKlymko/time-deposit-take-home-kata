package org.ikigaidigital.application.port.outbound

import org.ikigaidigital.domain.model.Withdrawal

interface WithdrawalRepository {
    fun findAll(): List<Withdrawal>
}
