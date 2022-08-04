package com.lowcostcustoms.plyaground.spring.jooq.utils.db

import io.r2dbc.spi.ConnectionFactory
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class TransactionManager(private val connectionFactory: ConnectionFactory) {
    suspend fun <T> withTransaction(block: suspend () -> T): T {
        val currentTransaction = coroutineContext[TransactionContextElement]
        return if (currentTransaction != null) {
            block()
        } else {
            connectionFactory.withConnection { connection ->
                connection.beginTransaction().awaitFirstOrNull()
                try {
                    withContext(coroutineContext + TransactionContextElement()) {
                        block().also { connection.commitTransaction().awaitFirstOrNull() }
                    }
                } finally {
                    connection.rollbackTransaction().awaitFirstOrNull()
                }
            }
        }
    }
}
