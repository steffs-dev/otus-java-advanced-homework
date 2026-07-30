package lesson_6_reflection_annotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, отмечающая метод как тестовый.
 * Методы с этой аннотацией выполняются фреймворком как отдельные тесты.
 * Каждый тест запускается в своём экземпляре класса.
 * Целевой элемент – метод, время жизни – RUNTIME.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    /**
     * Пользовательское имя теста. Если не задано, используется имя метода.
     * @return имя теста
     */
    String name() default "";
}
