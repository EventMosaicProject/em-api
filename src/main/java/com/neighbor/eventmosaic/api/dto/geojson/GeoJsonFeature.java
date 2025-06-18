package com.neighbor.eventmosaic.api.dto.geojson;

import com.neighbor.eventmosaic.api.dto.ApiGeoPoint;
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

    /**
     * Безопасный фабричный метод для создания GeoJsonFeature.
     * Возвращает null если apiGeoPoint является null, предотвращая создание невалидного GeoJSON.
     *
     * @param apiGeoPoint координаты для геометрии.
     * @param properties  свойства для feature.
     * @return GeoJsonFeature или null если координаты отсутствуют.
     */
    public static GeoJsonFeature createSafe(ApiGeoPoint apiGeoPoint,
                                            GeoJsonProperties properties) {
        GeoJsonGeometry geometry = GeoJsonGeometry.fromApiGeoPoint(apiGeoPoint);
        if (geometry == null) {
            return null; // Не создаем Feature с невалидной геометрией
        }
        return new GeoJsonFeature("Feature", geometry, properties);
    }

} 