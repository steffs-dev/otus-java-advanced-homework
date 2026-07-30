package lesson_6_reflection_annotations.utils;

import lesson_6_reflection_annotations.Status;
import lesson_6_reflection_annotations.events.Event;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Потокобезопасный сборщик статистики выполнения тестов.
 * Хранит количество тестов по статусам (SUCCESS, FAILURE) и общую длительность.
 * Использует {@link ConcurrentHashMap} и {@link AtomicInteger} для корректной работы
 * при параллельном выполнении тестов.
 */
public class StatisticsConsolidator {
    private final Map<Status, AtomicInteger> map = new ConcurrentHashMap<>();
    private Duration totalDuration = Duration.ZERO;

    /**
     * Добавляет результат одного теста (событие).
     *
     * @param event событие с информацией о статусе и длительности
     */
    public synchronized void add(Event event) {
        map.computeIfAbsent(event.getStatus(), s -> new AtomicInteger()).incrementAndGet();

        if (event.getDuration() != null)
            totalDuration = totalDuration.plus(event.getDuration());
    }

    /**
     * Возвращает количество тестов с указанным статусом.
     *
     * @param status статус (SUCCESS или FAILURE)
     * @return число тестов
     */
    public int getCount(Status status) {
        return map.getOrDefault(status, new AtomicInteger(0)).get();
    }

    /**
     * Возвращает общую длительность всех успешных тестов (суммируется только для SUCCESS).
     *
     * @return общая длительность
     */
    public Duration getTotalDuration() {
        return totalDuration;
    }

    /**
     * Возвращает общее количество выполненных тестов (сумма SUCCESS + FAILURE).
     *
     * @return общее количество
     */
    public int getTotalTests() {
        return map.values().stream().filter(Objects::nonNull).mapToInt(AtomicInteger::get).sum();
    }
}
