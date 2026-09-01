package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.states.AmountInputState;

/**
 * Маркерный интерфейс для команд, требующих ввода суммы (например, {@code WITHDRAW}).
 * <p>Используется в {@link AmountInputState} для определения типа ввода.</p>
 */

public interface AmountRequired {

    /**
     * Устанавливает сумму операции.
     *
     * @param amount положительная сумма
     */

    void setAmount(int amount);

}
