package io.github.schntgaispock.slimehud.util;

import java.util.logging.Level;

import org.bukkit.boss.BarColor;

import io.github.schntgaispock.slimehud.SlimeHUD;

public class Util {
    
    public static BarColor pickBarColorFromName(String name) {
        char colorCode = name.trim().toLowerCase().startsWith("§") ? name.charAt(1) : ' ';
        switch (colorCode) {
            case '4':
            case 'c':
                return BarColor.RED;
            
            case '6':
            case 'e':
                return BarColor.YELLOW;

            case '2':
            case 'a':
                return BarColor.GREEN;

            case '3':
            case 'b':
                return BarColor.BLUE;

            case '1':
            case '5':
            case '9':
                return BarColor.PURPLE;
        
            case 'd':
                return BarColor.PINK;

            default:
                return BarColor.WHITE;
        }

    }

    public static BarColor pickBarColorFromColor(String color) {
        switch (color.trim()) {
            case "red":
            case "yellow":
            case "green":
            case "blue":
            case "purple":
            case "pink":
            case "white":
                return BarColor.valueOf(color.toUpperCase());

            case "default":
                return BarColor.WHITE;
        
            default:
                SlimeHUD.log(Level.WARNING, "[WAILA HUD] Invalid bossbar color: " + color, "[WAILA HUD] Setting color to white...");
                return BarColor.WHITE;
        }
    }


}
