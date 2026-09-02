package lesson_13_oop_principles.states;

import lesson_13_oop_principles.*;
import lesson_13_oop_principles.commands.*;
import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.ui.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Состояние главного меню.
 * <p>Выводит приветствие и список команд, читает ввод пользователя
 * и переключает состояние в зависимости от типа команды:</p>
 * <ul>
 *   <li>{@code ATM_BALANCE} - немедленное выполнение;</li>
 *   <li>{@code WITHDRAW}/{@code DEPOSIT} - переход в {@link AmountInputState};</li>
 *   <li>{@code EXIT} - переход в {@link ExitState}.</li>
 * </ul>
 */

public class MenuState implements AtmStates {
    private final Atm atm;
    private final UI ui;
    private static final Logger log = LogManager.getLogger(MenuState.class);

    public MenuState(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    @Override
    public void execute(SessionManager sessionManager) {
        ui.print(Messages.get("welcome.menu"));
        Command command = sessionManager.getCommand(CommandNames.MENU.name());
        command.execute();
        String input = null;
        try {
            input = ui.readLine();
        } catch (RuntimeException e) {
            log.warn("IO Exception while reading input command in menu state {}", e.getMessage());
        }

        String cmd = InputParser.getCommandName(input);
        handleCommands(sessionManager, cmd);

    }

    /**
     * Маршрутизирует команду в соответствующее состояние.
     */

    private void handleCommands(SessionManager sessionManager, String cmd) {
        Command command = sessionManager.getCommand(cmd);
        Command executingCommand = (command instanceof GlobalCommand) ?
                ((GlobalCommand) command).executingCommand() : command;

        if (executingCommand instanceof AtmBalanceInfo) {
            command.execute();
        } else if (executingCommand instanceof AmountRequired) {
            sessionManager.setAtmState(new AmountInputState(atm, ui, command));
        } else if (executingCommand instanceof DenominationAndBanknotesRequired) {
            sessionManager.setAtmState(new AmountInputState(atm, ui, command));
        } else if (executingCommand instanceof ExitCommand) {
            sessionManager.setAtmState(new ExitState(ui));
        }
    }
}
