package lesson_13_oop_principles.states;

import lesson_13_oop_principles.Messages;
import lesson_13_oop_principles.SessionManager;
import lesson_13_oop_principles.ui.UI;

/**
 * Состояние завершения сессии.
 * <p>Выводит прощальное сообщение и закрывает ресурсы UI</p>
 */

public class ExitState implements AtmStates {
    private final UI ui;

    public ExitState(UI ui) {
        this.ui = ui;
    }

    @Override
    public void execute(SessionManager sessionManager) {
        ui.print(Messages.get("goodbye"));
        sessionManager.disconnect();
    }
}
