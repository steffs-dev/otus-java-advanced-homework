package ru.otus.advanced.homeworks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HelloOtusTest<T> {

    public static Stream<Arguments> listOfArguments() {
        List<Arguments> listOfArguments = new ArrayList<>();
        listOfArguments.add(Arguments.of(
                new HashSet<>(Arrays.asList(1, null, 2, 3)),
                List.of(1, 2, 3)
        ));
        listOfArguments.add(Arguments.of(
                new PriorityQueue<String>() {{
                    add("word1");
                    add("word2");
                    add("word3");
                }},
                List.of("word1", "word2", "word3")
        ));
        listOfArguments.add(Arguments.of(
                new LinkedList<Double>() {{
                    add(1.1);
                    add(2.2);
                    add(3.3);
                }},
                List.of(1.1, 2.2, 3.3)
        ));
        return listOfArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("listOfArguments")
    void convertToImmutableList(Iterable<? extends T> input, List<? super T> expected) {
        assertEquals(expected, HelloOtus.convertToImmutableList(input));
    }
}