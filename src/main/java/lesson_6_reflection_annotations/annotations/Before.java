package lesson_6_reflection_annotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, отмечающая метод, который должен быть выполнен перед каждым тестовым методом.
 * Методы, помеченные этой аннотацией, выполняются перед каждым @Test-методом
 * в рамках одного экземпляра тестового класса. Используется для инициализации состояния.
 * Целевой элемент – метод, время жизни – RUNTIME.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
}
