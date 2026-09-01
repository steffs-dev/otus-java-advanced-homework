package lesson_13_oop_principles;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Утилитарный класс для работы с локализованными сообщениями.
 * <p>Загружает строки из {@code messages.properties} через {@link ResourceBundle}
 * и форматирует их через {@link MessageFormat} (синтаксис {@code {0}}, {@code {1}}).</p>
 */

public class Messages {
    private static final ResourceBundle resources = ResourceBundle.getBundle("messages");

    /**
     * Возвращает строку по ключу без параметров.
     *
     * @param key ключ из {@code messages.properties}
     * @return локализованная строка
     */

    public static String get(String key) {
        return resources.getString(key);
    }

    /**
     * Возвращает отформатированную строку с подстановкой аргументов.
     *
     * @param key  ключ из {@code messages.properties}
     * @param args аргументы для подстановки в плейсхолдеры {@code {0}}, {@code {1}} и т.д.
     * @return отформатированная строка
     */

    public static String get(String key, Object... args) {
        return MessageFormat.format(resources.getString(key), args);
    }
}
