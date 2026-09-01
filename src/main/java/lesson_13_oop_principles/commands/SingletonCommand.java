package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Dispatcher;

/**
 * Маркерный интерфейс для команд, создаваемых один раз
 * (кэшируются в {@link Dispatcher}).
 * <p>Примеры: {@code ATM_BALANCE}, {@code MENU}, {@code EXIT}.</p>
 */

public interface SingletonCommand extends Command {

}
