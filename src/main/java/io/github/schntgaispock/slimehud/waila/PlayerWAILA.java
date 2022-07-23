package io.github.schntgaispock.slimehud.waila;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.util.Util;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerWAILA implements Runnable {
    
    @Getter Player player;

    private String previousFacing = "";

    @Getter BossBar WAILABar; // Only used by bossbar
    private boolean automaticWAILABarColor;

    private boolean lastFacingWasAir; // Only used by hotbar

    public PlayerWAILA(Player player) {
        this.player = player;
        this.lastFacingWasAir = true;

        String bossbarColor = SlimeHUD.getInstance().getConfig().getString("waila.bossbar-color").trim();
        this.automaticWAILABarColor = bossbarColor.toLowerCase().equals("inherit");
        this.WAILABar = Bukkit.createBossBar("", Util.pickBarColorFromColor(bossbarColor), BarStyle.SOLID);
        WAILABar.addPlayer(player);
        WAILABar.setVisible(false);
    }

    public String getFacing() {
        Block targetedBlock = getPlayer().getTargetBlock(null, 5);
        Optional<String> blockData = Slimefun.getBlockDataService().getBlockData(targetedBlock);

        System.out.println("blockData: " + blockData);

        if (blockData.isEmpty()) {
            return "";
        }

        return blockData.get();
    }

    @Override
    public void run() {
        String facing = getFacing();
        if (facing == previousFacing) {
            facing = previousFacing;
            return; // Nothing changed, skip for now
        }
        
        switch (SlimeHUD.getInstance().getConfig().getString("waila.location")) {
            case "bossbar":
                if (facing.equals("")) {
                    lastFacingWasAir = true;
                    WAILABar.setVisible(false);
                    break;

                } else if (lastFacingWasAir) {
                    lastFacingWasAir = false;
                    WAILABar.setVisible(true);
                }

                WAILABar.setTitle(facing);

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
