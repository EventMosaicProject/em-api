package com.neighbor.eventmosaic.api.dto.geojson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Представление GeoJSON FeatureCollection.
 * Это корневой объект для ответа API /events.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonFeatureCollection {

    /**
     * Тип объекта, всегда FeatureCollection
     */
    private String type = "FeatureCollection";

    /**
     * Список GeoJSON Feature (событий или кластеров).
     */
    private List<GeoJsonFeature> features = new ArrayList<>();

    /**
     * Конструктор для инициализации с предопределенным списком фич.
     * Автоматически фильтрует null значения для обеспечения валидности GeoJSON.
     *
     * @param features список фич
     */
    public GeoJsonFeatureCollection(List<GeoJsonFeature> features) {
        this.features = Objects.requireNonNullElse(features, Collections.<GeoJsonFeature>emptyList())
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Создает пустую FeatureCollection.
     *
     * @return пустая GeoJsonFeatureCollection
     */
    public static GeoJsonFeatureCollection empty() {
        return new GeoJsonFeatureCollection(Collections.emptyList());
    }
} 