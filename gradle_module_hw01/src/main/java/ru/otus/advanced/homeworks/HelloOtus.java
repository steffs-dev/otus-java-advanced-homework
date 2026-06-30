package ru.otus.advanced.homeworks;

import com.google.common.collect.*;

import java.util.*;

public class HelloOtus {

    public static <T> List<T> convertToImmutableList(Iterable<? extends T> input) {
        ImmutableList.Builder<T> immutableList = ImmutableList.builder();
        for (T el : input) {
            if (el != null) {
                immutableList.add(el);
            }
        }
        return immutableList.build();
    }

    public static void main(String[] args) {
        List<String> list = convertToImmutableList(Arrays.asList("Hello", " ", "World", "!"));
        list.forEach(System.out::print);

    }
}
