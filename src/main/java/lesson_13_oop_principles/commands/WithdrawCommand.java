package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.Denomination;
import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.ui.UI;

import java.util.*;

/**
 * Dynamic-команда снятия банкнот из банкомата.
 * <p>Реализует {@link AmountRequired} - перед выполнением требует установки суммы.
 * Выводит детализацию выданных банкнот по номиналам.</p>
 */

public class WithdrawCommand implements DynamicCommand, AmountRequired {
    private final Atm atm;
    private final UI ui;
    private int amount;

    /**
     * @param atm экземпляр банкомата
     * @param ui  интерфейс пользователя
     */

    public WithdrawCommand(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    /**
     * Делегирует операцию банкомату и выводит список выданных банкнот.
     */

    @Override
    public void execute() {
        Map<Denomination, Integer> withdrawn = atm.withdraw(amount);
        printWithdrawn(withdrawn);
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Выводит список выданных банкнот сгруппировано по номиналам.
     */

    private void printWithdrawn(Map<Denomination, Integer> banknotes) {
        ui.print(Messages.get("funds.withdraw.success.common"));
        banknotes.forEach((key, value) -> {
            ui.print(Messages.get("balance.details",
                    value,
                    key.getValue()));
        });
    }
}
