package lesson_13_oop_principles.entities;

import lesson_13_oop_principles.Denomination;
import lesson_13_oop_principles.exceptions.CapacityMoneyBoxException;

/**
 * Бокс банкомата для хранения банкнот одного номинала.
 * <p>Сравнивается по убыванию номинала (реализует {@link Comparable}),
 * что обеспечивает сортировку кассет от большего номинала к меньшему.</p>
 * <p>Создаётся через {@link Builder} с валидацией параметров.</p>
 */

public class MoneyBox implements Comparable<MoneyBox> {
    private final Denomination denomination;
    private final int capacity;
    private int numOfBanknotes;

    private MoneyBox(Builder builder) {
        this.denomination = builder.denomination;
        this.capacity = builder.capacity;
        this.numOfBanknotes = builder.numOfBanknotes;
    }

    /**
     * Создаёт билдер для бокса.
     *
     * @param denomination номинал банкнот
     * @param capacity     максимальная вместимость бокса
     * @return новый {@link Builder}
     */

    public static Builder builder(Denomination denomination, int capacity) {
        return new Builder(denomination, capacity);
    }

    /**
     * Сравнивает боксы по убыванию номинала.
     */

    @Override
    public int compareTo(MoneyBox o) {
        return Integer.compare(this.denomination.getValue(), o.denomination.getValue()) * (-1);
    }

    /**
     * Билдер для {@link MoneyBox} с валидацией параметров на этапе построения.
     */

    public static class Builder {
        private final Denomination denomination;
        private final int capacity;
        private int numOfBanknotes;

        /**
         * @param denomination номинал банкнот
         * @param capacity     вместимость бокса (должна быть &gt; 0)
         * @throws IllegalArgumentException если {@code capacity <= 0}
         */

        public Builder(Denomination denomination, int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be greater than zero");
            }
            this.denomination = denomination;
            this.capacity = capacity;
        }

        /**
         * Устанавливает начальное количество банкнот.
         *
         * @param numOfBanknotes количество (в пределах {@code [0, capacity]})
         * @return этот билдер для цепочки вызовов
         * @throws IllegalArgumentException если значение выходит за допустимые пределы
         */

        public Builder numOfBanknotes(int numOfBanknotes) {
            if (numOfBanknotes < 0 || numOfBanknotes > capacity) {
                throw new IllegalArgumentException(
                        "Number of banknotes must be between 0 and " + capacity);
            }
            this.numOfBanknotes = numOfBanknotes;
            return this;
        }

        /**
         * @return готовый объект {@link MoneyBox}
         */

        public MoneyBox build() {
            return new MoneyBox(this);
        }
    }

    /**
     * @return номинал банкнот в боксе
     */

    public Denomination getDenomination() {
        return denomination;
    }

    /**
     * @return текущее количество банкнот в боксе
     */

    public int getNumOfBanknotes() {
        return numOfBanknotes;
    }

    /**
     * Устанавливает количество банкнот (package private).
     *
     * @param numOfBanknotes новое количество
     * @throws CapacityMoneyBoxException если значение выходит за пределы {@code [0, capacity]}
     */

    void setNumOfBanknotes(int numOfBanknotes) {
        if (numOfBanknotes < 0 || numOfBanknotes > capacity) {
            throw new CapacityMoneyBoxException(capacity);
        }
        this.numOfBanknotes = numOfBanknotes;
    }

    /**
     * @return количество свободных мест в боксе
     */

    public int getEmptySpace() {
        return capacity - numOfBanknotes;
    }

    @Override
    public String toString() {
        return "MoneyBox{" +
                "denomination=" + denomination +
                ", capacity=" + capacity +
                ", numOfBanknotes=" + numOfBanknotes +
                '}';
    }
}
