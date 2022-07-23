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

import io.github.schntgaispock.slimehud.SlimeHUD;

/**
 * Keeps track of all the online players and their HUDs
 */
public class WAILAManager implements Listener {

    private static WAILAManager instance;
    private Map<UUID, PlayerWAILA> WAILAs = new HashMap<>();

    private WAILAManager() {}

    public static WAILAManager getgetInstance() {
        if (instance == null) return new WAILAManager();
        return instance;
    }

    public synchronized Map<UUID, PlayerWAILA> getWAILAs() {
        return this.WAILAs;
    }

    @EventHandler
    public void onPlayerJoin(@Nonnull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (
            SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waima", true) &&
            !SlimeHUD.getInstance().getConfig().getList("waila.disabled-in").contains(player.getWorld()) &&
            !SlimeHUD.getInstance().getConfig().getBoolean("waila.disabled", false)
        ) {
            PlayerWAILA WAILA = new PlayerWAILA(player);
            getWAILAs().put(e.getPlayer().getUniqueId(), WAILA);
            SlimeHUD.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(
                SlimeHUD.getInstance(), WAILA, 0l, SlimeHUD.getInstance().getConfig().getLong("waila.tick-rate")
            );
        }

        System.out.println(getWAILAs().keySet().toString());
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent e) {
        getWAILAs().remove(e.getPlayer().getUniqueId());

        System.out.println(getWAILAs().keySet().toString());
    }

    public static void setup() {
        Bukkit.getPluginManager().registerEvents(getgetInstance(), SlimeHUD.getInstance());
    }

}
