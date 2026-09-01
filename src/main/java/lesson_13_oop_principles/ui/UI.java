package lesson_13_oop_principles.ui;

/**
 * Интерфейс слоя представления (абстракция над UI).
 * <p>Позволяет подменить реализацию, не затрагивая бизнес-логику </p>
 */

public interface UI {

    /**
     * Выводит строку с переносом.
     */

    void print(String message);

    /**
     * Выводит отформатированную строку (синтаксис {@link java.util.Formatter}).
     */

    void printf(String message, Object... args);

    /**
     * Читает строку из пользовательского ввода.
     */

    String readLine();

    /**
     * Освобождает ресурсы (закрывает потоки ввода).
     */

    void close();
}
