package lesson_16_structural_patterns.homework.src.main.java.ru.otus;

import java.util.List;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.handler.ComplexProcessor;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.ListenerPrinterConsole;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor.LoggerProcessor;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor.ProcessorConcatFields;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor.ProcessorUpperField10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
