package net.letsgg.platform.exception.handler

import net.letsgg.platform.exception.EmailAlreadyInUseException
import net.letsgg.platform.exception.InternalException
import net.letsgg.platform.exception.InvalidLoginCredentialsException
import net.letsgg.platform.utility.INTERNAL_EXCEPTION
import net.letsgg.platform.utility.VALIDATION_EXCEPTION
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.function.Consumer
import javax.validation.ValidationException


@RestControllerAdvice
class AppResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {


  /**
   * TODO. Handle UnfinishedSetupUserException
   * */
//    @ExceptionHandler(value = [SecurityException::class])
//    protected fun handleUnfinishedSetupUserException(ex: SecurityException, req: ServletWebRequest) {
//        req.userPrincipal?.
//    }

  @ExceptionHandler(value = [Exception::class])
  protected fun handleInternalException(ex: Exception, req: ServletWebRequest): ResponseEntity<Any> {
    logger.error(String.format(INTERNAL_EXCEPTION, ex.localizedMessage), ex)
    val internalException = InternalException(String.format(INTERNAL_EXCEPTION, ex.localizedMessage), ex)
    val apiError =
      ApiError(HttpStatus.INTERNAL_SERVER_ERROR, internalException.localizedMessage, req.request.requestURI)
    return handleExceptionInternal(
      internalException,
      apiError,
      HttpHeaders(),
      HttpStatus.INTERNAL_SERVER_ERROR,
      req
    )
  }

  @ExceptionHandler(value = [InvalidLoginCredentialsException::class])
  protected fun handleInvalidLoginCredentials(
    ex: InvalidLoginCredentialsException,
    req: ServletWebRequest
  ): ResponseEntity<Any> {
    val apiError = ApiError(HttpStatus.UNAUTHORIZED, ex.localizedMessage, req.request.requestURI)
    return handleExceptionInternal(ex, apiError, HttpHeaders(), HttpStatus.UNAUTHORIZED, req)
  }

  @ExceptionHandler(value = [IllegalArgumentException::class])
  protected fun handleConflict(ex: IllegalArgumentException, req: ServletWebRequest): ResponseEntity<Any> {
    val apiError = ApiError(
      HttpStatus.NOT_ACCEPTABLE,
      ex.localizedMessage,
      req.request.requestURI
    )
    return handleExceptionInternal(ex, apiError, HttpHeaders(), apiError.httpStatus, req)
  }

  @ExceptionHandler(value = [EmailAlreadyInUseException::class])
  protected fun handleResourceNotFoundException(
    ex: EmailAlreadyInUseException,
    req: ServletWebRequest
  ): ResponseEntity<Any> {
    val apiError = ApiError(HttpStatus.NOT_ACCEPTABLE, ex.message, req.request.requestURI)
    return handleExceptionInternal(
      ex,
      apiError,
      HttpHeaders(),
      apiError.httpStatus,
      req
    )
  }

  @ExceptionHandler(value = [ValidationException::class]) //FIXME handle wrapper exceptions as well (i.e. JpaSystemException)
  protected fun handleValidationException(
    ex: ValidationException,
    req: ServletWebRequest
  ): ResponseEntity<Any> {
    logger.error(String.format(VALIDATION_EXCEPTION, ex.localizedMessage), ex)
    val apiError = ApiError(HttpStatus.CONFLICT, ex.message, req.request.requestURI)
    return handleExceptionInternal(
      ex,
      apiError,
      HttpHeaders(),
      apiError.httpStatus,
      req
    )
  }

  override fun handleMethodArgumentNotValid(
    ex: MethodArgumentNotValidException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val errors: MutableMap<String, String?> = HashMap()
    ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
      val fieldName = (error as FieldError).field
      val errorMessage = error.getDefaultMessage()
      errors[fieldName] = errorMessage
    })
    val apiError = ApiError(
      HttpStatus.BAD_REQUEST,
      String.format(VALIDATION_EXCEPTION, errors.keys.joinToString(",")),
      request.contextPath
    ).apply {
      this.errors = errors
    }
    return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
  }
}