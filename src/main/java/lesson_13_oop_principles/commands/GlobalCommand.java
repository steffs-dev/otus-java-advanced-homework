package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.exceptions.CapacityMoneyBoxException;
import lesson_13_oop_principles.exceptions.InvalidInputException;
import lesson_13_oop_principles.ui.UI;
import lesson_13_oop_principles.exceptions.AtmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Команда-декоратор, оборачивающая любую другую команду для централизованной
 * обработки исключений.
 * <p>Реализована как {@code record} для краткости. Перехватывает:</p>
 * <ul>
 *   <li>{@link InvalidInputException} - сообщение о неверном вводе;</li>
 *   <li>{@link CapacityMoneyBoxException} - сообщение об ошибке в допустимом объеме бокса;</li>
 *   <li>{@link AtmException} - сообщение об ошибке банкомата;</li>
 *   <li>{@link RuntimeException} - общее сообщение об ошибке.</li>
 * </ul>
 * <p>Все исключения логируются через Log4j.</p>
 *
 * @param executingCommand оборачиваемая команда
 * @param ui               интерфейс пользователя для вывода сообщений
 */

public record GlobalCommand(Command executingCommand, UI ui) implements Command {
    private static final Logger log = LogManager.getLogger(GlobalCommand.class);

    @Override
    public void execute() {
        try {
            executingCommand.execute();
        } catch (InvalidInputException e) {
            ui.print(Messages.get("invalid_input"));
            log.error("Error {} while executing command:{}",
                    e.getClass().getSimpleName(), e.getMessage());
        } catch (CapacityMoneyBoxException e) {
            ui.print(Messages.get("funds.capacity.exception"));
            log.error("Capacity exception {} while executing command: {}", e.getClass().getSimpleName(), e.getMessage());
        } catch (AtmException e) {
            ui.print(e.getMessage());
            log.error("AtmException {} while executing command: {}", e.getClass().getSimpleName(), e.getMessage());
        } catch (RuntimeException e) {
            ui.print(Messages.get("unexpected.error"));
            log.error("Runtime exception while executing command {}: {}", e.getMessage(), e);
        }
    }
}
