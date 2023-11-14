package com.weather.api.exceptions;


import org.springframework.http.HttpStatus;

import java.util.Map;
public abstract class ApplicationException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public ApplicationException(HttpStatus status, String message){
        super(Map.of("Status", status.toString(), "error", message).toString());
        this.status = status;
        this.message = message;
    }
}
