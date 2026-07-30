package lesson_6_reflection_annotations.utils;

import lesson_6_reflection_annotations.annotations.Description;
import lesson_6_reflection_annotations.annotations.Test;

import java.lang.reflect.Method;

/**
 * Вспомогательный класс для чтения значений аннотаций {@link Test} и {@link Description}.
 */
public class AnnotationProcessorUtil {

    /**
     * Возвращает имя теста (из аннотации @Test) или имя метода, если имя не задано.
     *
     * @param method метод, помеченный @Test
     * @return имя теста
     */
    public static String testName(Method method) {
        Test test = method.getAnnotation(Test.class);
        if (test == null || test.name().isBlank()) {
            return method.getName();
        }
        return test.name();
    }

    /**
     * Возвращает описание метода из аннотации {@link Description}.
     *
     * @param method метод
     * @return описание или null, если аннотация отсутствует или пуста
     */
    public static String descriptionMethod(Method method) {
        Description description = method.getAnnotation(Description.class);
        if (description == null || description.value().isBlank()) {
            return null;
        }
        return description.value();
    }

    /**
     * Возвращает описание класса из аннотации {@link Description}.
     *
     * @param testClass класс
     * @return описание или null, если аннотация отсутствует или пуста
     */
    public static String descriptionClass(Class<?> testClass) {
        Description description = testClass.getAnnotation(Description.class);
        if (description == null || description.value().isBlank()) {
            return null;
        }
        return description.value();
    }
}
