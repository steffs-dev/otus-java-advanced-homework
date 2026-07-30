package lesson_6_reflection_annotations.listener;

import lesson_6_reflection_annotations.annotations.Test;
import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.events.StatisticsEvent;

/**
 * Интерфейс для слушателей событий тестирования.
 * Реализации могут реагировать на старт, завершение, успех/провал тестов и вывод статистики.
 * Примеры реализации: {@link ConsoleListener} (вывод в консоль), {@link LogListener} (запись в файл).
 */
public interface TestListener {
    /**
     * Вызывается перед выполнением всех тестов в классе.
     */
    void beforeTests();

    /**
     * Вызывается, если в классе не найдено ни одного тестового метода с {@link Test @Test}.
     *
     * @param event событие с именем класса и описанием
     */
    void onEmptyTestsList(ServiceEvent event);

    /**
     * Вызывается перед началом выполнения каждого теста.
     *
     * @param event событие с информацией о тесте
     */
    void onTestStart(ServiceEvent event);

    /**
     * Вызывается при успешном прохождении теста.
     *
     * @param event событие с длительностью и статусом SUCCESS
     */
    void onTestSuccess(ServiceEvent event);

    /**
     * Вызывается при провале теста (исключение).
     *
     * @param event событие с исключением и статусом FAILURE
     */
    void onTestFailure(ServiceEvent event);

    /**
     * Вызывается после завершения всех тестов класса.
     *
     * @param event событие со статистикой
     */
    void afterTests(StatisticsEvent event);
}
