package lesson_13_oop_principles.exceptions;

/**
 * Исключение, возникающее при невалидном пользовательском вводе
 * (пустая строка, не число, превышение {@link Integer#MAX_VALUE},
 * неизвестный номинал и т.п.).
 */

public class InvalidInputException extends AtmException {
    public InvalidInputException(String message) {
        super(message);
    }
}
