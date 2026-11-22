package org.ikigaidigital.adapter.outbound.persistence.adapter

import org.ikigaidigital.adapter.outbound.persistence.mapper.toDomain
import org.ikigaidigital.adapter.outbound.persistence.repository.WithdrawalJpaRepository
import org.ikigaidigital.application.port.outbound.WithdrawalRepository
import org.ikigaidigital.domain.model.Withdrawal
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class WithdrawalPersistenceAdapter(
    private val jpaRepository: WithdrawalJpaRepository
) : WithdrawalRepository {

    override fun findAll(): List<Withdrawal> =
        jpaRepository.findAll(Sort.by("date", "id")).map { it.toDomain() }
}
