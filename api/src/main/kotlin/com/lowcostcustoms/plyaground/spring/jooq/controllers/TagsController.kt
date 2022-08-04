package com.lowcostcustoms.plyaground.spring.jooq.controllers

import com.lowcostcustoms.plyaground.spring.jooq.models.OffsetLimit
import com.lowcostcustoms.plyaground.spring.jooq.models.Page
import com.lowcostcustoms.plyaground.spring.jooq.models.TagRequest
import com.lowcostcustoms.plyaground.spring.jooq.models.TagResponse
import com.lowcostcustoms.plyaground.spring.jooq.services.TagsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import javax.validation.Valid
import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/tags")
@Tag(name = "Tags")
class TagsController(private val service: TagsService) {
    @GetMapping
    @Operation(description = "Returns a list of tags given a query criteria.")
    suspend fun listTags(@ParameterObject @Valid params: OffsetLimit): Page<TagResponse> =
        service.listTags(params)

    @GetMapping("/{id}")
    @Operation(description = "Retrieves a tag by its identifier.")
    suspend fun getTag(id: UUID): TagResponse =
        service.getTag(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creates a tag.")
    suspend fun createTag(@Valid @RequestBody request: TagRequest): TagResponse =
        service.createTag(request)

    @PutMapping("/{id}")
    @Operation(description = "Updates a tag.")
    suspend fun replaceTag(@PathVariable id: UUID, @RequestBody @Valid request: TagRequest): TagResponse =
        service.replaceTag(id, request)
}
