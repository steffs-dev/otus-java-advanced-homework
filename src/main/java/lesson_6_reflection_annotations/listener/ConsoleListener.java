package lesson_6_reflection_annotations.listener;

import lesson_6_reflection_annotations.events.Event;
import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.events.StatisticsEvent;

import java.util.Arrays;

/**
 * Слушатель, выводящий информацию о тестировании в консоль (System.out).
 * Использует форматированный вывод, показывает старт, завершение, успех/провал
 * с указанием времени и причины ошибки.
 */
public class ConsoleListener implements TestListener {
    @Override
    public void beforeTests() {
        System.out.println("-------ЗАПУСК ТЕСТИРОВАНИЯ-------");
    }


    @Override
    public void onEmptyTestsList(ServiceEvent event) {
        System.out.printf("В классе %s нет методов, помеченных аннотацией @Test\n",
                parseDescription(event));
    }

    @Override
    public void onTestStart(ServiceEvent event) {
        System.out.printf("Начало выполнения теста: %s\n", parseDescription(event));
    }

    @Override
    public void onTestSuccess(ServiceEvent event) {
        System.out.printf("Тест %s пройден за %d мс\n",
                parseDescription(event), event.getDuration().toMillis());
    }

    @Override
    public void onTestFailure(ServiceEvent event) {
        System.out.printf("Тест %s провален. Причина: %s\n",
                parseDescription(event), Arrays.toString(event.getThrowable().getStackTrace()));
    }

    @Override
    public void afterTests(StatisticsEvent event) {
        System.out.printf("""
                        -------ТЕСТИРОВАНИЕ ЗАВЕРШЕНО-------
                        Статистика выполнения тестов (класс: %s):
                        Общее количество тестов: %d, из них:
                        - тестов пройдено: %d;
                        - тестов провалено: %d.
                        
                        """,
                parseDescription(event),
                event.getTotalTests(),
                event.getPassedTests(), event.getFailedTests()
        );
    }

    /**
     * Формирует строку для отображения: если есть описание, добавляет его в скобках.
     *
     * @param event событие
     * @return строка вида "имя (описание)" или просто имя
     */
    private String parseDescription(Event event) {
        return (event.getDescription() == null) ? event.getName()
                : event.getName() + " (" + event.getDescription() + ")";
    }
}
