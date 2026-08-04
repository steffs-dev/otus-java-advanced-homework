package lesson_6_reflection_annotations.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитарный класс для извлечения методов, помеченных заданной аннотацией.
 * Анализирует все объявленные методы класса (включая приватные) и возвращает список.
 */
public class AnnotationExtractor {

    /**
     * Возвращает все методы класса, на которых присутствует указанная аннотация.
     *
     * @param testClass  класс для анализа
     * @param annotation класс аннотации
     * @return список методов (может быть пустым)
     */
    public static List<Method> extractMethods(Class<?> testClass, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }
}
