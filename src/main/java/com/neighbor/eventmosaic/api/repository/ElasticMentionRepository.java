package com.neighbor.eventmosaic.api.repository;

import com.neighbor.eventmosaic.api.document.MentionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Репозиторий для доступа к данным об упоминаниях событий GDELT (MentionDocument) в Elasticsearch.
 */
@Repository
public interface ElasticMentionRepository extends ElasticsearchRepository<MentionDocument, String> {

    /**
     * Находит все упоминания для указанного GlobalEventID.
     *
     * @param globalEventId идентификатор события
     * @return список упоминаний
     */
    List<MentionDocument> findByGlobalEventId(Long globalEventId);

    /**
     * Находит упоминания для указанного GlobalEventID в заданном временном диапазоне.
     * Имена полей в методе должны точно соответствовать полям в MentionDocument.
     *
     * @param globalEventId идентификатор события
     * @param startTime     начальное время диапазона
     * @param endTime       конечное время диапазона
     * @return список упоминаний
     */
    List<MentionDocument> findByGlobalEventIdAndMentionTimeDateBetween(Long globalEventId,
                                                                       OffsetDateTime startTime,
                                                                       OffsetDateTime endTime);
} 