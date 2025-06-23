package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.config.ApiConfigProperties;
import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;
import com.neighbor.eventmosaic.api.repository.ElasticEventRepository;
import com.neighbor.eventmosaic.api.service.EventQueryService;
import com.neighbor.eventmosaic.api.service.GeolocationService;
import com.neighbor.eventmosaic.api.service.ParameterParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для выполнения запросов к репозиторию событий.
 * Выполняет поиск событий в указанном временном диапазоне, применяет географический фильтр,
 * ограничивает количество результатов и возвращает список событий.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventQueryServiceImpl implements EventQueryService {

    private final ElasticEventRepository eventRepository;
    private final ParameterParsingService parameterParsingService;
    private final GeolocationService geolocationService;
    private final ApiConfigProperties apiConfigProperties;

    /**
     * Выполняет поиск событий по параметрам.
     * Пример запроса:
     * GET /api/v1/events/map?bbox=55.7558,37.6176,55.7776,37.6394&since=2025-06-18T00:00:00Z&until=2025-06-19T00:00:00Z
     *
     * @param params параметры запроса
     * @return список событий
     */
    @Override
    public List<EventDocument> findEvents(EventMapQueryParameters params) {
        log.debug("Поиск событий по параметрам: {}", params);

        // Определяем временной диапазон
        OffsetDateTime[] dateRange = createDateRange(params);
        OffsetDateTime startDateTime = dateRange[0];
        OffsetDateTime endDateTime = dateRange[1];

        log.debug("Временной диапазон: {} - {}", startDateTime, endDateTime);

        // Получаем события из репозитория
        List<EventDocument> events = findEventsInTimeRange(startDateTime, endDateTime);
        log.debug("Найдено {} событий в указанном временном диапазоне", events.size());

        // Применяем географический фильтр если задан bbox
        List<EventDocument> filteredEvents = applyGeographicalFilter(events, params.getBbox());
        log.debug("После географической фильтрации осталось {} событий", filteredEvents.size());

        // Ограничиваем количество результатов
        return limitResults(filteredEvents);
    }

    /**
     * Создает временной диапазон на основе параметров запроса.
     * Если параметр since не задан, то используется текущая дата.
     * Если параметр until не задан, то используется текущая дата + 1 день.
     */
    private OffsetDateTime[] createDateRange(EventMapQueryParameters params) {
        OffsetDateTime since = Optional.ofNullable(params.getSince())
                .map(date -> date.atStartOfDay().atOffset(ZoneOffset.UTC))
                .orElse(null);

        OffsetDateTime until = Optional.ofNullable(params.getUntil())
                .map(date -> date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC))
                .orElse(null);

        return parameterParsingService.createDateRange(since, until, apiConfigProperties.getDefaultEventWindowHours());
    }

    /**
     * Находит события в указанном временном диапазоне.
     * Вынимает из базы события в указанном временном диапазоне.
     */
    private List<EventDocument> findEventsInTimeRange(OffsetDateTime startDateTime,
                                                      OffsetDateTime endDateTime) {
        int maxResults = apiConfigProperties.getMaxIndividualEventsPerRequest() * 2; // Берем больше для фильтрации
        Pageable pageable = PageRequest.of(0, maxResults);

        return eventRepository.findByEventDateBetween(startDateTime, endDateTime, pageable)
                .getContent();
    }

    /**
     * Применяет географический фильтр если указан bounding box.
     * Если bounding box не задан, то фильтр не применяется.
     */
    private List<EventDocument> applyGeographicalFilter(List<EventDocument> events,
                                                        String bboxStr) {
        if (bboxStr == null || bboxStr.trim().isEmpty()) {
            log.debug("Географический фильтр не применяется - bbox не задан");
            return events;
        }

        try {
            double[] bbox = parameterParsingService.parseBoundingBox(bboxStr);
            return filterEventsByBoundingBox(events, bbox);
        } catch (Exception e) {
            log.warn("Ошибка при парсинге bbox '{}': {}. Пропускаем географический фильтр", bboxStr, e.getMessage());
            return events;
        }
    }

    /**
     * Фильтрует события по bounding box.
     * Если событие не попадает в bounding box, то оно пропускается.
     */
    private List<EventDocument> filterEventsByBoundingBox(List<EventDocument> events,
                                                          double[] bbox) {
        double minLat = bbox[0];
        double minLon = bbox[1];
        double maxLat = bbox[2];
        double maxLon = bbox[3];

        log.debug("Применяется географический фильтр: minLat={}, minLon={}, maxLat={}, maxLon={}",
                minLat, minLon, maxLat, maxLon);

        return events.stream()
                .filter(event -> isEventInBounds(event, minLat, minLon, maxLat, maxLon))
                .toList();
    }

    /**
     * Проверяет, попадает ли событие в заданные границы.
     * Если событие не имеет координат, то оно пропускается.
     */
    private boolean isEventInBounds(EventDocument event,
                                    double minLat,
                                    double minLon,
                                    double maxLat,
                                    double maxLon) {
        Optional<GeoPoint> geoPointOpt = geolocationService.selectBestCoordinates(event);

        if (geoPointOpt.isEmpty()) {
            return false;
        }

        GeoPoint geoPoint = geoPointOpt.get();
        double lat = geoPoint.getLat();
        double lon = geoPoint.getLon();

        return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
    }

    /**
     * Ограничивает результаты до максимального количества.
     * Если количество событий меньше или равно максимальному количеству, то возвращается исходный список.
     * Иначе возвращается список с ограниченным количеством событий.
     */
    private List<EventDocument> limitResults(List<EventDocument> events) {
        int maxResults = apiConfigProperties.getMaxIndividualEventsPerRequest();

        if (events.size() <= maxResults) {
            return events;
        }

        log.info("Результат ограничен до {} событий из {}", maxResults, events.size());
        return events.stream().limit(maxResults).toList();
    }
} 