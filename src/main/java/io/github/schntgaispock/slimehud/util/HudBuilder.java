package io.github.schntgaispock.slimehud.util;

import java.text.NumberFormat;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing various formatting functions for your HUD text
 */
@UtilityClass
public class HudBuilder {

    private static NumberFormat nf = NumberFormat.getInstance();

    /**
     * Formats stored energy
     * 
     * @param energy Amount of stored energy
     * @return The formatted text
     */
    public static String formatEnergyStored(int energy) {
        return "&7| " + HudBuilder.getAbbreviatedNumber(energy) + " J Stored";
    }

    /**
     * Formats energy generated (per tick)
     * 
     * @param energy Energy generated per tick
     * @return The formatted text
     */
    public static String formatEnergyGenerated(int energy) {
        return "&7| Generating " + HudBuilder.getAbbreviatedNumber(energy) + " J/t";
    }

    /**
     * Returns an 11-char progress bar made of "|"s
     * 
     * @param progress Completed amount, should be less than {@code total}
     * @param total    Total amount
     * @return The formatted progress bar
     */
    public static String getProgressBar(int progress, int total) {
        if (total == 0) return "";
        return HudBuilder.getProgressBar(100 * progress / total);
    }

    /**
     * Returns an 11-char progress bar made of "|"s
     * 
     * @param percentCompleted An integer between 0 and 100, inclusive
     * @return The formatted progress bar
     */
    public static String getProgressBar(int percentCompleted) {
        // Clamp to [0, 100]
        percentCompleted = Math.min(Math.max(percentCompleted, 0), 100);

        // 11 bars total
        StringBuffer progressBar = new StringBuffer();
        if (percentCompleted > 0) {
            char color = '2';
            if (percentCompleted < 15) {
                color = '4';
            } else if (percentCompleted < 30) {
                color = 'c';
            } else if (percentCompleted < 45) {
                color = '6';
            } else if (percentCompleted < 60) {
                color = 'e';
            } else if (percentCompleted < 75) {
                color = 'a';
            }
            // Magic numbers
            int split = (percentCompleted + 4) / 10;
            progressBar.append("&").append(color);
            for (int i = 0; i < split + 1; i++) {
                progressBar.append("|");
            }
            progressBar.append("&7");
            for (int i = 0; i < 10 - split; i++) {
                progressBar.append("|");
            }

        } else {
            progressBar.append("|||||||||||");
        }

        return progressBar.toString();
    }

    /**
     * Returns a fully-formatted 11-char progress bar made of "|"s. If you want to
     * append or prepend text, you should use {@code HudBuilder.getProgressBar}
     * 
     * @param progress Completed amount, should be less than {@code total}
     * @param total    Total amount
     * @return The formatted progress bar
     */
    public static String formatProgressBar(int progress, int total) {
        if (total == 0) return "";
        return HudBuilder.formatProgressBar(100 * progress / total);
    }

    /**
     * Returns a fully-formatted 11-char progress bar made of "|"s. If you want to
     * append or prepend text, you should use {@code HudBuilder.getProgressBar}
     * 
     * @param percentCompleted An integer between 0 and 100, inclusive
     * @return The formatted progress bar
     */
    public static String formatProgressBar(int percentCompleted) {
        return "&7| " + HudBuilder.getProgressBar(percentCompleted) + " - " + percentCompleted + "%";
    }

    /**
     * Shortens numbers and appends K, M, B, etc...
     * <ul>
     *   <li>123 -> 123</li>
     *   <li>1337 -> 1.33K</li>
     *   <li>9671111 -> 9.67M</li>
     *   <li>2147483647 -> 2.14B</li>
     *   <li>314159265358979 -> 314.15T</li>
     * </ul>
     * Numbers will always be rounded down
     * @param n Number to shorten
     * @return Shortened number
     */
    public static String getAbbreviatedNumber(long n) {
        String original = ""+n;
        int length = original.length();
        String append = "";
        if (length < 4) {
            return original;
        } else if (length < 7) {
            append = "K";
        } else if (length < 10) {
            append = "M";
        } else if (length < 13) {
            append = "B";
        } else if (length < 16) {
            append = "T";
        } else if (length < 19) {
            append = "Qa";
        } else if (length < 22) {
            append = "Qi";
        }

        StringBuffer shortened = new StringBuffer();
        int leadingLength = ((length-1) % 3) + 1;
        int i;
        for (i = 0; i < leadingLength; i++) {
            shortened.append(original.charAt(i));
        }
        char dec1 = original.charAt(i);
        char dec2 = original.charAt(i+1);
        boolean temp;
        if ((temp = dec2 != '0') || dec1 != '0') {
            shortened.append('.').append(dec1);
            if (temp) {
                shortened.append(dec2);
            }
        }
        return shortened.append(append).toString();
        
    }

    /**
     * Adds commas to a number
     * @param n No commas?
     * @return Number with commas
     */
    public static String getCommaNumber(long n) {
        return HudBuilder.nf.format(n);
    }
}
