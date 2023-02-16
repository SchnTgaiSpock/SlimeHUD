package io.github.schntgaispock.slimehud.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.boss.BarColor;

import io.github.schntgaispock.slimehud.SlimeHUD;
import lombok.Data;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class Util {
    @Data
    private class RGB {
        private final int red;
        private final int green;
        private final int blue;

        public int[] asArray() {
            return new int[] {red, green, blue};
        }
    }

    private static HashMap<RGB, BarColor> barColorRGBMap = new HashMap<>();
    private static HashMap<RGB, BarColor> savedBarColors = new HashMap<>();

    static {
        barColorRGBMap.put(new RGB(0x00, 0xb9, 0xec), BarColor.BLUE);
        barColorRGBMap.put(new RGB(0x16, 0xb9, 0x00), BarColor.GREEN);
        barColorRGBMap.put(new RGB(0xb9, 0x00, 0x90), BarColor.PINK);
        barColorRGBMap.put(new RGB(0x61, 0x00, 0xb9), BarColor.PURPLE);
        barColorRGBMap.put(new RGB(0xb9, 0x2a, 0x00), BarColor.RED);
        barColorRGBMap.put(new RGB(0x00, 0x00, 0x00), BarColor.WHITE);
        barColorRGBMap.put(new RGB(0xb9, 0xb9, 0x00), BarColor.YELLOW);
    }
    
    public static BarColor pickBarColorFromName(String name) {
        char colorCode = name.trim().toLowerCase().startsWith("§") ? name.charAt(1) : ' ';
        if (colorCode == 'x') {
            try {
                final String stripped = name.replace("§", "");
                final int red = Integer.parseInt(stripped, 1, 3, 16);
                final int green = Integer.parseInt(stripped, 3, 5, 16);
                final int blue = Integer.parseInt(stripped, 5, 7, 16);

                final RGB rgb = new RGB(red, green, blue);

                if (savedBarColors.containsKey(rgb)) {
                    return savedBarColors.get(rgb);
                } 

                BarColor color = barColorRGBMap.get(Collections.min(barColorRGBMap.keySet(), (RGB a, RGB b) -> {
                    return (errorSquared(a.asArray(), rgb.asArray()) < errorSquared(b.asArray(), rgb.asArray())) ? -1 : 1;
                }));

                savedBarColors.put(rgb, color);
                return color;

            } catch (NumberFormatException e) {
                return BarColor.WHITE;
            }
        }

        return switch (colorCode) {
            case '4', 'c' ->  BarColor.RED;
            case '6', 'e' -> BarColor.YELLOW;
            case '2', 'a' -> BarColor.GREEN;
            case '3', 'b' -> BarColor.BLUE;
            case '1', '5', '9' -> BarColor.PURPLE;
            case 'd' -> BarColor.PINK;
            default -> BarColor.WHITE;
        };
    }

    public static BarColor pickBarColorFromColor(String color) {
        switch (color.trim()) {
            case "red", "yellow", "green", "blue", "purple", "pink", "white":
                return BarColor.valueOf(color.toUpperCase());

            case "default", "inherit":
                return BarColor.WHITE;
        
            default:
                SlimeHUD.log(Level.WARNING, "[SlimeHUD] Invalid bossbar color: " + color, "[SlimeHUD] Setting color to white...");
                return BarColor.WHITE;
        }
    }

    public static ChatColor getColorFromCargoChannel(int channel) {
        switch (channel) {
            case 1:
                return ChatColor.WHITE;
            case 2:
                return ChatColor.GOLD;
            case 3:
                return ChatColor.BLUE; // No magenta
            case 4:
                return ChatColor.AQUA;
            case 5:
                return ChatColor.YELLOW;
            case 6:
                return ChatColor.GREEN;
            case 7:
                return ChatColor.LIGHT_PURPLE;
            case 8:
                return ChatColor.DARK_GRAY;
            case 9:
                return ChatColor.GRAY;
            case 10:
                return ChatColor.DARK_AQUA;
            case 11:
                return ChatColor.DARK_PURPLE;
            case 12:
                return ChatColor.DARK_BLUE;
            case 13:
                return ChatColor.RED; // No brown
            case 14:
                return ChatColor.DARK_GREEN;
            case 15:
                return ChatColor.DARK_RED;
            case 16:
                return ChatColor.BLACK;
        
            default:
                return ChatColor.WHITE;
        }
    }

    public static int errorSquared(int[] a, int[] b) {
        if (a.length != b.length) return Integer.MAX_VALUE;

        int total = 0;
        for (int i = 0; i < a.length; i++) {
            total += (int) Math.pow(a[i] - b[i], 2);
        }

        return total;
    }

}
