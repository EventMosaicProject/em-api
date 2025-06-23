package com.neighbor.eventmosaic.api.repository;

import com.neighbor.eventmosaic.api.document.EventDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

/**
 * Репозиторий для доступа к данным о событиях GDELT (EventDocument) в Elasticsearch.
 */
@Repository
public interface ElasticEventRepository extends ElasticsearchRepository<EventDocument, Long> {

    /**
     * Находит события в указанном временном диапазоне.
     *
     * @param startDate начальная дата
     * @param endDate   конечная дата
     * @param pageable  параметры пагинации
     * @return страница событий с пагинацией
     */
    Page<EventDocument> findByEventDateBetween(OffsetDateTime startDate,
                                               OffsetDateTime endDate,
                                               Pageable pageable);
} 