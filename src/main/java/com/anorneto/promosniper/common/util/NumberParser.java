package com.anorneto.promosniper.common.util;

public class NumberParser {
    public static int parseNumber(String input) {
        double multiplier = 1.0;

        if (input.endsWith("K")) {
            multiplier = 1e3; // 1 thousand
            input = input.substring(0, input.length() - 1);
        } else if (input.endsWith("M")) {
            multiplier = 1e6; // 1 million
            input = input.substring(0, input.length() - 1);
        }

        double parsedDouble = Double.parseDouble(input) * multiplier;
        return (int) Math.floor(parsedDouble);
    }
}