package io.github.schntgaispock.slimehud.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.schntgaispock.slimehud.SlimeHUD;

/**
 * Functionality for the '/slimehud'command
 */
public class SlimeHUDCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            String uuid = ((Player) sender).getUniqueId().toString();

            // May add more to the command in the future
            switch (args[0]) {
                case "toggle":
                    boolean waimaOn = SlimeHUD.getInstance().getPlayerData().getBoolean(uuid + ".waima", true);
                    SlimeHUD.getInstance().getPlayerData().set(uuid + ".waima", !waimaOn);
                    return true;
            
                default:
                    break;
            }
        }

        return false;
    }
    
}
