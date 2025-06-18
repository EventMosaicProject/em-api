# Сервис отображения событий на карте (em-api)

Микросервис `em-api` отвечает за предоставление REST API для интерактивного отображения событий GDELT на географической карте. Сервис поддерживает кластеризацию событий, фильтрацию по времени и области карты, визуализацию эмоционального тона и получение детальной информации о событиях с их медийным освещением.

## Основной рабочий процесс

1. **Получение запроса событий для карты:**
   * Клиент (фронтенд карты) отправляет GET-запрос к `/api/v1/events` с параметрами фильтрации: временной диапазон (`since`/`until` или `date`), границы видимой области карты (`bbox`) и текущий уровень масштабирования (`zoom`).
   * По умолчанию, если временные параметры не указаны, используется период последних 24 часов.

2. **Формирование запроса к Elasticsearch:**
   * `EventServiceImpl` анализирует параметры и выбирает соответствующие ежедневные индексы Elasticsearch (`gdelt-events-YYYY-MM-DD`).
   * Применяется географический фильтр `geo_bounding_box` по полю координат события для ограничения выборки текущей областью карты.
   * В зависимости от уровня `zoom` определяется стратегия обработки:
     * **Высокий zoom** (> `individual-events-zoom-threshold`): возвращаются отдельные события без кластеризации
     * **Низкий/средний zoom**: применяется серверная кластеризация с помощью агрегации `geotile_grid`

3. **Серверная кластеризация (при необходимости):**
   * Используется агрегация `geotile_grid` с precision, соответствующим уровню масштабирования карты.
   * Для каждого кластера (ячейки сетки) вычисляются подагрегации:
     * `geocentroid` — координаты центра кластера
     * `avg` по полю `avgTone` — средний эмоциональный тон событий в кластере
     * `top_hits` с `size: 1` — получение одного события из кластера (для кластеров из одного события)

4. **Выбор оптимальных координат события:**
   * Для каждого события применяется логика выбора наиболее точных координат:
     * **Приоритет 1:** `ActionGeo` (место действия события), если тип точности > 1 (точнее уровня страны)
     * **Приоритет 2:** `Actor1Geo`, если `ActionGeo` недостаточно точен и `Actor1Geo` имеет тип > 1
     * **Приоритет 3:** `Actor2Geo`, если предыдущие варианты неприменимы
     * **Резервный:** `ActionGeo` даже при низкой точности, если других вариантов нет

5. **Формирование GeoJSON ответа:**
   * Результаты преобразуются в формат GeoJSON FeatureCollection с помощью MapStruct мапперов.
   * Каждая фича содержит:
     * `geometry`: точка с координатами (центроид для кластера или координаты события)
     * `properties`: 
       * `clusterCount` — количество событий (1 для отдельного события, >1 для кластера)
       * `avgTone` — эмоциональный тон для визуализации цветом
       * `eventId` — идентификатор события (только для `clusterCount = 1`)

6. **Получение детальной информации о событии:**
   * При клике на отдельное событие на карте, фронтенд запрашивает `GET /api/v1/events/{eventId}`.
   * `EventServiceImpl` находит событие по `globalEventId` в соответствующем индексе.
   * Выполняется поиск упоминаний события в индексах `gdelt-mentions-*` за период: дата события + `mention-search-days-range` дней.
   * Формируется детальный ответ с основной информацией о событии и списком медийных источников.

7. **Визуализация эмоционального тона:**
   * Поле `avgTone` в ответе используется фронтендом для цветовой индикации событий/кластеров на карте.
   * Отрицательные значения (негативные события) → красные оттенки
   * Положительные значения (позитивные события) → зеленые/синие оттенки
   * Нейтральные значения (около 0) → желтые/белые оттенки

## Обработка ошибок

* **404 Not Found:** Событие с указанным ID не найдено (`ResourceNotFoundException`)
* **400 Bad Request:** Некорректные параметры запроса (неверный формат даты, невалидные значения)
* **500 Internal Server Error:** Ошибки подключения к Elasticsearch, внутренние ошибки обработки
* **Временные ограничения:** Запросы данных старше 30 дней возвращают пустой результат (данные удаляются согласно ILM-политике Elasticsearch)

## Масштабируемость и производительность

* **Ежедневные индексы:** Разделение данных по дням позволяет ограничивать поиск только нужными индексами
* **Географическая фильтрация:** `geo_bounding_box` существенно сокращает объем обрабатываемых данных
* **Адаптивная кластеризация:** Автоматическое переключение между кластеризованным и детальным отображением в зависимости от масштаба
* **Ленивая загрузка:** Данные запрашиваются только для видимой области карты и текущего временного диапазона

## API Endpoints

### `GET /api/v1/events`
Получение событий/кластеров для отображения на карте

**Параметры запроса:**
- `since` (optional): начальная дата диапазона (YYYY-MM-DD)
- `until` (optional): конечная дата диапазона (YYYY-MM-DD) 
- `date` (optional): конкретная дата (YYYY-MM-DD)
- `bbox` (optional): границы области "minLat,minLon,maxLat,maxLon"
- `zoom` (optional): уровень масштабирования карты

**Ответ:** GeoJSON FeatureCollection

### `GET /api/v1/events/{eventId}`
Получение детальной информации о событии

**Параметры:**
- `eventId`: глобальный идентификатор события

**Ответ:** JSON с деталями события и списком упоминаний

## Диаграмма последовательности (клик на кнопку ⟷ развернет схему)

```mermaid
sequenceDiagram
    participant Client as Клиент (Карта)
    participant Controller as EventController
    participant Service as EventServiceImpl
    participant ES as Elasticsearch
    participant Mapper as MapStruct Mappers

    %% Запрос событий для карты
    Client->>Controller: GET /api/v1/events?bbox=...&zoom=10&date=2025-05-19
    Controller->>Service: getEventsForMap(params)
    
    %% Определение стратегии и формирование запроса
    alt zoom <= individual-events-zoom-threshold
        Note over Service: Стратегия: кластеризация
        Service->>Service: Формирование запроса с geotile_grid
        Service->>ES: Поиск с агрегацией в gdelt-events-2025-05-19
        Note over ES: geo_bounding_box + geotile_grid<br/>+ geocentroid + avg(avgTone) + top_hits
        ES-->>Service: Агрегированные результаты (кластеры)
        
        loop Для каждого кластера
            Service->>Service: Определение координат (центроид)
            alt docCount == 1
                Service->>Mapper: fromSingleEventClusterAggregation()
                Note over Mapper: eventId из top_hits,<br/>clusterCount=1
            else docCount > 1
                Service->>Mapper: fromClusterAggregation()
                Note over Mapper: eventId=null,<br/>clusterCount=docCount
            end
            Mapper-->>Service: GeoJsonProperties
        end
        
    else zoom > individual-events-zoom-threshold
        Note over Service: Стратегия: отдельные события
        Service->>Service: Формирование обычного поиска
        Service->>ES: Поиск в gdelt-events-2025-05-19
        Note over ES: geo_bounding_box + limit + pagination
        ES-->>Service: Список EventDocument
        
        loop Для каждого события
            Service->>Service: Выбор координат (ActionGeo > Actor1Geo > Actor2Geo)
            Service->>Mapper: fromEventDocumentForMap()
            Note over Mapper: eventId=globalEventId,<br/>clusterCount=1
            Mapper-->>Service: GeoJsonProperties
        end
    end
    
    Service->>Service: Сборка GeoJSON FeatureCollection
    Service-->>Controller: GeoJsonFeatureCollection
    Controller-->>Client: JSON ответ
    
    %% Запрос деталей события (по клику)
    Client->>Controller: GET /api/v1/events/123456789
    Controller->>Service: getEventDetails(eventId)
    
    Service->>ES: Поиск события по globalEventId
    ES-->>Service: EventDocument
    
    Service->>Service: Определение диапазона для поиска упоминаний
    Note over Service: eventDate + mention-search-days-range дней
    
    Service->>ES: Поиск упоминаний в gdelt-mentions-*
    Note over ES: globalEventId + временной диапазон
    ES-->>Service: List<MentionDocument>
    
    Service->>Mapper: EventDetailsMapper.toDetailsResponse()
    Note over Mapper: Формирование title, location, actors
    Mapper-->>Service: EventDetailsResponse (без mentions)
    
    Service->>Mapper: MentionMapper.toMentionSummaryList()
    Mapper-->>Service: List<MentionSummary>
    
    Service->>Service: Установка mentions в EventDetailsResponse
    Service-->>Controller: EventDetailsResponse
    Controller-->>Client: JSON ответ с деталями и источниками
    
    %% Обработка ошибок
    alt Событие не найдено
        Service->>Service: throw ResourceNotFoundException
        Controller->>Controller: GlobalExceptionHandler
        Controller-->>Client: 404 Not Found
    else Некорректные параметры
        Controller->>Controller: Валидация @Valid
        Controller-->>Client: 400 Bad Request
    else Ошибка Elasticsearch  
        Service->>Service: Логирование ошибки
        Controller->>Controller: GlobalExceptionHandler
        Controller-->>Client: 500 Internal Server Error
    end
```