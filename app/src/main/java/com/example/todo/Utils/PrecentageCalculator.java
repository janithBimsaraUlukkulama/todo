package com.example.todo.Utils;

import java.text.DecimalFormat;

public class PrecentageCalculator {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private double precent = 0;
    private double comAmount = 0;
    private double allAmount = 0;

    public double getPrsentages(int completed, int all) {
        this.comAmount = completed;
        this.allAmount = all;
        precent = (comAmount / allAmount) * 100;

        precent = precent > 0 ? precent : 0;
        
        return (Double.parseDouble(df.format(precent)));

    }
}
