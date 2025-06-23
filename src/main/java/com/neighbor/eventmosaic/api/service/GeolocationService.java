package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.document.EventDocument;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Optional;

/**
 * Сервис для работы с геолокацией событий GDELT.
 * Отвечает за выбор оптимальных координат для отображения событий на карте.
 */
public interface GeolocationService {

    /**
     * Выбирает наилучшие координаты для события согласно приоритету:
     * 1. ActionGeo (если точность достаточная)
     * 2. Actor1Geo
     * 3. Actor2Geo
     * 4. ActionGeo (любой, если остальные недоступны)
     *
     * @param eventDocument документ события
     * @return координаты события или Optional.empty() если геоданные отсутствуют
     */
    Optional<GeoPoint> selectBestCoordinates(EventDocument eventDocument);
} 