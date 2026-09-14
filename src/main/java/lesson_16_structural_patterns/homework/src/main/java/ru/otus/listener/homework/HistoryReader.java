package lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.homework;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
