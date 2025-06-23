package com.neighbor.eventmosaic.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Валидатор для строки bounding box (bbox).
 * <p>
 * Реализует логику проверки, указанную в аннотации {@link ValidBoundingBox}.
 */
public class BoundingBoxValidator implements ConstraintValidator<ValidBoundingBox, String> {

    private static final int BBOX_COMPONENTS_COUNT = 4;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    /**
     * Проверяет, является ли строка bbox валидным bounding box.
     *
     * @param bboxStr строка с координатами bounding box
     * @param context контекст валидатора
     * @return true, если строка является валидным bounding box, иначе false
     */
    @Override
    public boolean isValid(String bboxStr, ConstraintValidatorContext context) {
        // null значения считаются валидными, чтобы можно было использовать @NotNull отдельно
        if (!StringUtils.hasText(bboxStr)) {
            return true;
        }

        String[] components = bboxStr.trim().split(",");
        if (components.length != BBOX_COMPONENTS_COUNT) {
            setValidationMessage(context, "Неверное количество компонентов. Ожидается 4, получено " + components.length);
            return false;
        }

        try {
            double minLat = Double.parseDouble(components[0].trim());
            double minLon = Double.parseDouble(components[1].trim());
            double maxLat = Double.parseDouble(components[2].trim());
            double maxLon = Double.parseDouble(components[3].trim());

            return isLatitudeValid(minLat, context, "minLat")
                    && isLatitudeValid(maxLat, context, "maxLat")
                    && isLongitudeValid(minLon, context, "minLon")
                    && isLongitudeValid(maxLon, context, "maxLon")
                    && areBoundsLogicallyValid(minLat, minLon, maxLat, maxLon, context);

        } catch (NumberFormatException e) {
            setValidationMessage(context, "Один или несколько компонентов не являются валидными числами.");
            return false;
        }
    }

    /**
     * Проверяет, находится ли широта в допустимом диапазоне.
     *
     * @param lat       широта
     * @param context   контекст валидатора
     * @param paramName имя параметра
     * @return true, если широта в допустимом диапазоне, иначе false
     */
    private boolean isLatitudeValid(double lat,
                                    ConstraintValidatorContext context,
                                    String paramName) {
        if (lat < MIN_LATITUDE || lat > MAX_LATITUDE) {
            setValidationMessage(context, String.format(
                    "%s должна быть в диапазоне [%.1f, %.1f], получено: %.2f", paramName, MIN_LATITUDE, MAX_LATITUDE, lat));
            return false;
        }
        return true;
    }

    /**
     * Проверяет, находится ли долгота в допустимом диапазоне.
     *
     * @param lon       долгота
     * @param context   контекст валидатора
     * @param paramName имя параметра
     * @return true, если долгота в допустимом диапазоне, иначе false
     */
    private boolean isLongitudeValid(double lon,
                                     ConstraintValidatorContext context,
                                     String paramName) {
        if (lon < MIN_LONGITUDE || lon > MAX_LONGITUDE) {
            setValidationMessage(context, String.format(
                    "%s должна быть в диапазоне [%.1f, %.1f], получено: %.2f", paramName, MIN_LONGITUDE, MAX_LONGITUDE, lon));
            return false;
        }
        return true;
    }

    /**
     * Проверяет, являются ли границы логически валидными.
     *
     * @param minLat  минимальная широта
     * @param minLon  минимальная долгота
     * @param maxLat  максимальная широта
     * @param maxLon  максимальная долгота
     * @param context контекст валидатора
     * @return true, если границы логически валидны, иначе false
     */
    private boolean areBoundsLogicallyValid(double minLat,
                                            double minLon,
                                            double maxLat,
                                            double maxLon,
                                            ConstraintValidatorContext context) {
        if (minLat >= maxLat) {
            setValidationMessage(context, "minLat должна быть меньше maxLat.");
            return false;
        }
        if (minLon >= maxLon) {
            setValidationMessage(context, "minLon должна быть меньше maxLon.");
            return false;
        }
        return true;
    }

    /**
     * Устанавливает кастомное сообщение об ошибке валидации.
     *
     * @param context контекст валидатора
     * @param message сообщение для отображения
     */
    private void setValidationMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
} 