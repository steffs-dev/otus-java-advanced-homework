package lesson_16_structural_patterns.homework.src.main.java.ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage copy() {
        ObjectForMessage copy = new ObjectForMessage();

        if (data != null) {
            copy.setData(new ArrayList<>(data));
        }
        return copy;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
