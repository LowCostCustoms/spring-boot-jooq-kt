package com.lowcostcustoms.plyaground.spring.jooq.models

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.PetsRecord
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.TagsRecord
import java.util.UUID

data class PetResponse(val id: UUID, val name: String, val status: PetStatus, val tags: List<TagResponse>) {
    companion object {
        fun fromPetsRecord(record: PetsRecord, tags: List<TagResponse>): PetResponse =
            PetResponse(
                id = record.id,
                name = record.name,
                status = PetStatus.fromString(record.status),
                tags = tags,
            )

        fun fromPetsRecord(record: PetsRecord, tags: Map<UUID, List<TagsRecord>>): PetResponse =
            fromPetsRecord(
                record = record,
                tags = tags
                    .getOrDefault(record.id, emptyList())
                    .map { TagResponse.fromTagsRecord(it) },
            )
    }
}
