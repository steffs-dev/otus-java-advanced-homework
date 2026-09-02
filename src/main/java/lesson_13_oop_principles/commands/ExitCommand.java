package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.states.ExitState;
import lesson_13_oop_principles.states.MenuState;

/**
 * Singleton-команда завершения сессии.
 * <p>Сама по себе ничего не делает - переход в {@link ExitState}
 * обрабатывается в {@link MenuState}.</p>
 * <p>Сделана для дальнейшего расширения функционала при обработке завершения работы</p>
 */

public class ExitCommand implements SingletonCommand {

    @Override
    public void execute() {

    }

}
