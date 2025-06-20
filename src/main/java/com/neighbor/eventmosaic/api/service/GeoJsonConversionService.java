package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeature;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;

import java.util.List;

/**
 * Сервис для конвертации событий в формат GeoJSON.
 */
public interface GeoJsonConversionService {

    /**
     * Конвертирует список событий в GeoJSON FeatureCollection.
     *
     * @param events список событий
     * @return GeoJSON FeatureCollection
     */
    GeoJsonFeatureCollection convertToGeoJson(List<EventDocument> events);

    /**
     * Конвертирует одно событие в GeoJSON Feature.
     *
     * @param event событие
     * @return GeoJSON Feature или null если событие не может быть конвертировано
     */
    GeoJsonFeature convertEventToFeature(EventDocument event);
} 