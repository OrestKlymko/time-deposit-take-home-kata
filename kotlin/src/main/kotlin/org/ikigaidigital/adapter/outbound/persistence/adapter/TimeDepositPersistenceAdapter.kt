package org.ikigaidigital.adapter.outbound.persistence.adapter

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.adapter.outbound.persistence.mapper.toBalanceColumn
import org.ikigaidigital.adapter.outbound.persistence.mapper.toDomain
import org.ikigaidigital.adapter.outbound.persistence.repository.TimeDepositJpaRepository
import org.ikigaidigital.application.port.outbound.TimeDepositRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class TimeDepositPersistenceAdapter(
    private val jpaRepository: TimeDepositJpaRepository
) : TimeDepositRepository {

    override fun findAll(): List<TimeDeposit> =
        jpaRepository.findAll(Sort.by("id")).map { it.toDomain() }

    override fun saveBalances(deposits: List<TimeDeposit>) {
        val balancesById = deposits.associate { it.id to it.balance }
        val entities = jpaRepository.findAllById(balancesById.keys)
        entities.forEach { it.balance = balancesById.getValue(it.id).toBalanceColumn() }
        jpaRepository.saveAll(entities)
    }
}
