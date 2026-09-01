package lesson_13_oop_principles.exceptions;

import lesson_13_oop_principles.commands.GlobalCommand;

/**
 * Базовое непроверяемое исключение для всех ошибок банкомата.
 * <p>Обрабатывается централизованно в {@link GlobalCommand}.</p>
 */

public class AtmException extends RuntimeException {

    public AtmException(String message) {
        super(message);
    }
}
