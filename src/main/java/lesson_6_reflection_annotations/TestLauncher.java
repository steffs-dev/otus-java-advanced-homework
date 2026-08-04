package lesson_6_reflection_annotations;

/**
 * Точка входа для запуска тестов.
 * Предоставляет статический метод {@link #launch(Class)}, который создаёт экземпляр
 * {@link LauncherFacade} и вызывает его метод {@link LauncherFacade#execute(Class)}.
 * Класс является упрощённым фасадом для пользователя.
 */
public class TestLauncher {

    /**
     * Запускает все тесты в указанном классе.
     *
     * @param testClass класс с тестами
     */
    public static void launch(Class<?> testClass) {
        LauncherFacade launcherFacade = new LauncherFacade();
        launcherFacade.execute(testClass);
    }
}
