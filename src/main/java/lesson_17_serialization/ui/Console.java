package lesson_17_serialization.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Консольная реализация {@link UI}.
 * <p>Использует {@link System#out} для вывода и {@link BufferedReader}
 * поверх {@link System#in} для ввода.</p>
 */

public class Console implements UI {

    /**
     * Reader для чтения пользовательского ввода из консоли.
     */

    private final BufferedReader reader;

    /**
     * Создает консольный интерфейс.
     */

    public Console() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Вывод выполняется в стандартный поток вывода.</p>
     */

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Форматированный вывод выполняется в стандартный поток вывода.</p>
     */

    @Override
    public void printf(String message, Object... args) {
        System.out.printf(message, args);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Читает строку из стандартного потока ввода.</p>
     *
     * @return введенная пользователем строка
     * @throws RuntimeException если возникает ошибка ввода
     */

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Закрывает ресурс чтения пользовательского ввода.
     *
     * <p>При закрытии {@link BufferedReader} также может быть закрыт
     * поток {@link System#in}.</p>
     *
     * @throws RuntimeException если возникает ошибка при закрытии ресурса
     */

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while closing resources", e);
        }
    }
}
