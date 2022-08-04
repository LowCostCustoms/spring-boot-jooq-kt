package com.lowcostcustoms.plyaground.spring.jooq.utils.db

import io.r2dbc.postgresql.api.PostgresqlException
import org.jooq.exception.DataAccessException

fun DataAccessException.isConstraintViolation(vararg constraints: String): Boolean =
    this.cause.let { cause ->
        cause != null
                && cause is PostgresqlException
                && constraints.contains(cause.errorDetails.constraintName.orElse(""))
    }
