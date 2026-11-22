package org.ikigaidigital.adapter.outbound.persistence.repository

import org.ikigaidigital.adapter.outbound.persistence.entity.WithdrawalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WithdrawalJpaRepository : JpaRepository<WithdrawalEntity, Int>
