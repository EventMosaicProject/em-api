# Используем официальный образ OpenJDK 21 для сборки приложения
FROM openjdk:21-jdk-slim AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Gradle wrapper и файлы конфигурации для кеширования зависимостей
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Копируем исходный код приложения
COPY src src

# Делаем gradlew исполняемым и собираем jar-файл приложения
RUN chmod +x gradlew && ./gradlew bootJar

# Второй этап: формируем финальный образ для запуска приложения
FROM openjdk:21-jdk-slim
WORKDIR /app

# Копируем собранный jar-файл из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8084

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]