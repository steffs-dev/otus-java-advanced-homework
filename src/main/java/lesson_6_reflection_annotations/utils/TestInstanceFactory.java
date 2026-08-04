package lesson_6_reflection_annotations.utils;

import java.lang.reflect.Constructor;

/**
 * Фабрика для создания экземпляров тестовых классов через конструктор без параметров.
 * <p>
 * Использует рефлексию, делает конструктор доступным (если он приватный).
 */
public class TestInstanceFactory {

    /**
     * Создаёт новый экземпляр класса.
     *
     * @param clazz класс теста
     * @return новый объект
     * @throws Exception если конструктор недоступен или создание не удалось
     */
    public static Object newInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
