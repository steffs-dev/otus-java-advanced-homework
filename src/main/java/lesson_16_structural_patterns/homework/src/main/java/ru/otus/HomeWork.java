package lesson_16_structural_patterns.homework.src.main.java.ru.otus;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.handler.ComplexProcessor;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.ListenerPrinterConsole;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.homework.HistoryListener;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.ObjectForMessage;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeWork {

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new ProcessorSwapField11AndField12(),
                new ProcessorThrowExceptionOnEvenSecond());

        var complexProcessor = new ComplexProcessor(processors, ex -> log.info("Error while running processor: {}", ex.getMessage()));
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        ObjectForMessage forField13 = new ObjectForMessage();
        forField13.setData(new ArrayList<>(List.of("value for field13")));

        Message message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("value for field11")
                .field12("value for field12")
                .field13(forField13)
                .build();

        Message result = complexProcessor.handle(message);
        log.info("result:{}", result);

        if(result.getField13() != null){
            result.getField13().setData(new ArrayList<>(List.of("new value for field13")));
        }

        Optional<Message> fromHistory = historyListener.findMessageById(1L);

        log.info("fromHistory: {}", fromHistory.orElse(null));
    }
}
