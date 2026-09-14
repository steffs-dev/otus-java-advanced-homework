package lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor;


import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;

public class ProcessorSwapField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        String f11 = message.getField11();
        String f12 = message.getField12();
        return message.toBuilder()
                .field11(f12)
                .field12(f11)
                .build();
    }
}
