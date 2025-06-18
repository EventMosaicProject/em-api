package com.neighbor.eventmosaic.api.controller;

import com.neighbor.eventmosaic.api.dto.EventMapQueryParameters;
import com.neighbor.eventmosaic.api.dto.details.EventDetailsResponse;
import com.neighbor.eventmosaic.api.dto.geojson.GeoJsonFeatureCollection;
import com.neighbor.eventmosaic.api.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST контроллер для получения информации о событиях GDELT.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
@Tag(name = "События GDELT", description = "API для получения и отображения событий GDELT на карте")
public class EventController {

    private final EventService eventService;

    /**
     * Получает события или кластеры событий для отображения на карте.
     *
     * @param params Параметры запроса (диапазон дат, bbox, zoom).
     * @return GeoJSON FeatureCollection с событиями/кластерами.
     */
    @Operation(
            summary = "Получить события/кластеры для карты",
            description = "Возвращает GeoJSON FeatureCollection с событиями или кластерами событий в указанной области и за указанный период.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GeoJsonFeatureCollection.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    @GetMapping
    public GeoJsonFeatureCollection getEventsForMap(
            @Parameter(description = "Параметры фильтрации событий для карты") @Valid EventMapQueryParameters params) {
        return eventService.getEventsForMap(params);
    }

    /**
     * Получает детальную информацию о конкретном событии.
     *
     * @param eventId Глобальный идентификатор события.
     * @return Детальная информация о событии, включая упоминания.
     */
    @Operation(
            summary = "Получить детализацию события",
            description = "Возвращает подробную информацию о конкретном событии по его ID, включая список упоминаний.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EventDetailsResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    @GetMapping("/{eventId}")
    public EventDetailsResponse getEventDetails(
            @Parameter(description = "Глобальный идентификатор события (GlobalEventID)", required = true, example = "1234567890")
            @PathVariable Long eventId) {
        return eventService.getEventDetails(eventId);
    }
} 