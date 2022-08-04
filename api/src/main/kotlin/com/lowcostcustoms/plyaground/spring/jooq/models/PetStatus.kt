package com.lowcostcustoms.plyaground.spring.jooq.models

import com.fasterxml.jackson.annotation.JsonValue

enum class PetStatus(@get:JsonValue val value: String) {
    AVAILABLE("Available"),
    PENDING("Pending"),
    SOLD("Sold");

    companion object {
        fun fromString(status: String): PetStatus = values().first { it.value == status }
    }
}
