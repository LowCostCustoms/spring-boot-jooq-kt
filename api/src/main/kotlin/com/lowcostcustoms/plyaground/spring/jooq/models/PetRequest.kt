package com.lowcostcustoms.plyaground.spring.jooq.models

import javax.validation.constraints.NotBlank

data class PetRequest(
    @field:NotBlank
    val name: String,
    val status: PetStatus,
    val tags: List<String>,
)
