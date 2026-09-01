package lesson_13_oop_principles;

import lesson_13_oop_principles.commands.CommandNames;
import lesson_13_oop_principles.exceptions.InvalidInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Парсер пользовательского ввода.
 * <p>Отвечает за:</p>
 * <ul>
 *   <li>преобразование строки в имя команды;</li>
 *   <li>валидацию и парсинг числовых значений (с защитой от переполнения через {@link BigInteger});</li>
 *   <li>парсинг составного ввода "номинал количество" для команды DEPOSIT.</li>
 * </ul>
 * <p>При невалидном вводе бросает {@link InvalidInputException}.</p>
 */

public class InputParser {

    /**
     * Регулярное выражение для валидации положительных целых чисел.
     */

    private static final String FIGURE_REGEX = "^[0-9]\\d*$";
    private static final Logger log = LogManager.getLogger(InputParser.class);

    /**
     * Преобразует строку в каноническое имя команды.
     *
     * @param input строка от пользователя
     * @return имя команды в верхнем регистре; при пустом/null вводе - {@code "DEFAULT"}
     */

    public static String getCommandName(String input) {
        return parseCommandName(input);
    }

    private static String parseCommandName(String input) {
        if (input == null || input.isBlank()) {
            log.warn("Wrong input format (null or blank)");
            return CommandNames.DEFAULT.name();
        }
        return input.trim().toUpperCase();
    }

    /**
     * Парсит строку в положительное целое число с защитой от переполнения.
     *
     * @param input строка, содержащая число
     * @return распарсенное значение
     * @throws InvalidInputException если ввод пустой, не число или превышает {@link Integer#MAX_VALUE}
     */

    public static int getParsedDigits(String input) {
        if (input == null || input.isBlank()) {
            log.warn("Wrong input format (null, blank or not a digit)");
            throw new InvalidInputException("Input is null or blank");
        }

        String inputToParse = input.trim();
        if (!inputToParse.matches(FIGURE_REGEX)) {
            log.warn("Invalid input format");
            throw new InvalidInputException("Invalid input format: " + inputToParse);
        }
        try {
            BigInteger bigValue = new BigInteger(inputToParse);
            if (bigValue.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new InvalidInputException("Number exceeds Integer.MAX_VALUE: " + inputToParse);
            }
            return bigValue.intValue();
        } catch (NumberFormatException e) {
            log.warn("Unable to parse number {}", inputToParse);
            throw new InvalidInputException("Unable to parse number " + inputToParse);
        }
    }

    /**
     * Парсит ввод вида "номинал количество" (например, "500 10").
     *
     * @param input строка с двумя числами, разделёнными пробелом
     * @return {@link Map} из одного элемента: номинал - количество
     * @throws InvalidInputException если формат неверный или номинал не существует
     */

    public static Map<Denomination, Integer> getComplexParsedValue(String input) {
        String[] inputToParse = input.split("\\s+");
        if (inputToParse.length != 2) {
            log.warn("Wrong input format (you need to input denomination first then an amount)");
            throw new InvalidInputException("Wrong denomination/amount input format");
        }
        Denomination denomination;
        try {
            int denomToParse = Integer.parseInt(inputToParse[0]);
            denomination = Denomination.denominationFromValue(denomToParse);
            if (denomination == null) {
                log.info("Invalid input of denomination {}", inputToParse[0]);
                throw new InvalidInputException("Wrong denomination input format");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input of denomination - not a number");
        }
        Integer amount = getParsedDigits(inputToParse[1]);
        Map<Denomination, Integer> map = new HashMap<>();
        map.put(denomination, amount);
        return map;
    }
}
