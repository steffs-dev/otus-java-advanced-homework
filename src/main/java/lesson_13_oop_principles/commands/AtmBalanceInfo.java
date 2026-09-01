package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.entities.MoneyBox;
import lesson_13_oop_principles.ui.UI;

import java.util.stream.Collectors;

/**
 * Singleton-команда, выводящая текущий баланс банкомата.
 * <p>Группирует боксы по номиналу, суммирует количество банкнот
 * и выводит общую сумму.</p>
 */

public class AtmBalanceInfo implements SingletonCommand {
    private final Atm atm;
    private final UI ui;

    /**
     * @param atm экземпляр банкомата
     * @param ui  интерфейс пользователя
     */

    public AtmBalanceInfo(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    @Override
    public void execute() {
        printBalance();
    }

    /**
     * Выводит детализацию баланса по номиналам и итоговую сумму.
     */

    private void printBalance() {
        ui.print(Messages.get("atm.balance.header"));
        atm.getMoneyBoxes().stream().collect(Collectors.groupingBy(MoneyBox::getDenomination,
                        Collectors.summingInt(MoneyBox::getNumOfBanknotes)))
                .forEach((denomination, numOfBanknotes) -> {
                    ui.print(Messages.get("balance.details",
                            numOfBanknotes,
                            denomination.getValue()));
                });
        long total = atm.getMoneyBoxes().stream().mapToLong(box ->
                (long) box.getDenomination().getValue() * box.getNumOfBanknotes()
        ).sum();
        ui.printf(Messages.get("atm.balance.total", total));
    }
}
