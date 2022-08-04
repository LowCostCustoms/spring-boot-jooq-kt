package com.lowcostcustoms.plyaground.spring.jooq.repositories

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.Pets
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.PetsRecord
import com.lowcostcustoms.plyaground.spring.jooq.utils.db.withContext
import com.lowcostcustoms.plyaground.spring.jooq.utils.toFlux
import io.r2dbc.spi.ConnectionFactory
import java.util.UUID
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Repository

@Repository
class PetsRepository(private val connectionFactory: ConnectionFactory) {
    companion object {
        private val TABLE = Pets.PETS
    }

    suspend fun findAll(offset: Int, limit: Int): List<PetsRecord> =
        connectionFactory.withContext { context ->
            context
                .selectFrom(TABLE)
                .offset(offset)
                .limit(limit)
                .toFlux()
                .collectList()
                .awaitSingle()
        }

    suspend fun findById(id: UUID): PetsRecord? =
        connectionFactory.withContext { context ->
            context
                .selectFrom(TABLE)
                .where(TABLE.ID.eq(id))
                .toFlux()
                .awaitFirstOrNull()
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

    suspend fun create(record: PetsRecord): Int =
        connectionFactory.withContext { context ->
            context
                .insertInto(TABLE, TABLE.ID, TABLE.NAME, TABLE.STATUS)
                .valuesOfRecords(record)
                .awaitSingle()
        }

    suspend fun update(record: PetsRecord): Int =
        connectionFactory.withContext { context ->
            context
                .update(TABLE)
                .set(record)
                .where(TABLE.ID.eq(record.id))
                .toFlux()
                .awaitSingle()
        }

    suspend fun deleteById(id: UUID): Int =
        connectionFactory.withContext { context ->
            context
                .deleteFrom(TABLE)
                .where(TABLE.ID.eq(id))
                .toFlux()
                .awaitSingle()
        }
}
