package com.lowcostcustoms.plyaground.spring.jooq.models

import javax.validation.constraints.NotBlank

data class TagRequest(@field:NotBlank val name: String)
