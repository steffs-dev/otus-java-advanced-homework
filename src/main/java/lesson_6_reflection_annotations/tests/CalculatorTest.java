package lesson_6_reflection_annotations.tests;

import lesson_6_reflection_annotations.annotations.*;

@Description("Класс с арифметическими тестами")
public class CalculatorTest {

    private int value;

    @Before
    public void setUp() {
        value = 10;
        System.out.println("Before: value = " + value);
    }

    @After
    public void tearDown() {
        System.out.printf("After: value = %d\n\n", value);
    }

    @Test
    @Description("Проверка сложения")
    public void testAddition() {
        value += 5;
        if (value != 15) {
            throw new AssertionError("Сложение не работает");
        }
    }

    @Test (name = "тест на вычитание")
    @Description("Проверка вычитания (упадёт)")
    public void testSubtraction() {
        value -= 3;
        if (value != 7) {
            throw new AssertionError("Вычитание не работает");
        }
        // Исключение, чтобы тест упал
        if (true) throw new RuntimeException("Искусственная ошибка");
    }

    @Test
    @Description("")
    public void testFactorial() {
        long res = 2_432_902_008_176_640_000L;
        long test = 1;
        for (int i = 1; i <= 20; i++) {
            test *= i;
        }
        if (test != res) {
            throw new AssertionError("Умножение не работает");
        }
    }
}