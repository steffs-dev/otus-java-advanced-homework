package lesson_13_oop_principles;

import lesson_13_oop_principles.commands.*;
import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.ui.UI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Диспетчер команд: регистрирует и выдаёт команды по их строковому имени.
 * <p>Разделяет команды на два типа:</p>
 * <ul>
 *   <li><b>Singleton-команды</b> - создаются один раз и кэшируются
 *       (например, {@code ATM_BALANCE}, {@code MENU}, {@code EXIT});</li>
 *   <li><b>Dynamic-команды</b> - создаются заново при каждом запросе
 *       через {@link BiFunction} (например, {@code DEPOSIT}, {@code WITHDRAW}).</li>
 * </ul>
 * <p>Любая возвращаемая команда оборачивается в {@link GlobalCommand}
 * для централизованной обработки ошибок.</p>
 */

public class Dispatcher {
    private final Atm atm;
    private final UI ui;

    /**
     * Реестр singleton-команд - команд, не требующих дополнительных параметров
     * и создаваемых один раз при инициализации диспетчера.
     * <p>Примеры: {@code ATM_BALANCE}, {@code MENU}, {@code EXIT}, {@code DEFAULT}.</p>
     */

    private final Map<CommandNames, SingletonCommand> cachedCommands;

    /**
     * Реестр фабрик для dynamic-команд - команд, требующих дополнительных параметров
     * (сумма, номинал) и создаваемых заново при каждом запросе.
     * <p>Примеры: {@code WITHDRAW}, {@code DEPOSIT}.</p>
     */

    private final Map<CommandNames, BiFunction<Atm, UI, DynamicCommand>> dynamicCommandsFunctions;

    /**
     * @param atm экземпляр банкомата, передаваемый в команды
     * @param ui  интерфейс пользователя, передаваемый в команды
     */

    public Dispatcher(Atm atm, UI ui) {
        this.atm = atm;
        this.ui = ui;
        cachedCommands = registerSingletonCommands();
        dynamicCommandsFunctions = registerDynamicCommands();
    }

    /**
     * Регистрирует singleton-команды - команды, не требующие параметров
     * и создаваемые один раз при инициализации диспетчера.
     * <p>
     * Регистрирует следующие команды:
     * <ul>
     *   <li>{@link CommandNames#ATM_BALANCE} - {@link AtmBalanceInfo} - вывод баланса;</li>
     *   <li>{@link CommandNames#EXIT} - {@link ExitCommand} - завершение сессии;</li>
     *   <li>{@link CommandNames#MENU} - {@link MenuCommand} - вывод главного меню;</li>
     *   <li>{@link CommandNames#DEFAULT} - {@link DefaultCommand} - обработка неизвестных команд.</li>
     * </ul>
     *
     * @return неизменяемая после создания карта зарегистрированных singleton-команд
     */

    private Map<CommandNames, SingletonCommand> registerSingletonCommands() {
        Map<CommandNames, SingletonCommand> regCommands = new HashMap<>();
        regCommands.put(CommandNames.ATM_BALANCE, new AtmBalanceInfo(atm, ui));
        regCommands.put(CommandNames.EXIT, new ExitCommand());
        regCommands.put(CommandNames.MENU, new MenuCommand(ui));
        regCommands.put(CommandNames.DEFAULT, new DefaultCommand(ui));

        return regCommands;
    }

    /**
     * Регистрирует фабрики для dynamic-команд - команд, требующих дополнительных
     * параметров перед выполнением.
     * <p>
     * Каждая фабрика представлена как {@link BiFunction}, принимающая {@link Atm} и {@link UI},
     * и возвращающая новый экземпляр команды. Это позволяет:
     * <ul>
     *   <li>использовать method reference ({@code WithdrawCommand::new});</li>
     *   <li>создавать новый экземпляр команды при каждом запросе, избегая проблем
     *       с сохранением состояния между вызовами.</li>
     * @return карта фабрик dynamic-команд
     */

    private Map<CommandNames, BiFunction<Atm, UI, DynamicCommand>> registerDynamicCommands() {
        Map<CommandNames, BiFunction<Atm, UI, DynamicCommand>> regDynamicCommands = new HashMap<>();
        regDynamicCommands.put(CommandNames.WITHDRAW, WithdrawCommand::new);
        regDynamicCommands.put(CommandNames.DEPOSIT, DepositCommand::new);
        return regDynamicCommands;
    }

    /**
     * Возвращает команду по её строковому имени, обёрнутую в {@link GlobalCommand}.
     * @param commandName строковое имя команды (должно совпадать с именем одной из констант
     *                    {@link CommandNames}, регистр важен — ожидается верхний регистр)
     * @return команда, обёрнутая в {@link GlobalCommand}; никогда не возвращает {@code null}
     */

    public Command getCommand(String commandName) {
        CommandNames enumName;
        try {
            enumName = CommandNames.valueOf(commandName);
        } catch (IllegalArgumentException e) {
            return new GlobalCommand(getDefaultCommand(), ui);
        }
        SingletonCommand singletonCommand = cachedCommands.get(enumName);
        if (singletonCommand != null) {
            return new GlobalCommand(singletonCommand, ui);
        }

        BiFunction<Atm, UI, DynamicCommand> function = dynamicCommandsFunctions.get(enumName);
        if (function != null) {
            DynamicCommand dynamicCommand = function.apply(atm, ui);
            return new GlobalCommand(dynamicCommand, ui);
        }

        return new GlobalCommand(getDefaultCommand(), ui);
    }

    /**
     * Возвращает команду по умолчанию, используемую при вводе неизвестного имени команды.
     * <p>
     * Команда {@link DefaultCommand} выводит сообщение о неизвестной команде
     * и не изменяет состояние банкомата.
     * </p>
     *
     * @return singleton-команда {@link DefaultCommand} из реестра {@link #cachedCommands}
     */

    private SingletonCommand getDefaultCommand() {
        return cachedCommands.get(CommandNames.DEFAULT);
    }

}
