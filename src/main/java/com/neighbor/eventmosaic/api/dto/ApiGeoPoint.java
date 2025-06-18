package com.neighbor.eventmosaic.api.dto;

/**
 * DTO для представления географической точки (широта и долгота).
 */
public record ApiGeoPoint(
        double lat, // Широта
        double lon  // Долгота
) {
} 