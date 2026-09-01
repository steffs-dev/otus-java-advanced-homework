package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Denomination;
import lesson_13_oop_principles.states.AmountInputState;

/**
 * Маркерный интерфейс для команд, требующих ввода номинала и количества банкнот
 * (например, {@code DEPOSIT}).
 * <p>Используется в {@link AmountInputState} для определения типа ввода.</p>
 */

public interface DenominationAndBanknotesRequired {

    /**
     * Устанавливает номинал и количество банкнот.
     *
     * @param denomination номинал банкнот
     * @param banknotes    количество банкнот (положительное)
     */

    void setDenominationAndBanknotes(Denomination denomination, int banknotes);
}
