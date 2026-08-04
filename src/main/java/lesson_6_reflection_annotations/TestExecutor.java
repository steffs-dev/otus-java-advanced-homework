package lesson_6_reflection_annotations;

import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.utils.NotificationManager;
import lesson_6_reflection_annotations.utils.AnnotationProcessorUtil;
import lesson_6_reflection_annotations.utils.MethodInvoker;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Исполнитель одного тестового метода с его жизненным циклом (Before -> Test -> After).
 * Для каждого теста создаётся отдельный экземпляр (через фабрику), и этот класс
 * управляет вызовом методов с аннотациями, уведомляя слушателей через {@link NotificationManager}.
 * Важно: ошибки в @Before и @After не влияют на статус самого теста, если только
 * не приводят к невозможности выполнения теста (в случае @Before). Ошибки в @After
 * логируются, но не меняют итоговый статус теста.
 */
public class TestExecutor {

    private final NotificationManager notificationManager;

    /**
     * Создаёт исполнитель с указанным менеджером уведомлений.
     *
     * @param notificationManager менеджер для отправки событий слушателям
     */
    public TestExecutor(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    /**
     * Выполняет тест с полным жизненным циклом.
     *
     * @param testInstance  экземпляр тестового класса
     * @param testMethod    метод, помеченный @Test
     * @param beforeMethods список методов @Before (может быть пустым)
     * @param afterMethods  список методов @After (может быть пустым)
     * @return событие ServiceEvent с результатом выполнения (SUCCESS или FAILURE)
     */
    public ServiceEvent execute(Object testInstance, Method testMethod,
                                List<Method> beforeMethods, List<Method> afterMethods) {

        String name = AnnotationProcessorUtil.testName(testMethod);
        String description = AnnotationProcessorUtil.descriptionMethod(testMethod);

        // Уведомление о старте

        ServiceEvent startEvent = new ServiceEvent.Builder(name)
                .description(description)
                .status(Status.START)
                .build();
        notificationManager.notifyStart(startEvent);

        // Выполнение @Before

        for (Method method : beforeMethods) {
            if (!MethodInvoker.invokeMethod(testInstance, method)) {
                ServiceEvent beforeEvent = createFailureEvent(name, description,
                        "Ошибка в @Before");
                notificationManager.notifyFailure(beforeEvent);
                return beforeEvent;
            }
        }

        // Выполнение самого теста

        ServiceEvent testEvent = runTest(testInstance, testMethod);
        if (testEvent.getStatus().equals(Status.SUCCESS)) {
            notificationManager.notifySuccess(testEvent);
        } else {
            notificationManager.notifyFailure(testEvent);
        }

        // Выполнение @After (ошибки не меняют статус теста, но отправляются уведомления)

        for (Method method : afterMethods) {
            if (!MethodInvoker.invokeMethod(testInstance, method)) {
                notificationManager.notifyFailure(createFailureEvent(name, description,
                        "Ошибка в @After"));
            }
        }

        return testEvent;
    }

    /**
     * Создаёт событие со статусом FAILURE и указанным сообщением.
     *
     * @param name        имя теста
     * @param description описание теста
     * @param message     сообщение об ошибке
     * @return событие ServiceEvent
     */
    private ServiceEvent createFailureEvent(String name, String description,
                                            String message) {
        return new ServiceEvent.Builder(name)
                .description(description)
                .status(Status.FAILURE)
                .throwable(new RuntimeException(message))
                .build();
    }

    /**
     * Выполняет сам тестовый метод, измеряет время, возвращает событие с результатом.
     *
     * @param testInstance экземпляр
     * @param method       тестовый метод
     * @return ServiceEvent с SUCCESS или FAILURE
     */
    private ServiceEvent runTest(Object testInstance, Method method) {
        String name = AnnotationProcessorUtil.testName(method);
        String description = AnnotationProcessorUtil.descriptionMethod(method);
        long startTime = System.nanoTime();

        try {
            method.setAccessible(true);
            method.invoke(testInstance);

            long duration = System.nanoTime() - startTime;

            return new ServiceEvent.Builder(name)
                    .description(description)
                    .status(Status.SUCCESS)
                    .duration(Duration.ofNanos(duration))
                    .build();
        } catch (Exception e) {
            return createFailureEvent(name, description, Arrays.toString(e.getStackTrace()));
        }
    }
}
