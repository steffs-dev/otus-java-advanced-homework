package lesson_13_oop_principles;

import lesson_13_oop_principles.entities.Atm;
import lesson_13_oop_principles.entities.MoneyBox;
import lesson_13_oop_principles.ui.Console;

/**
 * Фасад для инициализации банкомата с предустановленной конфигурацией.
 * <p>В дефолтной реализации создаёт три кассеты (10, 50, 100) с начальным наполнением,
 * собирает {@link Atm} и запускает {@link SessionManager}.</p>
 */

public class AtmFacade {

    /**
     * Создаёт и запускает банкомат с дефолтным набором кассет:
     * <ul>
     *   <li>10 × 10 банкнот (номинал 10)</li>
     *   <li>50 × 25 банкнот (номинал 50)</li>
     *   <li>1000 × 500 банкнот (номинал 100)</li>
     * </ul>
     */

    public static void startDefaultAtm() {
        Console console = new Console();
        MoneyBox mb10 = MoneyBox.builder(Denomination.TEN, 100)
                .numOfBanknotes(10).build();
        MoneyBox mb50 = MoneyBox.builder(Denomination.FIFTY, 50)
                .numOfBanknotes(25).build();
        MoneyBox mb100 = MoneyBox.builder(Denomination.HUNDRED, 1000)
                .numOfBanknotes(500).build();
        Atm atm = new Atm(console, mb10, mb50, mb100);
        SessionManager sessionManager = new SessionManager(atm, console);
        sessionManager.execute();
    }
}
