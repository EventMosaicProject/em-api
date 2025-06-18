package com.neighbor.eventmosaic.api.dto.geojson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представление GeoJSON Feature.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonFeature {

    /**
     * Тип объекта, всегда "Feature".
     */
    private String type = "Feature";

    /**
     * Геометрия объекта (точка).
     */
    private GeoJsonGeometry geometry;

    /**
     * Свойства объекта (данные о событии/кластере).
     */
    private GeoJsonProperties properties;
} 