package lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.homework;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener.Listener;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.ObjectForMessage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryListener implements Listener, HistoryReader {

    Map<Long, Message> history = new ConcurrentHashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if(msg ==  null) {
            throw new UnsupportedOperationException();
        }
        history.put(msg.getId(), copyMessage(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id)).map(this::copyMessage);
    }

    private Message copyMessage(Message msg) {
        Message.Builder builder = msg.toBuilder();
        ObjectForMessage obj = msg.getField13();

        if(obj != null) {
            builder.field13(obj.copy());
        }
        return builder.build();
    }
}
