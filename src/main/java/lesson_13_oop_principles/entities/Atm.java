package lesson_13_oop_principles.entities;

import lesson_13_oop_principles.Denomination;
import lesson_13_oop_principles.Dispatcher;
import lesson_13_oop_principles.ui.UI;
import lesson_13_oop_principles.exceptions.AtmException;
import lesson_13_oop_principles.exceptions.CapacityMoneyBoxException;

import java.util.*;

/**
 * Главный класс банкомата - агрегат, управляющий боксами и бизнес-операциями.
 * <p>Инкапсулирует логику внесения и снятия банкнот</p>
 * <p>Содержит {@link Dispatcher} для маршрутизации команд.</p>
 */

public class Atm {

    /**
     * Список боксов, отсортированный по убыванию номинала для повышения эффективности
     * выбора номинала банкнот для выдачи
     */

    List<MoneyBox> moneyBoxes = new ArrayList<>();
    private final Dispatcher dispatcher;

    /**
     * Создаёт банкомат с указанными боксами.
     *
     * @param ui    интерфейс пользователя (передаётся в диспетчер)
     * @param boxes боксы для инициализации
     */

    public Atm(UI ui, MoneyBox... boxes) {
        moneyBoxes = sortMoneyBoxes(boxes);
        dispatcher = new Dispatcher(this, ui);
    }

    /**
     * @return неизменяемое представление списка кассет
     */

    public List<MoneyBox> getMoneyBoxes() {
        return Collections.unmodifiableList(moneyBoxes);
    }

    /**
     * @return диспетчер команд банкомата
     */

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Сортирует и сохраняет боксы (по убыванию номинала).
     *
     * @param boxes боксы для добавления
     * @return отсортированный список боксов
     */

    public List<MoneyBox> sortMoneyBoxes(MoneyBox... boxes) {
        List<MoneyBox> list = new ArrayList<>(Arrays.asList(boxes));
        Collections.sort(list);
        return list;
    }

    /**
     * Вносит банкноты указанного номинала в банкомат.
     * <p>Распределяет банкноты по боксам соответствующего номинала,
     * пока не заполнит их или не разместит все банкноты.</p>
     *
     * @param denomination номинал вносимых банкнот
     * @param banknotes    количество банкнот
     * @throws CapacityMoneyBoxException если в боксах нет места
     */

    public void deposit(Denomination denomination, int banknotes) {
        int possibleToPut = banknotesPossibleToPut(denomination, moneyBoxes);
        if (isPossibleToPut(possibleToPut, banknotes)) {
            for (MoneyBox box : moneyBoxes) {
                if (box.getDenomination() == denomination && banknotes > 0) {
                    int banknotesToPut = banknotesToPut(box, banknotes);
                    box.setNumOfBanknotes(box.getNumOfBanknotes() + banknotesToPut);
                    banknotes -= banknotesToPut;
                }
            }
        } else {
            throw new CapacityMoneyBoxException(possibleToPut);
        }
    }

    private int banknotesPossibleToPut(Denomination denomination, List<MoneyBox> moneyBoxes) {
        int emptySpace = 0;
        for (MoneyBox box : moneyBoxes) {
            if (box.getDenomination() == denomination) {
                emptySpace += box.getEmptySpace();
            }
        }
        return emptySpace;
    }

    private boolean isPossibleToPut(int possibleToPut, int banknotes) {
        return possibleToPut >= banknotes;
    }

    private int banknotesToPut(MoneyBox moneyBox, int banknotes) {
        return Math.min(banknotes, moneyBox.getEmptySpace());
    }

    /**
     * Снимает указанную сумму из банкомата, используя жадный алгоритм
     * (от большего номинала к меньшему).
     *
     * @param amount сумма для снятия
     * @return мапа "номинал - количество выданных банкнот"
     * @throws AtmException если сумму невозможно выдать (недостаточно средств)
     */

    public Map<Denomination, Integer> withdraw(int amount) {
        Map<Denomination, Integer> banknotes = createDenominationPlan(amount);
        reducedBanknotesAmount(banknotes);

        return banknotes;
    }

    /**
     * Рассчитывает план выдачи банкнот без изменения состояния боксов.
     *
     * @param amount требуемая сумма
     * @return план выдачи
     * @throws AtmException если сумму невозможно выдать
     */

    private Map<Denomination, Integer> createDenominationPlan(int amount) {
        Map<Denomination, Integer> banknotes = new TreeMap<>(Collections.reverseOrder());

        int amountLeft = amount;
        for (MoneyBox box : moneyBoxes) {
            if (amountLeft <= 0) {
                break;
            }
            int denominationValue = box.getDenomination().getValue();
            int availableBills = box.getNumOfBanknotes();

            int count = Math.min(availableBills, amountLeft / denominationValue);

            if (count > 0) {
                banknotes.merge(box.getDenomination(), count, Integer::sum);
                amountLeft -= count * denominationValue;
            }
        }
        if (amountLeft != 0) {
            throw new AtmException("Unable to withdraw because of insufficient funds");
        }

        return banknotes;
    }

    /**
     * Применяет план выдачи, уменьшая количество банкнот в боксах.
     */

    private void reducedBanknotesAmount(Map<Denomination, Integer> banknotes) {
        for (Map.Entry<Denomination, Integer> entry : banknotes.entrySet()) {
            int amount = entry.getValue();
            for (MoneyBox box : moneyBoxes) {
                if (box.getDenomination().equals(entry.getKey())) {
                    int amountToReduce = Math.min(amount, box.getNumOfBanknotes());
                    box.setNumOfBanknotes(box.getNumOfBanknotes() - amountToReduce);
                    amount -= amountToReduce;
                }
            }
        }
    }
}
