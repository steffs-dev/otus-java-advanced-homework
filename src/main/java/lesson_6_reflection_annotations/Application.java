package lesson_6_reflection_annotations;

import lesson_6_reflection_annotations.tests.CalculatorTest;
import lesson_6_reflection_annotations.tests.EmptyTestClass;
import lesson_6_reflection_annotations.tests.TestWithAfterError;
import lesson_6_reflection_annotations.tests.TestWithBeforeError;

/**
 * Главный класс для демонстрации работы фреймворка.
 * Последовательно запускает несколько тестовых классов
 */
public class Application {
    public static void main(String[] args) {

        System.out.println("1. Обычный тест (CalculatorTest)");
        TestLauncher.launch(CalculatorTest.class);

        System.out.println("2. Пустой класс");
        TestLauncher.launch(EmptyTestClass.class);

        System.out.println("3. Ошибка в Before");
        TestLauncher.launch(TestWithBeforeError.class);

        System.out.println("4. Ошибка в After (не влияет на результат)");
        TestLauncher.launch(TestWithAfterError.class);
    }
}
