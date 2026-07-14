package lesson_4_generics_collections;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenCollMethodsTest {

    @Test
    void swapArray_Strings() {
        String[] array = {"a", "b", "c", "d"};
        GenCollMethods.swapArray(array, 0, 2);
        assertArrayEquals(new String[]{"c", "b", "a", "d"}, array);
    }

    @Test
    void swapArray_Integers() {
        Integer[] array = {1, 2, 3, 4};
        GenCollMethods.swapArray(array, 1, 3);
        assertArrayEquals(new Integer[]{1, 4, 3, 2}, array);
    }

    @Test
    void swapArray_SameIndex() {
        String[] array = {"x", "y", "z"};
        GenCollMethods.swapArray(array, 1, 1);
        assertArrayEquals(new String[]{"x", "y", "z"}, array);
    }

    @Test
    void swapArray_InvalidIndex() {
        String[] array = {"a", "b"};
        assertThrows(IndexOutOfBoundsException.class,
                () -> GenCollMethods.swapArray(array, 0, 5));
    }

    //--------------------------------------------

    @Test
    void convertArrayToList_Strings() {
        String[] array = {"one", "two", "three"};
        List<String> list = GenCollMethods.convertArrayToList(array);
        assertEquals(List.of("one", "two", "three"), list);
        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    void convertArrayToList_Empty() {
        String[] array = {};
        List<String> list = GenCollMethods.convertArrayToList(array);
        assertTrue(list.isEmpty());
        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    void convertArrayToList_Integers() {
        Integer[] array = {10, 20, 30};
        List<Integer> list = GenCollMethods.convertArrayToList(array);
        assertEquals(List.of(10, 20, 30), list);
    }

    //--------------------------------------------

    @Test
    void findDistinctValues_WithDuplicates() {
        String[] words = {
                "apple", "banana", "apple", "orange", "banana", "apple",
                "grape", "grape", "kiwi", "kiwi", "kiwi",
                "melon", "melon", "peach", "peach", "peach",
                "pear", "pear", "apple", "banana"
        };
        Map<String, Long> result = GenCollMethods.findDistinctValues(words);

        assertEquals(8, result.size());

        assertEquals(4L, result.get("apple"));
        assertEquals(3L, result.get("banana"));
        assertEquals(1L, result.get("orange"));
        assertEquals(2L, result.get("grape"));
        assertEquals(3L, result.get("kiwi"));
        assertEquals(2L, result.get("melon"));
        assertEquals(3L, result.get("peach"));
        assertEquals(2L, result.get("pear"));
    }

    @Test
    void findDistinctValues_AllSame() {
        String[] words = {"test", "test", "test"};
        Map<String, Long> result = GenCollMethods.findDistinctValues(words);
        assertEquals(1, result.size());
        assertEquals(3L, result.get("test"));
    }

    @Test
    void findDistinctValues_Empty() {
        String[] words = {};
        Map<String, Long> result = GenCollMethods.findDistinctValues(words);
        assertTrue(result.isEmpty());
    }
}