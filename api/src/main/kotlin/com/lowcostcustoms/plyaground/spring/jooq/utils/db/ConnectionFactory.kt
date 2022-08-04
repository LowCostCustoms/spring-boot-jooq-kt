package com.lowcostcustoms.plyaground.spring.jooq.utils.db

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.Settings
import org.jooq.impl.DSL

suspend fun <T> ConnectionFactory.withConnection(block: suspend (Connection) -> T): T {
    val currentContextElement = coroutineContext[ConnectionContextElement]
    return if (currentContextElement != null) {
        block(currentContextElement.connection)
    } else {
        val connection = create().awaitSingle()
        try {
            kotlinx.coroutines.withContext(coroutineContext + ConnectionContextElement(connection)) {
                block(connection)
            }
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }
}

suspend fun <T> ConnectionFactory.withContext(block: suspend (DSLContext) -> T): T =
    withConnection { connection ->
        block(
            DSL.using(
                connection,
                SQLDialect.POSTGRES,
                Settings().withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED),
            ),
        )
    }
