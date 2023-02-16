package io.github.schntgaispock.slimehud.tests;

import java.util.LinkedHashMap;
import java.util.Map;

import io.github.schntgaispock.slimehud.util.HudBuilder;
import io.github.schntgaispock.slimehud.util.Util;

/**
 * Some really jank tests
 */
public class UtilTests {
    public static void main(String[] args) {
        Map<Long, String> numberTestCases1 = new LinkedHashMap<>();
        numberTestCases1.put(1l, "1");
        numberTestCases1.put(23l, "23");
        numberTestCases1.put(456l, "456");
        numberTestCases1.put(7890l, "7.89K");
        numberTestCases1.put(12345l, "12.34K");
        numberTestCases1.put(678901l, "678.9K");
        numberTestCases1.put(2345678l, "2.34M");
        numberTestCases1.put(90123456l, "90.12M");
        numberTestCases1.put(789012345l, "789.01M");
        numberTestCases1.put(6789012345l, "6.78B");
        numberTestCases1.put(67890123456l, "67.89B");
        numberTestCases1.put(789012345678l, "789.01B");
        numberTestCases1.put(9012345678901l, "9.01T");
        numberTestCases1.put(23456789012345l, "23.45T");
        numberTestCases1.put(678901234567890l, "678.9T");
        numberTestCases1.put(1234567890123456l, "1.23Qa");
        numberTestCases1.put(78901234567890123l, "78.9Qa");
        numberTestCases1.put(456789012345678901l, "456.78Qa");
        numberTestCases1.put(2345678901234567890l, "2.34Qi");
        numberTestCases1.put(-1l, "-1");
        numberTestCases1.put(-23l, "-23");
        numberTestCases1.put(-456l, "-456");
        numberTestCases1.put(-7890l, "-7.89K");
        numberTestCases1.put(-12345l, "-12.34K");
        numberTestCases1.put(-678901l, "-678.9K");
        numberTestCases1.put(-2345678l, "-2.34M");
        numberTestCases1.put(-90123456l, "-90.12M");
        numberTestCases1.put(-789012345l, "-789.01M");
        numberTestCases1.put(-6789012345l, "-6.78B");
        numberTestCases1.put(-67890123456l, "-67.89B");
        numberTestCases1.put(-789012345678l, "-789.01B");
        numberTestCases1.put(-9012345678901l, "-9.01T");
        numberTestCases1.put(-23456789012345l, "-23.45T");
        numberTestCases1.put(-678901234567890l, "-678.9T");
        numberTestCases1.put(-1234567890123456l, "-1.23Qa");
        numberTestCases1.put(-78901234567890123l, "-78.9Qa");
        numberTestCases1.put(-456789012345678901l, "-456.78Qa");
        numberTestCases1.put(-2345678901234567890l, "-2.34Qi");

        for (Map.Entry<Long, String> entry : numberTestCases1.entrySet()) {
            String result = HudBuilder.getAbbreviatedNumber(entry.getKey());
            boolean passed = result.equals(entry.getValue());
            System.out.println("[" + (passed ? "PASSED" : "FAILED") + "] Test Case: " +
                entry.getKey() + "; expected " + entry.getValue() + "; got " + result);
        }

        System.out.println(Util.pickBarColorFromName("§#b9b900"));
        System.out.println(Util.pickBarColorFromName("§#b9b9b5"));
    }
}
