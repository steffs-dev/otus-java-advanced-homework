package lesson_6_reflection_annotations.listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Хранилище слушателей с потокобезопасным доступом.
 * Использует {@link CopyOnWriteArrayList} для безопасного добавления/удаления слушателей
 * во время выполнения тестов в многопоточной среде.
 *
 * @param <T> тип слушателя, должен наследовать {@link TestListener}
 */
public class ExecutionListener<T extends TestListener> {
    private final List<T> listeners;

    public ExecutionListener() {
        listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Регистрирует нового слушателя.
     *
     * @param listener слушатель
     */
    public void registerListener(T listener) {
        listeners.add(listener);
    }

    /**
     * Удаляет ранее зарегистрированного слушателя.
     *
     * @param listener слушатель
     */
    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    /**
     * Возвращает копию списка слушателей.
     *
     * @return список слушателей
     */
    public List<T> getListeners() {
        return listeners;
    }
}
