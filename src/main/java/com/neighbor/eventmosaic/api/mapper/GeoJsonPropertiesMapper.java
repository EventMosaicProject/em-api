package com.neighbor.eventmosaic.api.mapper;

import com.neighbor.eventmosaic.api.document.EventDocument;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования данных в {@link GeoJsonProperties} для использования в GeoJSON ответах.
 * Отвечает за формирование свойств для отдельных событий и для кластеров.
 */
@Mapper(componentModel = "spring")
public interface GeoJsonPropertiesMapper {

    /**
     * Создает {@link GeoJsonProperties} из {@link EventDocument} для отображения одиночного события на карте.
     *
     * @param eventDocument Документ события.
     * @return {@link GeoJsonProperties} для одиночного события.
     */
    @Mapping(target = "eventId", source = "globalEventId")
    @Mapping(target = "clusterCount", constant = "1")
    @Mapping(target = "avgTone", source = "avgTone")
    GeoJsonProperties fromEventDocumentForMap(EventDocument eventDocument);

    /**
     * Создает {@link GeoJsonProperties} для кластера событий на основе агрегированных данных.
     * Используется, когда ID конкретного события для кластера не важен или кластер содержит много событий.
     *
     * @param docCount Количество документов (событий) в кластере.
     * @param avgTone  Средний тон для событий в кластере.
     * @return {@link GeoJsonProperties} для кластера.
     */
    @Mapping(target = "eventId", expression = "java(null)")
    @Mapping(target = "avgTone", source = "avgTone")
    @Mapping(target = "clusterCount", source = "docCount")
    GeoJsonProperties fromClusterAggregation(long docCount,
                                             Double avgTone);

    /**
     * Создает {@link GeoJsonProperties} для кластера, который представляет одно событие.
     * Это может произойти, если в результате агрегации бакет содержит только одно событие.
     *
     * @param docCount Количество документов (должно быть 1).
     * @param avgTone  Средний тон (тон единственного события).
     * @param eventId  Идентификатор единственного события в кластере.
     * @return {@link GeoJsonProperties} для кластера из одного события.
     */
    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "avgTone", source = "avgTone")
    @Mapping(target = "clusterCount", source = "docCount")
    GeoJsonProperties fromSingleEventClusterAggregation(long docCount,
                                                        Double avgTone,
                                                        Long eventId);

} 