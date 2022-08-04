package com.lowcostcustoms.plyaground.spring.jooq.repositories

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.PetTags
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.Tags
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.PetTagsRecord
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.TagsRecord
import com.lowcostcustoms.plyaground.spring.jooq.utils.db.withContext
import com.lowcostcustoms.plyaground.spring.jooq.utils.toFlux
import io.r2dbc.spi.ConnectionFactory
import java.util.UUID
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Repository

@Repository
class TagsRepository(private val connectionFactory: ConnectionFactory) {
    companion object {
        private val TABLE = Tags.TAGS
        private val PET_TAGS_TABLE = PetTags.PET_TAGS

        const val UNIQUE_NAME_CONSTRAINT = "uq__tags__name"
    }

    suspend fun count(): Int =
        connectionFactory.withContext { context ->
            context
                .selectCount()
                .from(TABLE)
                .toFlux()
                .awaitSingle()
                .value1()
        }

    suspend fun findAll(offset: Int, limit: Int): List<TagsRecord> =
        connectionFactory.withContext {  context ->
            context
                .selectFrom(TABLE)
                .offset(offset)
                .limit(limit)
                .toFlux()
                .collectList()
                .awaitSingle()
        }

    suspend fun findById(id: UUID): TagsRecord? =
        connectionFactory.withContext { context ->
            context
                .selectFrom(TABLE)
                .where(TABLE.ID.eq(id))
                .toFlux()
                .awaitFirstOrNull()
        }

    suspend fun findByNames(names: List<String>): List<TagsRecord> =
        connectionFactory.withContext { context ->
            context
                .selectFrom(TABLE)
                .where(TABLE.NAME.`in`(names))
                .toFlux()
                .collectList()
                .awaitSingle()
        }

    suspend fun findByPetIds(petIds: List<UUID>): Map<UUID, List<TagsRecord>> =
        connectionFactory.withContext { context ->
            context
                .select()
                .from(TABLE.join(PET_TAGS_TABLE).on(PET_TAGS_TABLE.TAG_ID.eq(TABLE.ID)))
                .where(PET_TAGS_TABLE.PET_ID.`in`(petIds))
                .toFlux()
                .collectList()
                .awaitSingle()
                .groupBy(
                    keySelector = { it.get(PET_TAGS_TABLE.PET_ID) },
                    valueTransform = { TagsRecord(it.get(TABLE.ID), it.get(TABLE.NAME)) },
                )
        }

    suspend fun create(tag: TagsRecord): Int =
        connectionFactory.withContext { context ->
            context
                .insertInto(TABLE, TABLE.ID, TABLE.NAME)
                .valuesOfRecords(tag)
                .awaitSingle()
        }

    suspend fun createPetTags(tags: List<PetTagsRecord>): Int =
        connectionFactory.withContext { context ->
            context
                .insertInto(PET_TAGS_TABLE, PET_TAGS_TABLE.PET_ID, PET_TAGS_TABLE.TAG_ID)
                .valuesOfRecords(tags)
                .awaitSingle()
        }

    suspend fun update(tag: TagsRecord): Int =
        connectionFactory.withContext { context ->
            context
                .update(TABLE)
                .set(tag)
                .where(TABLE.ID.eq(tag.id))
                .toFlux()
                .awaitSingle()
        }

    suspend fun deletePetTagsByPetId(id: UUID): Int =
        connectionFactory.withContext { context ->
            context
                .deleteFrom(PET_TAGS_TABLE)
                .where(PET_TAGS_TABLE.PET_ID.eq(id))
                .toFlux()
                .awaitSingle()
        }
}