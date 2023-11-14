package com.weather.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadInputException extends ApplicationException{
    public BadInputException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        log.error(message);
    }
}
