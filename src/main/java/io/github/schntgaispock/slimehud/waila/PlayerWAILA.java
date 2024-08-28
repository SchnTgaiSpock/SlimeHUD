package io.github.schntgaispock.slimehud.waila;

import javax.annotation.Nonnull;

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

    final private @Nonnull
    @Getter Player player;
    final private @Getter BossBar WAILABar; // Bossbar
    final private String WAILALocation;
    final private boolean useAutoBossBarColor;
    final private boolean keepTextColors;
    /**
     * Returns an empty string if not a Slimefun item. Otherwise returns the
     * formatted item name
     */
    @Getter
    private String facing = "";
    @Getter
    private String facingBlock = "";
    @Getter
    private String facingBlockInfo = "";
    private String previousFacing = "";

    private @Getter boolean paused;

    public PlayerWAILA(@Nonnull Player player) {
        this.WAILALocation = SlimeHUD.getInstance().getConfig().getString("waila.location");

        this.player = player;

        String bossbarColor = SlimeHUD.getInstance().getConfig().getString("waila.bossbar-color").trim().toLowerCase();
        this.useAutoBossBarColor = bossbarColor.equals("inherit");
        this.WAILABar = Bukkit.createBossBar("", Util.pickBarColorFromColor(bossbarColor), BarStyle.SOLID);
        WAILABar.addPlayer(player);
        WAILABar.setVisible(false);

        this.keepTextColors = SlimeHUD.getInstance().getConfig().getBoolean("waila.use-original-colors");
    }

    /**
     * Called every <code>waila.tick-rate</code> ticks
     */
    @Override
    public void run() {
        updateFacing();

        if (isPaused()) {
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
                    WAILABar.setVisible(false);
                    break;
                } else {
                    WAILABar.setVisible(true);
                }

                WAILABar.setTitle(keepTextColors ? facing : ChatColor.stripColor(facing));

                if (useAutoBossBarColor) {
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

    private void updateFacing() {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            clearFacing();
            return;
        }

        SlimefunItem item = BlockStorage.check(targetBlock);
        if (item == null) {
            clearFacing();
            return;
        }

        Location target = targetBlock.getLocation();
        HudRequest request = new HudRequest(item, target, player);
        facingBlock = SlimeHUD.getTranslationManager().getItemName(player, item);
        facingBlockInfo = SlimeHUD.getHudController().processRequest(request);
        facing = ChatColor.translateAlternateColorCodes('&', facingBlock + (facingBlockInfo.isEmpty() ? "" : " &7| " + facingBlockInfo));
    }

    private void clearFacing() {
        facingBlock = "";
        facingBlockInfo = "";
        facing = "";
    }

    public void setPaused(boolean paused) {
        setVisible(!previousFacing.equals("") && !paused);
        this.paused = paused;
    }

    public PlayerWAILA setVisible(boolean visible) {
        WAILABar.setVisible(visible);
        return this;
    }

    @Override
    public int hashCode() {
        return getPlayer().hashCode();
    }

}
