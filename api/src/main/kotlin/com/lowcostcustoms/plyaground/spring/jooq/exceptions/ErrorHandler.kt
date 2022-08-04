package com.lowcostcustoms.plyaground.spring.jooq.exceptions

import com.lowcostcustoms.plyaground.spring.jooq.models.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    suspend fun handle(ex: NotFoundException): ErrorResponse =
        ErrorResponse(detail = ex.message.orEmpty())

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    suspend fun handle(ex: BadRequestException): ErrorResponse =
        ErrorResponse(detail = ex.message.orEmpty())
}
