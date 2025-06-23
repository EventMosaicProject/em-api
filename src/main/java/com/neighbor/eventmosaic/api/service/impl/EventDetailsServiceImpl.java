package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.document.MentionDocument;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import com.neighbor.eventmosaic.api.dto.details.MentionSummary;
import com.neighbor.eventmosaic.api.mapper.EventDetailsMapper;
import com.neighbor.eventmosaic.api.mapper.MentionMapper;
import com.neighbor.eventmosaic.api.service.EventDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Реализация сервиса для формирования детального ответа о событии.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventDetailsServiceImpl implements EventDetailsService {

    private final EventDetailsMapper eventDetailsMapper;
    private final MentionMapper mentionMapper;

    @Override
    public EventDetailsResponse buildEventDetailsResponse(EventDocument eventDocument,
                                                          List<MentionDocument> mentions) {
        log.debug("Формирование детального ответа для события: {}", eventDocument.getGlobalEventId());

        // Базовая информация о событии
        EventDetailsResponse response = eventDetailsMapper.toDetailsResponse(eventDocument);

        // Добавляем упоминания
        if (mentions != null && !mentions.isEmpty()) {
            List<MentionSummary> mentionSummaries = mentions.stream()
                    .map(mentionMapper::toMentionSummary)
                    .toList();
            response.setMentions(mentionSummaries);
            log.debug("Добавлено {} упоминаний к событию {}",
                    mentionSummaries.size(), eventDocument.getGlobalEventId());
        } else {
            response.setMentions(Collections.emptyList());
            log.debug("Упоминания отсутствуют для события {}", eventDocument.getGlobalEventId());
        }

        return response;
    }
} 