package lesson_6_reflection_annotations.tests;

import lesson_6_reflection_annotations.annotations.After;
import lesson_6_reflection_annotations.annotations.Before;

public class EmptyTestClass {
    @Before
    public void before() {
        System.out.println(this.getClass().getName() + " @Before");
    }

    @After
    public void after() {
        System.out.println(this.getClass().getName() + " @After");
    }
}
