package io.github.schntgaispock.slimehud.waila;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import io.github.schntgaispock.slimehud.SlimeHUD;
import lombok.Getter;

/**
 * Keeps track of all the online players and their HUDs
 */
public class WAILAManager implements Listener {

    private static WAILAManager instance;
    @Getter Map<UUID, BukkitTask> WAILAs;

    private WAILAManager() {
        WAILAs = new HashMap<>();
    }

    public static WAILAManager getInstance() {
        if (instance == null) return new WAILAManager();
        return instance;
    }

    private void generateWAILA(Player player) {
        WAILAs.put(player.getUniqueId(), (new PlayerWAILA(player)).runTaskTimer(
            SlimeHUD.getInstance(),
            0l,
            SlimeHUD.getInstance().getConfig().getLong("waila.tick-rate")
        ));
    }

    private void removeWAILA(Player player) {
        BukkitTask task = getWAILAs().remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    @EventHandler
    public void onPlayerJoin(@Nonnull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (
            // SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waila", true) &&
            !SlimeHUD.getInstance().getConfig().getList("waila.disabled-in").contains(player.getWorld()) &&
            !SlimeHUD.getInstance().getConfig().getBoolean("waila.disabled", false)
        ) {
            generateWAILA(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent e) {
        removeWAILA(e.getPlayer());
    }

    public static void setup() {
        Bukkit.getPluginManager().registerEvents(getInstance(), SlimeHUD.getInstance());
    }

}
