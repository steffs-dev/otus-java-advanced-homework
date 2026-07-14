package lesson_4_generics_collections;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenCollMethods {
    //Написать метод, который меняет два элемента массива местами (массив может
    // быть любого ссылочного типа);
    public static <T> void swapArray(T[] array, int index1, int index2) {
        if (index1 < 0 || index2 < 0 || index1 > array.length - 1 || index2 > array.length - 1) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (index1 == index2) {
            return;
        }

        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    //Написать метод, который преобразует массив в ArrayList;
    public static <T> List<T> convertArrayToList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
    //Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся).
    //Найти и вывести список уникальных слов, из которых состоит массив
    //(дубликаты не считаем). Посчитать, сколько раз встречается каждое слово.
    public static Map<String, Long> findDistinctValues(String[] array) {
        Map<String, Long> map = Arrays.stream(array)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println("Список уникальных слов: " + map.keySet());

        map.forEach((key, value) ->
                System.out.println(key + " встречается " + value + " раз"));
        return map;
    }
}
