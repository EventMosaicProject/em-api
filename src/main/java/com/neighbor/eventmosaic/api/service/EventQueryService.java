package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;

import java.util.List;

/**
 * Сервис для выполнения запросов к репозиторию событий.
 */
public interface EventQueryService {

    /**
     * Находит события по параметрам запроса.
     *
     * @param params параметры запроса (даты, bbox)
     * @return список найденных событий
     */
    List<EventDocument> findEvents(EventMapQueryParameters params);
} 