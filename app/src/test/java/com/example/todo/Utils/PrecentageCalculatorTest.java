package com.example.todo.Utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrecentageCalculatorTest {

    @Test
    void getPrsentages() {
        int completed = 5;
        int all = 10;
        double expected = 50;

        PrecentageCalculator precentageCalculator = new PrecentageCalculator();
        double output = precentageCalculator.getPrsentages(completed, all);

        assertEquals(expected, output);
    }

    //    all 0
    @Test
    void getPrsentagesAllZero() {
        int completed = 0;
        int all = 0;
        double expected = 0;

        PrecentageCalculator precentageCalculator = new PrecentageCalculator();
        double output = precentageCalculator.getPrsentages(completed, all);

        assertEquals(expected, output);
    }

}