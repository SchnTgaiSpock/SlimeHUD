package io.github.schntgaispock.slimehud.waila;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.util.Util;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerWAILA extends BukkitRunnable {
    
    @Getter Player player;
    private String WAILALocation;
    private String previousFacing = "";

    @Getter BossBar WAILABar; // Bossbar

    private boolean automaticWAILABarColor;
    private boolean lastFacingWasVanilla;
    private boolean useOriginalColors;

    public PlayerWAILA(Player player) {
        this.WAILALocation = SlimeHUD.getInstance().getConfig().getString("waila.location");

        this.player = player;
        this.lastFacingWasVanilla = true;

        String bossbarColor = SlimeHUD.getInstance().getConfig().getString("waila.bossbar-color").trim();
        this.automaticWAILABarColor = bossbarColor.toLowerCase().equals("inherit");
        this.WAILABar = Bukkit.createBossBar("", Util.pickBarColorFromColor(bossbarColor), BarStyle.SOLID);
        WAILABar.addPlayer(player);
        WAILABar.setVisible(false);

        this.useOriginalColors = SlimeHUD.getInstance().getConfig().getBoolean("waila.use-original-colors");
    }

    /**
     * Returns an empty string if not a Slimefun item. Otherwise returns the formatted item name
     */
    public String getFacing() {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            return "";
        }
        Location target = targetBlock.getLocation();

        SlimefunItem item = BlockStorage.check(targetBlock);
        if (item == null) {
            return "";
        }

        HudRequest request = new HudRequest(item, target);
        String name = item.getItemName() + " ";
        String additionalString = SlimeHUD.getHudController().processRequest(item, request);

        return name + additionalString;
    }

    @Override
    public void run() {
        if (!SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waila", true)) {
            WAILABar.setVisible(false);
            return;
        }

        String facing = getFacing();
        if (facing.equals(previousFacing)) {
            return; // Nothing changed, skip for now
        }

        previousFacing = facing;
        switch (WAILALocation) {
            case "bossbar":
                if (facing.equals("")) {
                    lastFacingWasVanilla = true;
                    WAILABar.setVisible(false);
                    break;
                } else if (lastFacingWasVanilla) {
                    lastFacingWasVanilla = false;
                    WAILABar.setVisible(true);
                }

                WAILABar.setTitle(useOriginalColors ? facing : ChatColor.stripColor(facing));

                if (automaticWAILABarColor) {
                    WAILABar.setColor(Util.pickBarColorFromName(facing));
                }

                break;
            
            case "hotbar":
                getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(facing));
                break;
            default:
                break;
        }
        
    }

}
