package lesson_13_oop_principles.states;

import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.SessionManager;
import lesson_13_oop_principles.ui.UI;
import lesson_13_oop_principles.commands.*;

/**
 * Состояние показа баланса после успешной операции DEPOSIT.
 * <p>Автоматически выполняет команду {@code ATM_BALANCE}
 * и возвращает пользователя в {@link MenuState}.</p>
 */

public class BalanceState implements AtmStates {
    private final Atm atm;
    private final UI ui;

    public BalanceState(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    @Override
    public void execute(SessionManager sessionManager) {
        handleCommands(sessionManager, CommandNames.ATM_BALANCE.name());
    }

    private void handleCommands(SessionManager sessionManager, String cmd) {
        Command command = sessionManager.getCommand(cmd);
        command.execute();
        sessionManager.setAtmState(new MenuState(atm, ui));
    }
}

