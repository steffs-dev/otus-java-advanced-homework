package lesson_13_oop_principles;

/**
 * Точка входа в приложение "Банкомат".
 * <p>Запускает стандартную конфигурацию банкомата через {@link AtmFacade}.</p>
 */

public class AtmApplication {

    public static void main(String[] args) {
        AtmFacade.startDefaultAtm();
    }
}
