package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.ui.UI;

/**
 * Singleton-команда, выводящая главное меню с описанием доступных действий.
 */

public class MenuCommand implements SingletonCommand {
    private final UI ui;

    public MenuCommand(UI ui) {
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.printf(Messages.get("menu.text",
                CommandNames.ATM_BALANCE.name(),
                CommandNames.DEPOSIT.name(),
                CommandNames.WITHDRAW.name(),
                CommandNames.MENU.name(),
                CommandNames.EXIT.name()
        ));
    }
}
