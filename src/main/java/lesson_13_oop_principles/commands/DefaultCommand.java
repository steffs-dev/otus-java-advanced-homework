package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.ui.UI;

/**
 * Singleton-команда, выполняемая при вводе неизвестного имени команды.
 * <p>Выводит сообщение "Неизвестная команда".</p>
 */

public class DefaultCommand implements SingletonCommand {
    private final UI ui;

    public DefaultCommand(UI ui) {
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.print(Messages.get("unknown.command"));
    }
}
