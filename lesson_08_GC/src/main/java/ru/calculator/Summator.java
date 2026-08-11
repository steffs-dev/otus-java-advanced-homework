package ru.calculator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>(100_000);
    private final Random random = new Random();
    private int listCounter = 0;

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        listCounter++;
        if(listCounter == 100_000){
            listValues.clear();
            listCounter = 0;
        }

        int value = data.getValue();
        sum += value + random.nextInt();

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;
        int intermValue = (sumLastThreeValues * sumLastThreeValues / (value + 1) - sum);
        int listSize = listValues.size();

        int some = someValue;
        some +=intermValue;
        some = Math.abs(some) + listSize;

        some +=intermValue;
        some = Math.abs(some) + listSize;

        some +=intermValue;
        some = Math.abs(some) + listSize;
        someValue = some;
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}