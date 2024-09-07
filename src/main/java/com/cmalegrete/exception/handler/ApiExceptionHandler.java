package com.cmalegrete.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request, BindingResult result) {  
        log.error("Api error - {}", e.getMessage());

        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Validation error", result);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorMessage);
    }
    
}
