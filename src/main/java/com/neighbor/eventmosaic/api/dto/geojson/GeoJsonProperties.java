package com.neighbor.eventmosaic.api.dto.geojson;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Свойства для GeoJSON Feature.
 * Содержит информацию о событии или кластере.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Не включать null поля в JSON
public class GeoJsonProperties {

    /**
     * Идентификатор события (GlobalEventID).
     * Присутствует только если это одиночное событие (clusterCount = 1).
     */
    private Long eventId;

    /**
     * Средний тон события или кластера.
     */
    private Double avgTone;

    /**
     * Количество событий в кластере.
     * Если равно 1, то это одиночное событие.
     */
    private int clusterCount;
} 