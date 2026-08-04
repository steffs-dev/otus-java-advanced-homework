package lesson_6_reflection_annotations;

import lesson_6_reflection_annotations.annotations.After;
import lesson_6_reflection_annotations.annotations.Before;
import lesson_6_reflection_annotations.annotations.Test;
import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.events.StatisticsEvent;
import lesson_6_reflection_annotations.listener.ConsoleListener;
import lesson_6_reflection_annotations.listener.LogListener;
import lesson_6_reflection_annotations.utils.NotificationManager;
import lesson_6_reflection_annotations.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Основной класс-фасад, управляющий запуском всех тестов в указанном классе.
 * Реализует многопоточное выполнение тестов (каждая группа Before - Test - After в отдельном потоке).
 * Алгоритм работы:
 * <ol>
 *     <li>Извлекает методы с аннотациями {@code @Before}, {@code @Test}, {@code @After}.</li>
 *     <li>Если тестов нет – уведомляет слушателей и завершает работу.</li>
 *     <li>Отправляет уведомление {@code beforeTests()} один раз.</li>
 *     <li>Для каждого тестового метода создаёт отдельный экземпляр класса и запускает его
 *         в пуле потоков через {@link TestExecutor}.</li>
 *     <li>Собирает статистику (успех/провал, общая длительность) в потокобезопасный
 *         {@link StatisticsConsolidator}.</li>
 *     <li>Ожидает завершения всех тестов (с таймаутом).</li>
 *     <li>Формирует {@link StatisticsEvent} и уведомляет слушателей.</li>
 *     <li>Закрывает зарегистрированные ресурсы (файловый канал, пул потоков).</li>
 * </ol>
 */
public class LauncherFacade {

    private final NotificationManager notificationManager = new NotificationManager();
    private final StatisticsConsolidator statisticsConsolidator = new StatisticsConsolidator();
    private final ResourceManager resourceManager = new ResourceManager();
    private final ExecutorService executorService;
    private static final Logger log = LogManager.getLogger(LauncherFacade.class);

    /**
     * Конструктор, регистрирующий слушателей (консоль, файл) и создающий пул потоков.
     * Размер пула равен количеству зарегистрированных слушателей.
     * Также регистрирует ресурсы для автоматического закрытия.
     */
    public LauncherFacade() {
        ConsoleListener consoleListener = new ConsoleListener();
        LogListener logListener = new LogListener("src/main/java/lesson_6_reflection_annotations/log");
        notificationManager.registerListener(consoleListener);
        notificationManager.registerListener(logListener);
        executorService = Executors.newFixedThreadPool(notificationManager.numberOfListeners());
        resourceManager.register(logListener);
        resourceManager.register(executorService);
    }

    /**
     * Запускает все тесты в указанном классе.
     *
     * @param testClass класс с тестами
     */
    public void execute(Class<?> testClass) {
        List<Method> beforeMethods = AnnotationExtractor.extractMethods(testClass, Before.class);
        List<Method> testMethods = AnnotationExtractor.extractMethods(testClass, Test.class);
        List<Method> afterMethods = AnnotationExtractor.extractMethods(testClass, After.class);

        // Если нет тестовых методов – уведомляет и выходит

        if (testMethods.isEmpty()) {
            ServiceEvent event = new ServiceEvent.Builder(testClass.getSimpleName())
                    .description(AnnotationProcessorUtil.descriptionClass(testClass))
                    .build();
            notificationManager.notifyOnEmptyTestsList(event);
            return;
        }

        // Одно уведомление о старте всех тестов

        notificationManager.notifyBefore();

        // Запуск каждого теста в отдельной задаче

        for (Method testMethod : testMethods) {
            executorService.submit(() -> {
                try {
                    Object testInstance = TestInstanceFactory.newInstance(testClass);
                    TestExecutor testExecutor = new TestExecutor(notificationManager);

                    ServiceEvent testResultEvent = testExecutor.execute(testInstance,
                            testMethod, beforeMethods, afterMethods);

                    statisticsConsolidator.add(testResultEvent);
                } catch (Exception e) {
                    // При ошибке создания экземпляра тест считается проваленным
                    ServiceEvent exceptionEvent = new ServiceEvent.Builder(AnnotationProcessorUtil.testName(testMethod))
                            .description(AnnotationProcessorUtil.descriptionMethod(testMethod))
                            .status(Status.FAILURE)
                            .throwable(new RuntimeException("Ошибка при создании тестового класса"))
                            .build();
                    notificationManager.notifyFailure(exceptionEvent);
                    statisticsConsolidator.add(exceptionEvent);
                    log.error("Ошибка при выполнении теста {}: {}", testMethod.getName(), e.getStackTrace());
                }
            });
        }

        // Ожидание завершения всех задач с таймаутом 10 секунд

        shutdown();

        // Формирование и отправка итоговой статистики

        StatisticsEvent statisticsEvent = new StatisticsEvent.Builder(testClass.getSimpleName())
                .description(AnnotationProcessorUtil.descriptionClass(testClass))
                .status(Status.AFTER)
                .duration(statisticsConsolidator.getTotalDuration())
                .totalTests(statisticsConsolidator.getTotalTests())
                .passedTests(statisticsConsolidator.getCount(Status.SUCCESS))
                .failedTests(statisticsConsolidator.getCount(Status.FAILURE))
                .build();
        notificationManager.notifyAfter(statisticsEvent);

        // Закрытие ресурсов

        resourceManager.close();
    }

    /**
     * Завершает работу пула потоков с ожиданием выполнения всех задач.
     * Сначала инициирует плавное завершение ({@link ExecutorService#shutdown()}),
     * затем ждёт до 10 секунд. Если задачи не успели завершиться, то принудительно прерывает.
     */
    private void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                List<Runnable> tryList = executorService.shutdownNow();
                log.warn(tryList.size() + " тестов принудительно завершены в try блоке");
            }
        } catch (Exception e) {
            List<Runnable> catchList = executorService.shutdownNow();
            log.warn(catchList.size() + " тестов принудительно завершены в catch блоке");
            Thread.currentThread().interrupt();
        }
    }
}
