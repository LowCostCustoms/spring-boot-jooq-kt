package com.lowcostcustoms.plyaground.spring.jooq.utils.db

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class TransactionContextElement : AbstractCoroutineContextElement(TransactionContextElement) {
    companion object Key : CoroutineContext.Key<TransactionContextElement>
}
