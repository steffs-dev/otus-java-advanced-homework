package lesson_13_oop_principles.states;

import lesson_13_oop_principles.SessionManager;

/**
 * Интерфейс состояния сессии банкомата (паттерн <b>State</b>).
 * <p>Каждое состояние определяет поведение приложения при обработке ввода пользователя.</p>
 */

public interface AtmStates {

    /**
     * Выполняет действия, характерные для данного состояния.
     *
     * @param sessionManager менеджер сессии для переключения состояний и получения команд
     */

    void execute(SessionManager sessionManager);
}
