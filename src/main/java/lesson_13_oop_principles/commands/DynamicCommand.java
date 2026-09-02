package lesson_13_oop_principles.commands;

import lesson_13_oop_principles.Dispatcher;

/**
 * Маркерный интерфейс для команд, требующих дополнительных параметров,
 * которые задаются через сеттеры перед выполнением.
 * <p>Создаются заново при каждом запросе через {@link Dispatcher}.</p>
 */

public interface DynamicCommand extends Command {
}
