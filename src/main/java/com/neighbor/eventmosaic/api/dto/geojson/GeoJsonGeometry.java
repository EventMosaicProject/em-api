package com.neighbor.eventmosaic.api.dto.geojson;

import com.neighbor.eventmosaic.api.dto.ApiGeoPoint;
import com.neighbor.eventmosaic.api.exception.GeoPointBadRequestException;
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
     * Конструктор для создания из GeoPoint.
     *
     * @param apiGeoPoint точка с широтой и долготой (не может быть null).
     * @throws GeoPointBadRequestException если apiGeoPoint является null.
     */
    public GeoJsonGeometry(ApiGeoPoint apiGeoPoint) {
        if (apiGeoPoint == null) {
            throw new GeoPointBadRequestException("Географическая точка не может быть null");
        }
        this.coordinates = new double[]{apiGeoPoint.lon(), apiGeoPoint.lat()};
    }

    /**
     * Безопасный фабричный метод для создания GeoJsonGeometry.
     * Возвращает null если apiGeoPoint является null, вместо создания невалидного GeoJSON.
     *
     * @param apiGeoPoint точка с широтой и долготой.
     * @return GeoJsonGeometry или null если входная точка null.
     */
    public static GeoJsonGeometry fromApiGeoPoint(ApiGeoPoint apiGeoPoint) {
        if (apiGeoPoint == null) {
            return null;
        }
        return new GeoJsonGeometry(apiGeoPoint);
    }
} 