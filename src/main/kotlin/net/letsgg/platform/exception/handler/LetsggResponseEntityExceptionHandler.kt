package net.letsgg.platform.exception.handler

import net.letsgg.platform.exception.EmailAlreadyInUseException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class LetsggResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class])
    protected fun handleRuntimeException(ex: RuntimeException, req: ServletWebRequest): ResponseEntity<Any>? {
        logger.error(ex.message)
        return handleException(ex, req)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    protected fun handleConflict(ex: RuntimeException, req: ServletWebRequest): ResponseEntity<Any> {
        logger.error(ex.message)
        val apiError = ApiError(
            HttpStatus.NOT_ACCEPTABLE,
            "Illegal argument/ state occurred",
            req.request.requestURI)
        return handleExceptionInternal(ex, apiError, HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, req)
    }

    @ExceptionHandler(value = [EmailAlreadyInUseException::class])
    protected fun handleResourceNotFoundException(ex: EmailAlreadyInUseException, req: ServletWebRequest): ResponseEntity<Any> {
        logger.error(ex.message)
        val apiError = ApiError(HttpStatus.NOT_ACCEPTABLE, ex.message!!, req.request.requestURI)
        return handleExceptionInternal(
            ex,
            apiError,
            HttpHeaders(),
            apiError.error,
            req
        )
    }
}