package com.neighbor.eventmosaic.api.exception;

/**
 * Исключение, выбрасываемое, когда запрашиваемый ресурс не найден.
 * Приводит к HTTP ответу 404 Not Found.
 */
public class ResourceNotFoundException extends EmApiException {

    /**
     * Конструктор с сообщением об ошибке.
     * @param message сообщение об ошибке
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением и причиной ошибки.
     * @param message сообщение об ошибке
     * @param cause причина ошибки
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 