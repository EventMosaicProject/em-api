package com.neighbor.eventmosaic.api.repository;

import com.neighbor.eventmosaic.api.document.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для доступа к данным о событиях GDELT (EventDocument) в Elasticsearch.
 */
@Repository
public interface ElasticEventRepository extends ElasticsearchRepository<EventDocument, Long> {
} 