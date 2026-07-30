package lesson_6_reflection_annotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для добавления текстового описания к классу или методу.
 * Используется для вывода кастомного имени теста или класса вместо технического имени.
 * Может быть применена как к классу (для описания всего набора тестов), так и к методу.
 * Целевые элементы – метод и тип, время жизни – RUNTIME.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    /**
     * Текстовое описание.
     * @return строка с описанием
     */
    String value();
}
