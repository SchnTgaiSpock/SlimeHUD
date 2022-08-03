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
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();

            if (args.length == 0) {
                sendInfo(player);
                return true;
            }

            // May add more to the command in the future
            switch (args[0]) {
                case "toggle":
                    boolean wailaOn = SlimeHUD.getInstance().getPlayerData().getBoolean(uuid + ".waila", true);
                    SlimeHUD.getInstance().getPlayerData().set(uuid + ".waila", !wailaOn);
                    SlimeHUD.getInstance().getPlayerData().save();
                    player.sendMessage("§a§lSlimeHUD§7> HUD toggled " + (wailaOn ? "§coff" : "§aon"));
                    return true;
            
                default:
                    break;
            }
        }

        return false;
    }

    private void sendInfo(Player player) {
        player.sendMessage(
            "",
            "§a§lSlimeHUD §7- §2Version " + SlimeHUD.getInstance().getPluginVersion(),
            "§7------",
            "§a§lWiki §7- §2https://github.com/SchnTgaiSpock/SlimeHUD/wiki",
            "§a§lIssues §7- §2https://github.com/SchnTgaiSpock/SlimeHUD/issues",
            ""
        );
    }
    
}
