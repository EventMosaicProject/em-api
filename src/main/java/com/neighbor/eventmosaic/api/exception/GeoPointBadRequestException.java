package com.neighbor.eventmosaic.api.exception;

/**
 * Исключение, выбрасываемое при некорректных данных географической точки.
 */
public class GeoPointBadRequestException extends EmApiException {

    public GeoPointBadRequestException(String message) {
        super(message);
    }
}
