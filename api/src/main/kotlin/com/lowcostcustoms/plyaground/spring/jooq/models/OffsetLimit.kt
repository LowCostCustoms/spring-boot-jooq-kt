package com.lowcostcustoms.plyaground.spring.jooq.models

import javax.validation.constraints.Max
import javax.validation.constraints.Min

open class OffsetLimit(
    @field:Min(0)
    val offset: Int = 0,
    @field:Min(1)
    @field:Max(1000)
    val limit: Int = 100,
)
