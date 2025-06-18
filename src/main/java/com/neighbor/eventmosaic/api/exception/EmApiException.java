package com.neighbor.eventmosaic.api.exception;

/**
 * Исключение, которое выбрасывается при ошибках в API Event Mosaic.
 * Это общее исключение для обработки ошибок, связанных с API.
 */
public class EmApiException extends RuntimeException {

    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message Сообщение об ошибке
     */
    public EmApiException(String message) {
        super(message);
    }

    /**
     * Конструктор для создания исключения с сообщением и причиной.
     *
     * @param message Сообщение об ошибке
     * @param cause   Причина ошибки
     */
    public EmApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
