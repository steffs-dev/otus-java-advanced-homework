package lesson_6_reflection_annotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, отмечающая метод, который должен быть выполнен после каждого тестового метода.
 * Методы, помеченные этой аннотацией, выполняются после каждого @Test-метода,
 * даже если тест упал. Используется для очистки ресурсов, закрытия соединений и т.п.
 * Целевой элемент – метод, время жизни – RUNTIME.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
}
