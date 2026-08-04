package lesson_6_reflection_annotations.utils;

import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.events.StatisticsEvent;
import lesson_6_reflection_annotations.listener.ExecutionListener;
import lesson_6_reflection_annotations.listener.TestListener;

import java.util.function.Consumer;

/**
 * Централизованный менеджер для отправки уведомлений всем зарегистрированным слушателям.
 * Содержит экземпляр {@link ExecutionListener} и предоставляет методы для вызова соответствующих
 * методов слушателей.
 *
 */
public class NotificationManager {
    private final ExecutionListener<TestListener> executionListener = new ExecutionListener<>();

    /**
     * Регистрирует слушателя.
     *
     * @param listener слушатель
     */
    public void registerListener(TestListener listener) {
        executionListener.registerListener(listener);
    }

    /**
     * Удаляет слушателя.
     *
     * @param listener слушатель
     */
    public void removeListener(TestListener listener) {
        executionListener.removeListener(listener);
    }

    /**
     * Возвращает количество зарегистрированных слушателей.
     *
     * @return число слушателей
     */
    public int numberOfListeners() {
        return executionListener.getListeners().size();
    }

    private void notify(Consumer<TestListener> action) {
        for (TestListener listener : executionListener.getListeners()) {
            action.accept(listener);
        }
    }

    /**
     * Уведомляет о старте всех тестов (перед циклом).
     */
    public void notifyBefore() {
        notify(TestListener::beforeTests);
    }

    /**
     * Уведомляет об отсутствии тестовых методов.
     */
    public void notifyOnEmptyTestsList(ServiceEvent event) {
        notify(listener -> listener.onEmptyTestsList(event));
    }

    /**
     * Уведомляет о начале выполнения конкретного теста.
     */
    public void notifyStart(ServiceEvent event) {
        notify(listener -> listener.onTestStart(event));
    }

    /**
     * Уведомляет о провале теста.
     */
    public void notifyFailure(ServiceEvent event) {
        notify(listener -> listener.onTestFailure(event));
    }

    /**
     * Уведомляет об успешном прохождении теста.
     */
    public void notifySuccess(ServiceEvent event) {
        notify(listener -> listener.onTestSuccess(event));
    }

    /**
     * Уведомляет о завершении всех тестов и передаёт статистику.
     */
    public void notifyAfter(StatisticsEvent event) {
        notify(listener -> listener.afterTests(event));
    }
}
