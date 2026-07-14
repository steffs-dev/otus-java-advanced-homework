package lesson_4_generics_collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GenCollMethods {

    //Написать метод, который меняет два элемента массива местами (массив может
    // быть любого ссылочного типа);

    public static <T> void swapArray(T[] array, int index1, int index2) {
        if (array == null || index1 == index2) {
            return;
        }
        if (index1 < 0 || index2 < 0 || index1 > array.length - 1 || index2 > array.length - 1) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    //Написать метод, который преобразует массив в ArrayList;

    public static <T> List<T> convertArrayToList(T[] array) {
        if (array == null) { return new ArrayList<>(); }
        return new ArrayList<>(Arrays.asList(array));
    }

    //Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся).
    //Найти и вывести список уникальных слов, из которых состоит массив
    //(дубликаты не считаем). Посчитать, сколько раз встречается каждое слово.

    public static void printDistinctValues(String[] array) {
        if (array == null) { return; }
        Map<String, Long> map = findDistinctValues(array);
        System.out.println("Список уникальных слов: " + map.keySet());

        map.forEach((key, value) ->
                System.out.println(key + " встречается " + value + " раз"));
    }

    public static Map<String, Long> findDistinctValues(String[] array) {
        if (array == null) { return new HashMap<>(); }
        Map<String, Long> map = new HashMap<>();
        Arrays.stream(array).forEach(k -> {
            long count = map.getOrDefault(k, 0L) + 1L;
            map.put(k, count);
        });
        return map;
    }
}
