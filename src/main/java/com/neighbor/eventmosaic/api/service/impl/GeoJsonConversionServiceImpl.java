package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.ApiGeoPoint;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeature;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonProperties;
import com.neighbor.eventmosaic.api.mapper.GeoJsonPropertiesMapper;
import com.neighbor.eventmosaic.api.service.GeoJsonConversionService;
import com.neighbor.eventmosaic.api.service.GeolocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Реализация сервиса для конвертации событий в формат GeoJSON.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoJsonConversionServiceImpl implements GeoJsonConversionService {

    private final GeolocationService geolocationService;
    private final GeoJsonPropertiesMapper geoJsonPropertiesMapper;

    /**
     * Конвертирует список событий в GeoJSON FeatureCollection
     *
     * @param events список событий
     * @return GeoJSON FeatureCollection
     */
    @Override
    public GeoJsonFeatureCollection convertToGeoJson(List<EventDocument> events) {
        log.debug("Конвертация {} событий в GeoJSON", events.size());

        List<GeoJsonFeature> features = events.stream()
                .map(this::convertEventToFeature)
                .filter(Objects::nonNull)
                .toList();

        log.debug("Успешно конвертировано {} из {} событий", features.size(), events.size());
        return new GeoJsonFeatureCollection(features);
    }

    /**
     * Конвертирует событие в GeoJSON Feature.
     * Если координаты не найдены, то событие пропускается.
     *
     * @param event событие
     * @return GeoJSON Feature
     */
    @Override
    public GeoJsonFeature convertEventToFeature(EventDocument event) {
        try {
            Optional<GeoPoint> geoPointOpt = geolocationService.selectBestCoordinates(event);

            if (geoPointOpt.isEmpty()) {
                log.debug("Событие {} пропущено - нет координат", event.getGlobalEventId());
                return null;
            }

            GeoPoint geoPoint = geoPointOpt.get();
            ApiGeoPoint apiGeoPoint = new ApiGeoPoint(geoPoint.getLat(), geoPoint.getLon());

            GeoJsonProperties properties = geoJsonPropertiesMapper.toSingleEventProperties(
                    event.getGlobalEventId(),
                    event.getAvgTone()
            );

            return GeoJsonFeature.createSafe(apiGeoPoint, properties);

        } catch (Exception e) {
            log.warn("Ошибка при конвертации события {} в GeoJSON: {}",
                    event.getGlobalEventId(), e.getMessage());
            return null;
        }
    }
} 