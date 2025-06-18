package com.neighbor.eventmosaic.api.dto.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO для ответа с детальной информацией о событии.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDetailsResponse {

    /**
     * Глобальный идентификатор события.
     */
    private Long eventId;

    /**
     * Необязательный заголовок/описание события (может генерироваться).
     */
    private String title;

    /**
     * Дата и время события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime eventDate;

    /**
     * Местоположение события (например, "Paris, France").
     */
    private String location;

    /**
     * Список акторов, участвующих в событии (например, ["Government", "Protesters"]).
     */
    private List<String> actors;

    /**
     * Средний тон события.
     */
    private Double avgTone;

    /**
     * Общее количество упоминаний события.
     */
    private Integer mentionsCount;

    /**
     * Список упоминаний события.
     */
    private List<MentionSummary> mentions;
} 