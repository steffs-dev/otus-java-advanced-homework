package lesson_13_oop_principles.commands;

/**
 * Базовый интерфейс для всех команд банкомата.
 * <p>Следует паттерну <b>Command</b>: инкапсулирует запрос как объект.</p>
 */

public interface Command {

    /**
     * Выполняет действие команды.
     */

    void execute();
}
