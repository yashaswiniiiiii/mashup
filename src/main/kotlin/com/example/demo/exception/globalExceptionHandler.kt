package com.example.demo.exception
import com.example.demo.model.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    @ExceptionHandler(UserNotFoundExceptionn::class)
    fun handleUserNotFoundException(ex: UserNotFoundExceptionn): ResponseEntity<ErrorResponse> {
       logger.info("User not found error occured")
        println("ERROR: Code = ${ex.errorCode}, Message = ${ex.message}")

        val errorResponse = ErrorResponse(ex.errorCode, ex.message ?: "User not found")
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
  @ExceptionHandler(MethodArgumentNotValidExceptionn :: class)
  fun handleMethodArgumentMismatch(ex: MethodArgumentNotValidExceptionn): ResponseEntity<ErrorResponse> {
      logger.info("MethodargumentMismatch error occured")
      val errorResponse = ErrorResponse(ex.errorCode, ex.message ?: "User not found")
      return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
  }
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        logger.info("BadRequest error occured")
        val errorResponse = ErrorResponse(ex.errorCode,ex.message ?:"Input Mismatch")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(HttpMessageNotReadableExceptionn::class)
    fun handlemethodArgumentNotValidException(ex: HttpMessageNotReadableExceptionn): ResponseEntity<ErrorResponse> {
        logger.info("BadRequest error occured")
        val errorResponse = ErrorResponse(ex.errorCode,ex.message ?:"Input Mismatch")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse>{
        logger.info("Internal serveer error occured")
        val errorerponse= ErrorResponse(500, "An unexpected error occured")
        return ResponseEntity(errorerponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  @ExceptionHandler(conflictException::class)
  fun handleConflictException(ex: conflictException): ResponseEntity<ErrorResponse>{
      logger.info("conflict error occured")
      val errorerponse= ErrorResponse(409, "An unexpected error occured")
      return ResponseEntity(errorerponse, HttpStatus.CONFLICT);
  }


}
