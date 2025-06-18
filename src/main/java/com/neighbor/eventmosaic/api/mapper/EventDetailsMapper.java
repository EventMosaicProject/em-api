package com.neighbor.eventmosaic.api.mapper;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Маппер для преобразования данных из {@link EventDocument} в {@link EventDetailsResponse}.
 * Отвечает за формирование детального ответа о событии.
 */
@Mapper(componentModel = "spring")
public interface EventDetailsMapper {

    String N_A = "N/A";

    /**
     * Преобразует {@link EventDocument} в {@link EventDetailsResponse}.
     *
     * @param eventDocument Документ события из Elasticsearch.
     * @return {@link EventDetailsResponse} DTO с детальной информацией о событии.
     * Упоминания (mentions) в этом методе не заполняются, предполагается их установка отдельно.
     */
    @Mapping(target = "title", source = "eventDocument", qualifiedByName = "mapTitleFromEventDocument")
    @Mapping(target = "location", source = "actionGeoFullName", qualifiedByName = "mapLocationString")
    @Mapping(target = "actors", source = "eventDocument", qualifiedByName = "mapActorsListFromEventDocument")
    @Mapping(target = "mentions", ignore = true) // Упоминания (mentions) должны быть установлены отдельно в сервисе
    @Mapping(target = "mentionsCount", source = "numMentions")
    @Mapping(target = "eventId", source = "globalEventId")
    @Mapping(target = "eventDate", source = "eventDate")
    @Mapping(target = "avgTone", source = "avgTone")
    EventDetailsResponse toDetailsResponse(EventDocument eventDocument);

    /**
     * Формирует заголовок события на основе его CAMEO кода.
     *
     * @param eventDocument Документ события.
     * @return Строка с заголовком или "N/A", если код события отсутствует.
     */
    @Named("mapTitleFromEventDocument")
    default String mapTitleFromEventDocument(EventDocument eventDocument) {
        if (eventDocument == null || !StringUtils.hasText(eventDocument.getEventCode())) {
            return N_A;
        }
        return String.format("Событие CAMEO: %s", eventDocument.getEventCode());
    }

    /**
     * Возвращает полное имя местоположения действия (ActionGeoFullName).
     *
     * @param fullName Строка с полным именем местоположения.
     * @return Исходная строка или "N/A", если строка пуста или null.
     */
    @Named("mapLocationString")
    default String mapLocationString(String fullName) {
        return Optional.ofNullable(fullName).filter(StringUtils::hasText).orElse(N_A);
    }

    /**
     * Формирует список акторов события.
     *
     * @param eventDocument Документ события.
     * @return Список имен акторов. Если акторы отсутствуют, возвращает список с одним элементом "N/A".
     */
    @Named("mapActorsListFromEventDocument")
    default List<String> mapActorsListFromEventDocument(EventDocument eventDocument) {
        List<String> actors = new ArrayList<>();
        if (eventDocument == null) {
            return List.of(N_A);
        }
        if (StringUtils.hasText(eventDocument.getActor1Name())) {
            actors.add(eventDocument.getActor1Name());
        }
        if (StringUtils.hasText(eventDocument.getActor2Name())) {
            actors.add(eventDocument.getActor2Name());
        }
        return actors.isEmpty()
                ? List.of(N_A)
                : actors;
    }
} 