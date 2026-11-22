package org.ikigaidigital.adapter.outbound.persistence.repository

import org.ikigaidigital.adapter.outbound.persistence.entity.TimeDepositEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TimeDepositJpaRepository : JpaRepository<TimeDepositEntity, Int>
