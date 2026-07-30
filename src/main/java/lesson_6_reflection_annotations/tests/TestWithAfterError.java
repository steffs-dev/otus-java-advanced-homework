package lesson_6_reflection_annotations.tests;

import lesson_6_reflection_annotations.annotations.After;
import lesson_6_reflection_annotations.annotations.Before;
import lesson_6_reflection_annotations.annotations.Description;
import lesson_6_reflection_annotations.annotations.Test;

public class TestWithAfterError {

    @Before
    public void setUp() {
        System.out.println("Before: подготовка");
    }

    @Test
    @Description("Тест, который проходит успешно, но @After падает")
    public void testPassing() {
        System.out.println("Внутри теста");
    }

    @After
    public void failingAfter() {
        throw new RuntimeException("Ошибка в @After, но тест всё равно успешен");
    }
}
