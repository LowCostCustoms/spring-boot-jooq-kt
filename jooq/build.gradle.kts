import org.jooq.meta.jaxb.Property

plugins {
    id("nu.studer.jooq") version "7.1.1"
}

val jooqVersion by extra("3.16.6")

dependencies {
    // JOOQ
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-meta:$jooqVersion")
    implementation("org.jooq:jooq-meta-extensions:$jooqVersion")

    // JOOQ Generator dependencies
    jooqGenerator("org.jooq:jooq-meta:$jooqVersion")
    jooqGenerator("org.jooq:jooq-meta-extensions:$jooqVersion")
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties.apply {
                            add(Property().withKey("scripts").withValue("src/main/resources/schema.sql"))
                        }
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isPojosAsKotlinDataClasses = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.lowcostcustoms.plyaground.spring.jooq.db"
                        directory = "build/generated-src/jooq/main"
                    }
                }
            }
        }
    }
}
