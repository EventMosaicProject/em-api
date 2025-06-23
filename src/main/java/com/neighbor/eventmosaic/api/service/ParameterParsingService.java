package com.neighbor.eventmosaic.api.service;

import com.neighbor.eventmosaic.api.exception.InvalidApiParameterException;

import java.time.OffsetDateTime;

/**
 * Сервис для парсинга параметров запроса.
 */
public interface ParameterParsingService {

    /**
     * Парсит строку bounding box в массив координат.
     * Формат: "minLat,minLon,maxLat,maxLon"
     *
     * @param bboxStr строка с координатами
     * @return массив [minLat, minLon, maxLat, maxLon]
     * @throws InvalidApiParameterException если формат неверный
     */
    double[] parseBoundingBox(String bboxStr);

    /**
     * Создает временной диапазон для запроса.
     *
     * @param since              начальная дата (может быть null)
     * @param until              конечная дата (может быть null)
     * @param defaultWindowHours окно по умолчанию в часах
     * @return массив [startDate, endDate]
     */
    OffsetDateTime[] createDateRange(OffsetDateTime since,
                                     OffsetDateTime until,
                                     int defaultWindowHours);
} 