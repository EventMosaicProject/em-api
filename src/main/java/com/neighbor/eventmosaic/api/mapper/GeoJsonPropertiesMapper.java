package com.neighbor.eventmosaic.api.mapper;

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
     * Создает {@link GeoJsonProperties} для одиночного события.
     *
     * @param eventId ID события
     * @param avgTone тон события
     * @return {@link GeoJsonProperties} для одиночного события
     */
    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "avgTone", source = "avgTone")
    @Mapping(target = "clusterCount", constant = "1")
    GeoJsonProperties toSingleEventProperties(Long eventId, Double avgTone);

} 