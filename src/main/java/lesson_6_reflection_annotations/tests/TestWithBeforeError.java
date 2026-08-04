package lesson_6_reflection_annotations.tests;

import lesson_6_reflection_annotations.annotations.After;
import lesson_6_reflection_annotations.annotations.Before;
import lesson_6_reflection_annotations.annotations.Description;
import lesson_6_reflection_annotations.annotations.Test;

public class TestWithBeforeError {

    @Before
    public void failingBefore() {
        throw new RuntimeException("Ошибка в @Before");
    }

    @Test
    public void testThatShouldBeSkipped() {
        System.out.println("Этот вывод не должен появиться");
    }

    @After
    public void after() {
        System.out.println("After не выполнится, потому что выполнение прервано.");
    }
}
