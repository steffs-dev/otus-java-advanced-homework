package lesson_13_oop_principles;

import lesson_13_oop_principles.commands.Command;
import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.states.AtmStates;
import lesson_13_oop_principles.states.ExitState;
import lesson_13_oop_principles.states.MenuState;
import lesson_13_oop_principles.ui.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Менеджер сессии пользователя с банкоматом.
 * <p>Хранит текущее состояние ({@link AtmStates}) и обеспечивает цикл
 * выполнения состояний в методе {@link #execute()}. Завершение цикла
 * происходит при переходе в состояние {@link ExitState}.</p>
 */

public class SessionManager {
    private final Atm atm;
    private AtmStates atmState;
    private final UI ui;

    private static final Logger log = LogManager.getLogger(SessionManager.class);

    /**
     * @param atm экземпляр банкомата
     * @param ui  интерфейс пользователя
     */

    public SessionManager(Atm atm, UI ui) {
        this.atm = atm;
        atmState = new MenuState(atm, ui);
        this.ui = ui;
    }

    /**
     * Устанавливает новое состояние сессии.
     */

    public void setAtmState(AtmStates atmState) {
        this.atmState = atmState;
    }

    /**
     * Делегирует запрос команды диспетчеру банкомата.
     */

    public Command getCommand(String commandName) {
        return atm.getDispatcher().getCommand(commandName);
    }

    /**
     * @return текущий UI
     */

    public UI getUi() {
        return ui;
    }

    /**
     * Главный цикл сессии: последовательно вызывает {@code execute()}
     * у текущего состояния, пока не будет достигнуто {@link ExitState}.
     * При достижении {@link ExitState} цикл завершается, выполняется execute
     * у {@link ExitState} и программа завершается
     */

    public void execute() {
        while (isRunning()) {
            atmState.execute(this);
        }
        atmState.execute(this);
    }

    private boolean isRunning() {
        return !(this.atmState instanceof ExitState);
    }

    /**
     * Закрывает ресурсы UI (например, {@link java.io.BufferedReader}).
     */

    public void disconnect() {
        try {
            ui.close();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }
}
