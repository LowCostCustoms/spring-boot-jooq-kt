# spring-boot-jooq-kt

An example of integrating JOOQ database mapper with Spring Boot reactive stack.

## Local Setup

Spin up a postgres database using docker:

```shell
docker run --rm --env-file .env -p "5432:5432" postgres:13-alpine -c log_statement=all
```

Run the application:

```shell
./gradlew bootRun
```

Navigate to http://localhost:8080/swagger to see the Swagger UI.
