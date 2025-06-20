package com.neighbor.eventmosaic.api.service.impl;

import com.neighbor.eventmosaic.api.exception.InvalidApiParameterException;
import com.neighbor.eventmosaic.api.service.ParameterParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;

/**
 * Реализация сервиса для парсинга параметров запроса.
 * Этот сервис занимается исключительно преобразованием данных,
 * доверяя, что валидация была выполнена на предыдущем этапе (например, с помощью аннотаций).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParameterParsingServiceImpl implements ParameterParsingService {

    @Override
    public double[] parseBoundingBox(String bboxStr) {
        if (!StringUtils.hasText(bboxStr)) {
            return null;
        }

        log.debug("Парсинг bounding box: {}", bboxStr);
        String[] components = bboxStr.trim().split(",");

        try {
            return new double[]{
                    Double.parseDouble(components[0].trim()),
                    Double.parseDouble(components[1].trim()),
                    Double.parseDouble(components[2].trim()),
                    Double.parseDouble(components[3].trim())
            };
        } catch (NumberFormatException e) {
            // Эта ошибка должна быть отловлена валидатором,
            // но на случай прямого вызова сервиса оставляем исключение для надежности.
            throw new InvalidApiParameterException("Неверный числовой формат в bounding box: " + bboxStr, e);
        }
    }

    @Override
    public OffsetDateTime[] createDateRange(OffsetDateTime since,
                                            OffsetDateTime until,
                                            int defaultWindowHours) {
        boolean hasSince = since != null;
        boolean hasUntil = until != null;

        OffsetDateTime start;
        OffsetDateTime end;

        if (hasSince && hasUntil) {
            log.debug("Используем заданный диапазон: {} - {}", since, until);
            start = since;
            end = until;
        } else if (hasSince) {
            start = since;
            end = since.plusHours(defaultWindowHours);
            log.debug("Начальная дата задана, окно {} часов: {} - {}", defaultWindowHours, start, end);
        } else if (hasUntil) {
            end = until;
            start = end.minusHours(defaultWindowHours);
            log.debug("Конечная дата задана, окно {} часов: {} - {}", defaultWindowHours, start, end);
        } else {
            // Если ни одна дата не задана, используем окно по умолчанию
            end = OffsetDateTime.now();
            start = end.minusHours(defaultWindowHours);
            log.debug("Используем окно по умолчанию {} часов: {} - {}", defaultWindowHours, start, end);
        }

        return new OffsetDateTime[]{start, end};
    }
} 