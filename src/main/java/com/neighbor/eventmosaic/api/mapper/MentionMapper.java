package com.neighbor.eventmosaic.api.mapper;

import com.neighbor.eventmosaic.api.document.MentionDocument;
import com.neighbor.eventmosaic.api.dto.details.MentionSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Маппер для преобразования данных из {@link MentionDocument} в {@link MentionSummary} DTO.
 * Используется для формирования списка упоминаний события.
 */
@Mapper(componentModel = "spring")
public interface MentionMapper {

    /**
     * Преобразует один документ {@link MentionDocument} в DTO {@link MentionSummary}.
     *
     * @param mentionDocument Документ упоминания из Elasticsearch.
     * @return {@link MentionSummary} DTO с информацией об упоминании.
     */
    @Mapping(target = "source", source = "mentionSourceName")
    @Mapping(target = "url", source = "mentionIdentifier")
    @Mapping(target = "mentionTime", source = "mentionTimeDate")
    @Mapping(target = "mentionDocTone", source = "mentionDocTone")
    MentionSummary toMentionSummary(MentionDocument mentionDocument);

    /**
     * Преобразует список документов {@link MentionDocument} в список DTO {@link MentionSummary}.
     *
     * @param mentionDocuments Список документов упоминаний из Elasticsearch.
     * @return Список {@link MentionSummary} DTO.
     */
    List<MentionSummary> toMentionSummaryList(List<MentionDocument> mentionDocuments);
} 