package lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class ProcessorThrowExceptionOnEvenSecond implements Processor{
    private final Supplier<LocalDateTime> dateTimeSupplier;

    public ProcessorThrowExceptionOnEvenSecond() {
        this(LocalDateTime::now);
    }

    public ProcessorThrowExceptionOnEvenSecond(Supplier<LocalDateTime> dateTimeSupplier) {
        this.dateTimeSupplier = dateTimeSupplier;
    }

    public ProcessorThrowExceptionOnEvenSecond(LocalDateTime fixedDateTime) {
        this.dateTimeSupplier = () -> fixedDateTime;
    }


    @Override
    public Message process(Message message) {
        int second = dateTimeSupplier.get().getSecond();

        if (second % 2 == 0) {
            throw new RuntimeException("Even second " + second);
        }
        return message;
    }
}
