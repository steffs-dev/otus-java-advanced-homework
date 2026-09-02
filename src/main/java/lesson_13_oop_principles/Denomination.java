package lesson_13_oop_principles;

/**
 * Перечисление номиналов банкнот, поддерживаемых банкоматом.
 * <p>Каждый номинал имеет целочисленное значение. Используется как ключ
 * при распределении банкнот по кассетам и при расчёте сумм.</p>
 */

public enum Denomination {
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    /**
     * @return числовое значение номинала
     */

    public int getValue() {
        return value;
    }

    /**
     * Находит номинал по его числовому значению.
     *
     * @param input числовое значение номинала
     * @return соответствующий {@code Denomination}
     * @throws IllegalArgumentException если номинал не найден
     */

    public static Denomination denominationFromValue(int input) {
        for (Denomination d : values()) {
            if (d.value == input) {
                return d;
            }
        }
        return null;
    }
}
