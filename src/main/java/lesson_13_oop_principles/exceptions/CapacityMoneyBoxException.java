package lesson_13_oop_principles.exceptions;

/**
 * Исключение, возникающее при попытке разместить в боксе больше банкнот,
 * чем позволяет её вместимость.
 */

public class CapacityMoneyBoxException extends AtmException {
    private static final String message = "Number of banknotes must be between 0 and ";

    /**
     * @param capacity доступная вместимость бокса
     */

    public CapacityMoneyBoxException(int capacity) {
        super(message + capacity);
    }

}
