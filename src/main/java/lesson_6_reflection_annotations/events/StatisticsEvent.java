package lesson_6_reflection_annotations.events;

import lesson_6_reflection_annotations.Status;

import java.time.Duration;

/**
 * Событие, содержащее итоговую статистику выполнения всех тестов в классе.
 * Передаётся слушателям после завершения всех тестов класса.
 * Содержит общее количество, пройденные и упавшие тесты, общую длительность.
 */
public class StatisticsEvent extends Event {
    private final int totalTests;
    private final int passedTests;
    private final int failedTests;

    private StatisticsEvent(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.status = builder.status;
        this.duration = builder.duration;
        this.totalTests = builder.totalTests;
        this.passedTests = builder.passedTests;
        this.failedTests = builder.failedTests;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    /**
     * Строитель для {@link StatisticsEvent}.
     */
    public static class Builder {
        private final String name;
        private String description;
        private Status status;
        private Duration duration;
        private int totalTests;
        private int passedTests;
        private int failedTests;

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

        public Builder totalTests(int totalTests) {
            this.totalTests = totalTests;
            return this;
        }

        public Builder passedTests(int passedTests) {
            this.passedTests = passedTests;
            return this;
        }

        public Builder failedTests(int failedTests) {
            this.failedTests = failedTests;
            return this;
        }

        public StatisticsEvent build() {
            return new StatisticsEvent(this);
        }
    }
}
