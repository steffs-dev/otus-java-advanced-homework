package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.Denomination;
import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.ui.UI;

/**
 * Dynamic-команда внесения банкнот в банкомат.
 * <p>Реализует {@link DenominationAndBanknotesRequired} — перед выполнением
 * требует установки номинала и количества банкнот.</p>
 */

public class DepositCommand implements DynamicCommand, DenominationAndBanknotesRequired {
    private final Atm atm;
    private final UI ui;
    private int banknotes;
    private Denomination denomination;

    /**
     * @param atm экземпляр банкомата
     * @param ui  интерфейс пользователя
     */

    public DepositCommand(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    /**
     * Делегирует операцию банкомату и выводит сообщение об успехе.
     */

    @Override
    public void execute() {
        atm.deposit(denomination, banknotes);
        ui.printf(Messages.get("funds.deposit.success",
                banknotes, denomination.getValue()));
    }

    @Override
    public void setDenominationAndBanknotes(Denomination denomination, int banknotes) {
        this.denomination = denomination;
        this.banknotes = banknotes;
    }

}
