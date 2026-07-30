package lesson_6_reflection_annotations.events;

import lesson_6_reflection_annotations.Status;

import java.time.Duration;

/**
 * Базовый абстрактный класс для всех событий фреймворка.
 * Содержит общие поля: имя, описание, статус, длительность, исключение (если было).
 * Используется для передачи информации между компонентами и слушателями.
 */
public abstract class Event {
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected Throwable throwable;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
