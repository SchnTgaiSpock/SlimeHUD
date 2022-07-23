package io.github.schntgaispock.slimehud.command;

import io.github.schntgaispock.slimehud.SlimeHUD;

/**
 * Does all the necessary prep work for commands to work
 */
public class CommandManager {
    
    public static void setup() {
        
        SlimeHUD.getInstance().getCommand("slimehud").setExecutor(new SlimeHUDCommandExecutor());
        SlimeHUD.getInstance().getCommand("slimehud").setTabCompleter(new SlimeHUDTabCompleter());

    }

}
