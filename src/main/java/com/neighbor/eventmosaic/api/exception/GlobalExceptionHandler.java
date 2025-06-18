package com.neighbor.eventmosaic.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для контроллеров.
 * Обеспечивает единообразную обработку ошибок и формирование JSON ответов.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link ResourceNotFoundException}.
     * Возвращает HTTP 404 Not Found.
     *
     * @param ex      исключение
     * @param request веб-запрос
     * @return ResponseEntity с деталями ошибки
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                  WebRequest request) {
        log.warn("Ресурс не найден: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Обрабатывает исключение {@link GeoPointBadRequestException}.
     * Возвращает HTTP 400 Bad Request.
     *
     * @param ex      исключение
     * @param request веб-запрос
     * @return ResponseEntity с деталями ошибки
     */
    @ExceptionHandler(GeoPointBadRequestException.class)
    public ResponseEntity<Object> handleGeoPointBadRequestException(GeoPointBadRequestException ex,
                                                                    WebRequest request) {
        log.warn("Некорректные данные географической точки: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Обрабатывает исключения валидации аргументов метода (например, @Valid в DTO).
     * Возвращает HTTP 400 Bad Request.
     *
     * @param ex      исключение
     * @param request веб-запрос
     * @return ResponseEntity с деталями ошибки
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               WebRequest request) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String message = "Ошибка валидации: " + errors;
        log.warn("Ошибка валидации: {} для пути {}", errors, getPath(request));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Обрабатывает ошибки преобразования типов параметров (например, некорректный формат даты).
     * Возвращает HTTP 400 Bad Request.
     *
     * @param ex      исключение
     * @param request веб-запрос
     * @return ResponseEntity с деталями ошибки
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String message = String.format("Некорректное значение '%s' для параметра '%s'", ex.getValue(), ex.getName());
        log.warn("Ошибка типа параметра: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Обрабатывает все остальные исключения.
     * Возвращает HTTP 500 Internal Server Error.
     *
     * @param ex      исключение
     * @param request веб-запрос
     * @return ResponseEntity с деталями ошибки
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex,
                                                           WebRequest request) {
        log.error("Внутренняя ошибка сервера: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла внутренняя ошибка сервера.", request);
    }

    /**
     * Создает стандартизированный ответ об ошибке в формате JSON.
     *
     * @param status  HTTP статус ответа
     * @param message детальное сообщение об ошибке
     * @param request веб-запрос для получения пути
     * @return ResponseEntity с телом ошибки
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status,
                                                      String message,
                                                      WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", getPath(request));
        return new ResponseEntity<>(body, status);
    }

    /**
     * Извлекает путь из веб-запроса.
     *
     * @param request веб-запрос
     * @return путь запроса
     */
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}