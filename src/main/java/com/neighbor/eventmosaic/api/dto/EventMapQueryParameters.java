package com.neighbor.eventmosaic.api.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO для параметров запроса событий на карте.
 */
@Data
public class EventMapQueryParameters {

    /**
     * Начальная дата диапазона (включительно).
     * Формат: YYYY-MM-DD.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate since;

    /**
     * Конечная дата диапазона (включительно).
     * Формат: YYYY-MM-DD.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate until;

    /**
     * Конкретная дата для запроса (если не используется диапазон since/until).
     * Формат: YYYY-MM-DD.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    /**
     * Границы видимой области карты (bounding box).
     * Формат: "minLat,minLon,maxLat,maxLon".
     * Пример: "34.0,-10.0,42.0,5.0".
     */
    private String bbox;

    /**
     * Текущий уровень масштабирования карты.
     * Используется для определения стратегии кластеризации.
     * Пример: 10.
     */
    private Integer zoom;
} 