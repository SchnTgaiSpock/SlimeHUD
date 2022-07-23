package io.github.schntgaispock.slimehud.waila;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;

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
        String json;
        String id;

        try {
            Block targetedBlock = getPlayer().getTargetBlockExact(5);
            json = BlockStorage.getBlockInfoAsJson(targetedBlock);
        } catch (NullPointerException e) {
            return "";
        }
        
        try {
            id = SlimeHUD.getInstance().getJsonMapper().readValue(json, new TypeReference<HashMap<String, String>>(){}).get("id");

            if (id == null) return "";
        } catch (JacksonException e) {
            e.printStackTrace();
            return "";
        }

        SlimefunItem item = SlimefunItem.getById(id);
        return (item == null) ? "" : item.getItemName();
    }

    @Override
    public void run() {
        if (!SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waila", true)) {
            WAILABar.setVisible(false);
            return;
        }

        String facing = getFacing();
        if (facing == previousFacing) {
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
        
            default:
                break;
        }
        
    }

}
