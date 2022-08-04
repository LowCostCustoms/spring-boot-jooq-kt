package com.lowcostcustoms.plyaground.spring.jooq.utils.db

import io.r2dbc.spi.Connection
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class ConnectionContextElement(val connection: Connection) : AbstractCoroutineContextElement(ConnectionContextElement) {
    companion object Key : CoroutineContext.Key<ConnectionContextElement>
}
