package com.ecommerce.users.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ecommerce.users.dto.ErrorResponse;

import feign.FeignException;
import jakarta.validation.ValidationException;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

      @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
      ValidationException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(ErrorMessages.ERROR_VALIDATION).extraMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity<FeignException> handleInternalException(
      FeignException ex, WebRequest request) {
        return new ResponseEntity<FeignException>(ex, null, ex.status());
    }
}


