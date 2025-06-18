package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.config.ApiConfigProperties;
import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;
import com.neighbor.eventmosaic.api.exception.ResourceNotFoundException;
import com.neighbor.eventmosaic.api.mapper.EventDetailsMapper;
import com.neighbor.eventmosaic.api.mapper.GeoJsonPropertiesMapper;
import com.neighbor.eventmosaic.api.mapper.MentionMapper;
import com.neighbor.eventmosaic.api.repository.ElasticEventRepository;
import com.neighbor.eventmosaic.api.repository.ElasticMentionRepository;
import com.neighbor.eventmosaic.api.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для получения информации о событиях GDELT.
 */
// TODO: Реализовать методы
@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final ElasticEventRepository eventRepository;
    private final ElasticMentionRepository mentionRepository;
    private final EventDetailsMapper eventDetailsMapper;
    private final GeoJsonPropertiesMapper geoJsonPropertiesMapper;
    private final MentionMapper mentionMapper;
    private final ApiConfigProperties apiConfigProperties;

    /**
     * Получает события и/или кластеры событий для отображения на карте.
     *
     * @param params параметры запроса (диапазон дат, bbox, zoom)
     * @return GeoJsonFeatureCollection с событиями/кластерами
     */
    @Override
    public GeoJsonFeatureCollection getEventsForMap(EventMapQueryParameters params) {
        return null;
    }

    /**
     * Получает детальную информацию о конкретном событии, включая список упоминаний.
     *
     * @param eventId глобальный идентификатор события
     * @return EventDetailsResponse с деталями события
     * @throws ResourceNotFoundException если событие с указанным ID не найдено
     */
    @Override
    public EventDetailsResponse getEventDetails(Long eventId) throws ResourceNotFoundException {
        return null;
    }
} 