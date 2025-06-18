package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;
import com.neighbor.eventmosaic.api.exception.ResourceNotFoundException;

/**
 * Сервис для обработки запросов, связанных с событиями GDELT.
 */
public interface EventService {

    /**
     * Получает события и/или кластеры событий для отображения на карте.
     *
     * @param params параметры запроса (диапазон дат, bbox, zoom)
     * @return GeoJsonFeatureCollection с событиями/кластерами
     */
    GeoJsonFeatureCollection getEventsForMap(EventMapQueryParameters params);

    /**
     * Получает детальную информацию о конкретном событии, включая список упоминаний.
     *
     * @param eventId глобальный идентификатор события
     * @return EventDetailsResponse с деталями события
     * @throws ResourceNotFoundException если событие с указанным ID не найдено
     */
    EventDetailsResponse getEventDetails(Long eventId) throws ResourceNotFoundException;

} 