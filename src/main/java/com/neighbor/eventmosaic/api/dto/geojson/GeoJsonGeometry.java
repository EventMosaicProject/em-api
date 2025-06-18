package com.neighbor.eventmosaic.api.dto.geojson;

import com.neighbor.eventmosaic.api.dto.ApiGeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представление геометрии для GeoJSON Feature.
 * У нас это всегда будет точка.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonGeometry {

    /**
     * Тип геометрии, всегда "Point" для отдельных событий/кластеров.
     */
    private String type = "Point";

    /**
     * Координаты точки [долгота, широта].
     * ВАЖНО: GeoJSON использует порядок [longitude, latitude].
     */
    private double[] coordinates; // [lon, lat]

    /**
     * Конструктор для удобного создания из GeoPoint.
     * @param apiGeoPoint точка с широтой и долготой.
     */
    public GeoJsonGeometry(ApiGeoPoint apiGeoPoint) {
        if (apiGeoPoint != null) {
            this.coordinates = new double[]{apiGeoPoint.lon(), apiGeoPoint.lat()};
        }
    }
} 