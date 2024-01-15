package com.anorneto.promosniper.common.util;

public class NumberParser {
    public static int parseNumber(String input) {
        double multiplier = 1.0;

        if (input.endsWith("K")) {
            // 1 thousand
            multiplier = 1e3;
            input = input.substring(0, input.length() - 1);
        } else if (input.endsWith("M")) {
            // 1 million
            multiplier = 1e6;
            input = input.substring(0, input.length() - 1);
        }

        double parsedDouble = Double.parseDouble(input) * multiplier;
        return (int) Math.floor(parsedDouble);
    }
}