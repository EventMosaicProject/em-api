package com.neighbor.eventmosaic.api.dto.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * DTO с краткой информацией об упоминании события.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentionSummary {

    /**
     * Название или домен источника упоминания (например, "bbc.co.uk").
     */
    private String source;

    /**
     * URL или уникальный идентификатор статьи-источника.
     */
    private String url; // mentionIdentifier

    /**
     * Время публикации упоминания.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime mentionTime;

    /**
     * Тональность конкретной статьи-источника.
     */
    private Double mentionDocTone;
} 