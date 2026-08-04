package lesson_6_reflection_annotations.events;

import lesson_6_reflection_annotations.Status;

import java.time.Duration;

/**
 * Событие, описывающее один тестовый метод (его выполнение).
 * Содержит имя, описание, статус, длительность и возможное исключение.
 * Используется для уведомления слушателей о старте, успехе или провале теста.
 */
public class ServiceEvent extends Event {
    private ServiceEvent(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.status = builder.status;
        this.duration = builder.duration;
        this.throwable = builder.throwable;
    }

    /**
     * Строитель для {@link ServiceEvent}.
     */
    public static class Builder {
        private final String name;
        private String description;
        private Status status;
        private Duration duration;
        private Throwable throwable;

        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public ServiceEvent build() {
            return new ServiceEvent(this);
        }
    }
}
