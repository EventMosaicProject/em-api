package com.neighbor.eventmosaic.api.dto;

import com.neighbor.eventmosaic.api.validation.ValidBoundingBox;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO для параметров запроса событий на карте.
 * Используется для биндинга query-параметров в методе контроллера.
 */
@Data
public class EventMapQueryParameters {

    @Parameter(description = "Начальная дата для фильтрации (включительно), формат YYYY-MM-DD", example = "2025-06-20")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate since;

    @Parameter(description = "Конечная дата для фильтрации (включительно), формат YYYY-MM-DD", example = "2025-06-21")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate until;

    @Parameter(description = "Конкретная дата для фильтрации, формат YYYY-MM-DD. Игнорируется, если заданы since или until.", example = "2025-06-20")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Parameter(description = "Bounding box для фильтрации в формате 'minLat,minLon,maxLat,maxLon'",
            example = "55.6,37.5,55.8,37.7")
    @ValidBoundingBox
    private String bbox;

    @Parameter(description = "Уровень масштабирования карты (zoom)", example = "10")
    private Integer zoom;
} 