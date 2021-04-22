package net.letsgg.platform.exception.handler

import net.letsgg.platform.exception.EmailAlreadyInUseException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class AppResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * TODO. Handle UnfinishedSetupUserException
     * */
//    @ExceptionHandler(value = [SecurityException::class])
//    protected fun handleUnfinishedSetupUserException(ex: SecurityException, req: ServletWebRequest) {
//        req.userPrincipal?.
//    }

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    protected fun handleConflict(ex: RuntimeException, req: ServletWebRequest): ResponseEntity<Any> {
        logger.error("Illegal argument/state detected", ex)
        val apiError = ApiError(
            HttpStatus.NOT_ACCEPTABLE,
            ex.message,
            req.request.requestURI
        )
        return handleExceptionInternal(ex, apiError, HttpHeaders(), apiError.httpStatus, req)
    }

    @ExceptionHandler(value = [EmailAlreadyInUseException::class])
    protected fun handleResourceNotFoundException(
        ex: EmailAlreadyInUseException,
        req: ServletWebRequest
    ): ResponseEntity<Any> {
        logger.error("Email already in use", ex)
        val apiError = ApiError(HttpStatus.NOT_ACCEPTABLE, ex.message, req.request.requestURI)
        return handleExceptionInternal(
            ex,
            apiError,
            HttpHeaders(),
            apiError.httpStatus,
            req
        )
    }

    @ExceptionHandler(value = [ConstraintViolationException::class]) //FIXME handle wrapper exceptions as well (i.e. JpaSystemException)
    protected fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        req: ServletWebRequest
    ): ResponseEntity<Any> {
        logger.error("Constraint violation occurred", ex)
        val apiError = ApiError(HttpStatus.CONFLICT, ex.message, req.request.requestURI)
        return handleExceptionInternal(
            ex,
            apiError,
            HttpHeaders(),
            apiError.httpStatus,
            req
        )
    }
}