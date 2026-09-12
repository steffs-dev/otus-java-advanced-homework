package lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.homework;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.Listener;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;

import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    @Override
    public void onUpdated(Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        throw new UnsupportedOperationException();
    }
}
