package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.document.MentionDocument;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;

import java.util.List;

/**
 * Сервис для формирования детального ответа о событии.
 * Отвечает за объединение данных события с упоминаниями.
 */
public interface EventDetailsService {

    /**
     * Создает полный ответ с деталями события и упоминаниями.
     *
     * @param eventDocument документ события
     * @param mentions      список упоминаний события
     * @return полный {@link EventDetailsResponse} с событием и упоминаниями
     */
    EventDetailsResponse buildEventDetailsResponse(EventDocument eventDocument, List<MentionDocument> mentions);
} 