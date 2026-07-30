package lesson_6_reflection_annotations.utils;

import java.lang.reflect.Method;

/**
 * Утилита для безопасного вызова методов через рефлексию.
 * Делает метод доступным (setAccessible) и вызывает его, возвращая true при успехе,
 * false при любом исключении (включая проверяемые).
 */
public class MethodInvoker {

    /**
     * Вызывает метод на указанном объекте.
     *
     * @param testInstance экземпляр, на котором вызывается метод
     * @param method       вызываемый метод
     * @return true, если метод выполнен без исключений, иначе - false
     */
    public static boolean invokeMethod(Object testInstance, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(testInstance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
