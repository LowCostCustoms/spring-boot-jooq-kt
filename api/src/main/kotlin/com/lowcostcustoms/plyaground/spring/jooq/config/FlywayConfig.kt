package com.lowcostcustoms.plyaground.spring.jooq.config

import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    @Value("\${spring.r2dbc.url}") url: String,
    @Value("\${spring.r2dbc.username}") username: String,
    @Value("\${spring.r2dbc.password}") password: String,
) {
    private val logger = KotlinLogging.logger { }

    init {
        logger.info { "Applying database migrations" }
        val flyway = Flyway
            .configure()
            .dataSource(url.replace("r2dbc:", "jdbc:"), username, password)
            .load()
        flyway.migrate()
    }
}
