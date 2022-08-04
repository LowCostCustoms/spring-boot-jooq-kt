package com.lowcostcustoms.plyaground.spring.jooq.models

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.TagsRecord
import java.util.UUID

data class TagResponse(val id: UUID, val name: String) {
    companion object {
        fun fromTagsRecord(record: TagsRecord): TagResponse =
            TagResponse(id = record.id, name = record.name)
    }
}
