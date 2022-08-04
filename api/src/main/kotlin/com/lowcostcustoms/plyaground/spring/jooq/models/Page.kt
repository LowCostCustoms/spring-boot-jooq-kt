package com.lowcostcustoms.plyaground.spring.jooq.models

data class Page<T>(val count: Int, val results: List<T>)
