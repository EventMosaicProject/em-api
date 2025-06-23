package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.config.ApiConfigProperties;
import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.document.MentionDocument;
import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;
import com.neighbor.eventmosaic.api.exception.ResourceNotFoundException;
import com.neighbor.eventmosaic.api.repository.ElasticEventRepository;
import com.neighbor.eventmosaic.api.repository.ElasticMentionRepository;
import com.neighbor.eventmosaic.api.service.EventDetailsService;
import com.neighbor.eventmosaic.api.service.EventQueryService;
import com.neighbor.eventmosaic.api.service.EventService;
import com.neighbor.eventmosaic.api.service.GeoJsonConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Основная реализация сервиса событий.
 * Координирует работу специализированных сервисов для обеспечения функциональности событий.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final ElasticEventRepository eventRepository;
    private final ElasticMentionRepository mentionRepository;
    private final EventQueryService eventQueryService;
    private final GeoJsonConversionService geoJsonConversionService;
    private final EventDetailsService eventDetailsService;
    private final ApiConfigProperties apiConfigProperties;

    /**
     * Получает события для отображения на карте в формате GeoJSON.
     *
     * @param params параметры запроса (даты, bbox, zoom)
     * @return GeoJSON FeatureCollection с событиями
     */
    @Override
    public GeoJsonFeatureCollection getEventsForMap(EventMapQueryParameters params) {
        log.debug("Запрос событий для карты: {}", params);

        try {
            List<EventDocument> events = eventQueryService.findEvents(params);
            return geoJsonConversionService.convertToGeoJson(events);

        } catch (Exception e) {
            log.error("Ошибка при получении событий для карты: {}", e.getMessage(), e);
            return GeoJsonFeatureCollection.empty();
        }
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
        log.debug("Запрос деталей события: eventId={}", eventId);

        EventDocument event = findEventByIdOrThrow(eventId);
        List<MentionDocument> mentions = findMentionsForEvent(event);

        log.debug("Найдено {} упоминаний для события {}", mentions.size(), eventId);

        return eventDetailsService.buildEventDetailsResponse(event, mentions);
    }

    /**
     * Находит событие по ID или выбрасывает исключение.
     */
    private EventDocument findEventByIdOrThrow(Long eventId) throws ResourceNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Событие не найдено: eventId={}", eventId);
                    return new ResourceNotFoundException("Событие с ID " + eventId + " не найдено");
                });
    }

    /**
     * Находит упоминания для события в рамках временного окна.
     */
    private List<MentionDocument> findMentionsForEvent(EventDocument event) {
        OffsetDateTime eventDateTime = event.getEventDate();
        OffsetDateTime searchEndTime = eventDateTime.plusDays(apiConfigProperties.getMentionSearchDaysRange());

        return mentionRepository.findByGlobalEventIdAndMentionTimeDateBetween(
                event.getGlobalEventId(), eventDateTime, searchEndTime);
    }
} 