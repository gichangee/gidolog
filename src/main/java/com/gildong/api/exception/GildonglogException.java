package com.gildong.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GildonglogException extends RuntimeException{

    private final Map<String,String> validation = new HashMap<String, String>();


    public GildonglogException(String message) {
        super(message);
    }

    public GildonglogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
