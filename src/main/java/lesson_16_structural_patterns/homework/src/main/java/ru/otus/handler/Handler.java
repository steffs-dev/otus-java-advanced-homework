package lesson_16_structural_patterns.homework.src.main.java.ru.otus.handler;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.Listener;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
