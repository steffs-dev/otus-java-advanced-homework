package lesson_13_oop_principles.commands;

/**
 * Перечисление всех доступных команд банкомата.
 * <p>Имена констант используются как строковые ключи при парсинге ввода</p>
 */

public enum CommandNames {
    ATM_BALANCE,
    DEPOSIT,
    WITHDRAW,
    MENU,
    EXIT,
    DEFAULT
}
