package lesson_13_oop_principles.states;

import lesson_13_oop_principles.*;
import lesson_13_oop_principles.commands.*;
import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.exceptions.InvalidInputException;
import lesson_13_oop_principles.ui.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Состояние ввода суммы/номинала для команд {@code WITHDRAW} и {@code DEPOSIT}.
 * <p>Определяет тип требуемого ввода через маркерные интерфейсы
 * ({@link AmountRequired} или {@link DenominationAndBanknotesRequired})
 * и парсит ввод через {@link InputParser}.</p>
 * <p>При ошибке ввода остаётся в том же состоянии, предлагая повторить попытку.</p>
 */

public class AmountInputState implements AtmStates {
    private final Atm atm;
    private final UI ui;
    private final Command command;
    private Command executingCommand;
    private static final Logger log = LogManager.getLogger(AmountInputState.class);

    /**
     * @param atm     банкомат
     * @param ui      интерфейс пользователя
     * @param command команда, для которой запрашивается ввод (обёрнута в {@link GlobalCommand})
     */

    public AmountInputState(Atm atm, UI ui, Command command) {
        this.atm = atm;
        this.ui = ui;
        this.command = command;
        executingCommand = ((GlobalCommand) command).executingCommand();
        ;
    }

    @Override
    public void execute(SessionManager sessionManager) {
        ui.printf(Messages.get("enter.amount",
                (executingCommand instanceof DenominationAndBanknotesRequired) ? "номинал и" : "",
                CommandNames.MENU.name()));
        String input = "";
        try {
            input = ui.readLine();
        } catch (RuntimeException e) {
            log.warn("IO Exception while reading input command in menu state {}", e.getMessage());
        }
        handleCommands(sessionManager, input, ui);
    }

    /**
     * Обрабатывает введённую строку и переключает состояние.
     */

    private void handleCommands(SessionManager sessionManager, String input, UI ui) {
        if (input.trim().equalsIgnoreCase(CommandNames.MENU.name())) {
            sessionManager.setAtmState(new MenuState(atm, ui));
        } else if (executingCommand instanceof AmountRequired req) {
            int amount = 0;
            try {
                amount = InputParser.getParsedDigits(input);
                if (amount <= 0) {
                    throw new InvalidInputException("Invalid amount given");
                }
            } catch (InvalidInputException e) {
                ui.printf(Messages.get("enter.correct",
                        input,
                        CommandNames.MENU.name()));
                sessionManager.setAtmState(new AmountInputState(atm, ui, command));
                return;
            }

            req.setAmount(amount);
            command.execute();
            sessionManager.setAtmState(new MenuState(atm, ui));
        } else if (executingCommand instanceof DenominationAndBanknotesRequired req) {
            try {
                Map<Denomination, Integer> complexParsedValue = InputParser.getComplexParsedValue(input);
                boolean incorrectAmount = false;
                for (Map.Entry<Denomination, Integer> entry : complexParsedValue.entrySet()) {
                    if (entry.getValue() <= 0) {
                        ui.printf(Messages.get("enter.correct",
                                entry.getValue(),
                                CommandNames.MENU.name()));
                        sessionManager.setAtmState(new AmountInputState(atm, ui, command));
                        incorrectAmount = true;
                        break;
                    }
                }
                if (incorrectAmount) {
                    return;
                }

                complexParsedValue.forEach(req::setDenominationAndBanknotes);
                command.execute();
                sessionManager.setAtmState(new BalanceState(atm, ui));
            } catch (InvalidInputException | IllegalArgumentException e) {
                ui.print(Messages.get("invalid.input"));
                sessionManager.setAtmState(new AmountInputState(atm, ui, command));
            }
        }
    }
}