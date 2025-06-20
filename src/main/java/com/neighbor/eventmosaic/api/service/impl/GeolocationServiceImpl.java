package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.service.GeolocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация сервиса для работы с геолокацией событий GDELT.
 */
@Slf4j
@Service
public class GeolocationServiceImpl implements GeolocationService {

    private static final int COUNTRY_LEVEL_TYPE = 1; // Уровень страны (низкая точность)

    /**
     * Выбирает наилучшие координаты для события из доступных геолокаций.
     * Приоритет: ActionGeo > Actor1Geo > Actor2Geo.
     * Если ни одна из геолокаций не точна, возвращает ActionGeo как запасной вариант.
     *
     * @param eventDocument документ события
     * @return Optional с наилучшими координатами или пустой Optional, если координаты отсутствуют
     */
    @Override
    public Optional<GeoPoint> selectBestCoordinates(EventDocument eventDocument) {
        if (eventDocument == null) {
            log.debug("EventDocument is null, возвращаем пустой Optional");
            return Optional.empty();
        }

        log.debug("Выбираем координаты для события: {}", eventDocument.getGlobalEventId());

        // 1. Пробуем ActionGeo, если оно достаточно точное
        if (eventDocument.getActionLocation() != null &&
                isPreciseEnough(eventDocument.getActionGeoType())) {
            log.debug("Используем ActionGeo (точность: {})", eventDocument.getActionGeoType());
            return Optional.of(eventDocument.getActionLocation());
        }

        // 2. Пробуем Actor1Geo
        if (eventDocument.getActor1Location() != null &&
                isPreciseEnough(eventDocument.getActor1GeoType())) {
            log.debug("Используем Actor1Geo (точность: {})", eventDocument.getActor1GeoType());
            return Optional.of(eventDocument.getActor1Location());
        }

        // 3. Пробуем Actor2Geo
        if (eventDocument.getActor2Location() != null &&
                isPreciseEnough(eventDocument.getActor2GeoType())) {
            log.debug("Используем Actor2Geo (точность: {})", eventDocument.getActor2GeoType());
            return Optional.of(eventDocument.getActor2Location());
        }

        // 4. Если ничего точного нет, используем ActionGeo даже если неточное
        if (eventDocument.getActionLocation() != null) {
            log.debug("Используем ActionGeo как fallback (точность: {})",
                    eventDocument.getActionGeoType());
            return Optional.of(eventDocument.getActionLocation());
        }

        log.debug("Координаты отсутствуют для события: {}", eventDocument.getGlobalEventId());
        return Optional.empty();
    }

    /**
     * Проверяет, достаточно ли точны координаты для отображения.
     * В GDELT Type=1 означает уровень страны (низкая точность).
     * Если точность равна COUNTRY_LEVEL_TYPE, то используем ActionGeo как запасной вариант.
     *
     * @param geoType тип геопозиции из GDELT (может быть Integer или Short)
     * @return true, если координаты достаточно точны
     */
    private boolean isPreciseEnough(Number geoType) {
        return geoType != null && geoType.intValue() > COUNTRY_LEVEL_TYPE;
    }
} 