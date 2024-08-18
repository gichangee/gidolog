package com.gildong.api.controller;

import com.gildong.api.exception.GildonglogException;
import com.gildong.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public  ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다")
                .build();
        
        for(FieldError fieldError : e.getFieldErrors()){
            response.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
        }

        return response;

    }

    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GildonglogException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> gildonglogException(GildonglogException e){

        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);
        return response;

    }

}
