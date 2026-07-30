package lesson_6_reflection_annotations;

/**
 * Перечисление статусов, которые может принимать событие.
 * Используется в {@link lesson_6_reflection_annotations.events.Event} и его наследниках.
 */
public enum Status {
    /**
     * Старт выполнения теста.
     */
    START,

    /**
     * Успешное завершение.
     */
    SUCCESS,

    /**
     * Провал (исключение).
     */
    FAILURE,

    /**
     * Стадия после завершения всех тестов.
     */
    AFTER
}
