package com.lowcostcustoms.plyaground.spring.jooq.controllers

import com.lowcostcustoms.plyaground.spring.jooq.models.OffsetLimit
import com.lowcostcustoms.plyaground.spring.jooq.models.Page
import com.lowcostcustoms.plyaground.spring.jooq.models.PetRequest
import com.lowcostcustoms.plyaground.spring.jooq.models.PetResponse
import com.lowcostcustoms.plyaground.spring.jooq.services.PetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import javax.validation.Valid
import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/pets")
@Tag(name = "Pets")
class PetsController(private val service: PetService) {
    @GetMapping
    @Operation(description = "Returns a list of pets given a query criteria.")
    suspend fun listPets(@ParameterObject @Valid params: OffsetLimit): Page<PetResponse> =
        service.listPets(params)

    @GetMapping("/{id}")
    @Operation(description = "Retrieves a pet.")
    suspend fun getPet(@PathVariable id: UUID): PetResponse =
        service.getPet(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creates a new pet.")
    suspend fun createPet(@RequestBody @Valid request: PetRequest): PetResponse =
        service.createPet(request)

    @PutMapping("/{id}")
    @Operation(description = "Updates a pet.")
    suspend fun replacePet(@PathVariable id: UUID, @RequestBody @Valid request: PetRequest): PetResponse =
        service.replacePet(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Deletes a pet.")
    suspend fun deletePet(@PathVariable id: UUID): Unit =
        service.deletePet(id)
}
