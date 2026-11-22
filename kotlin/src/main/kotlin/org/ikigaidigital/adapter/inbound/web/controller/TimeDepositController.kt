package org.ikigaidigital.adapter.inbound.web.controller

import org.ikigaidigital.adapter.inbound.web.api.TimeDepositsApi
import org.ikigaidigital.adapter.inbound.web.api.model.TimeDepositDto
import org.ikigaidigital.adapter.inbound.web.mapper.toDto
import org.ikigaidigital.application.port.inbound.GetAllTimeDepositsUseCase
import org.ikigaidigital.application.port.inbound.UpdateAllBalancesUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implements the interface generated from `openapi.yaml` (contract-first).
 * Paths, HTTP methods and DTOs come from the contract; this class only maps between the API and the use cases.
 */
@RestController
class TimeDepositController(
    private val getAllTimeDepositsUseCase: GetAllTimeDepositsUseCase,
    private val updateAllBalancesUseCase: UpdateAllBalancesUseCase
) : TimeDepositsApi {

    override fun getAllTimeDeposits(): ResponseEntity<List<TimeDepositDto>> =
        ResponseEntity.ok(getAllTimeDepositsUseCase.getAll().map { it.toDto() })

    override fun updateAllBalances(): ResponseEntity<Unit> {
        updateAllBalancesUseCase.updateAllBalances()
        return ResponseEntity.noContent().build()
    }
}
