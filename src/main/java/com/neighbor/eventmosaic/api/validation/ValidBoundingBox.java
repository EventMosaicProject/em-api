package com.neighbor.eventmosaic.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации параметра bounding box (bbox).
 * <p>
 * Проверяет, что строка:
 * Не пуста (если не null).
 * Содержит 4 числовых компонента, разделенных запятыми.
 * Компоненты соответствуют корректным диапазонам широты [-90, 90] и долготы [-180, 180].
 * minLat < maxLat и minLon < maxLon.
 *
 *
 * @see BoundingBoxValidator
 */
@Documented
@Constraint(validatedBy = BoundingBoxValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBoundingBox {

    String message() default "Неверный формат bounding box";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} 