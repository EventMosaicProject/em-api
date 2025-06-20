package com.neighbor.eventmosaic.api.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Конфигурационные свойства для API.
 * Загружаются из application.yml с префиксом "api.config".
 */
@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "api.config")
public class ApiConfigProperties {

    /**
     * Уровень масштабирования (zoom), выше которого кластеризация отключается
     * и возвращаются отдельные события.
     * Максимальный zoom может варьироваться в зависимости от картографического провайдера
     */
    @Min(1)
    @Max(25)
    private int individualEventsZoomThreshold = 10;

    /**
     * Максимальное количество дней назад, за которые можно запросить упоминания для события.
     * Не больше общего срока хранения данных.
     */
    @Min(0)
    @Max(30)
    private int mentionSearchDaysRange = 2; // День события + 2 следующих дня

    /**
     * Период по умолчанию для запроса событий (в часах), если не указаны since/until.
     */
    @Min(1)
    private int defaultEventWindowHours = 24;

    /**
     * Максимальное количество кластеров в одном ответе при кластеризации.
     */
    @Min(10)
    @Max(10000)
    private int maxClustersPerRequest = 1000;

    /**
     * Максимальное количество отдельных событий в одном ответе.
     */
    @Min(10)
    @Max(10000)
    private int maxIndividualEventsPerRequest = 500;

    /**
     * Precision по умолчанию для geotile_grid агрегации.
     */
    @Min(1)
    @Max(15)
    private int defaultGeotilePrecision = 5;

    /**
     * Максимальный precision для geotile_grid агрегации.
     */
    @Min(5)
    @Max(29)
    private int maxGeotilePrecision = 18;
} 