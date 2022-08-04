package com.lowcostcustoms.plyaground.spring.jooq.utils

import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

fun <T> Publisher<T>.toFlux() = Flux.from(this)
