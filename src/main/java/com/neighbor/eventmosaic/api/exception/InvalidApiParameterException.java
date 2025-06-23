package com.neighbor.eventmosaic.api.exception;

/**
 * Исключение для невалидных параметров запроса.
 */
public class InvalidApiParameterException extends EmApiException {

    public InvalidApiParameterException(String message, Throwable cause) {
        super(message, cause);
    }
} 