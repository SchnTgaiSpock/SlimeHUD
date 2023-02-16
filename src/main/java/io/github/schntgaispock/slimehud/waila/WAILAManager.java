package io.github.schntgaispock.slimehud.waila;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.schntgaispock.slimehud.SlimeHUD;
import lombok.Getter;

/**
 * Keeps track of all the online players and their HUDs
 */
public class WAILAManager implements Listener {

    private static WAILAManager instance;
    private @Getter Map<UUID, PlayerWAILA> wailas = new HashMap<>();

    private WAILAManager() {
    }

    public static WAILAManager getInstance() {
        if (instance == null)
            instance = new WAILAManager();
        return instance;
    }

    private void generateWAILA(@Nonnull Player player) {
        PlayerWAILA waila;
        if (!wailas.containsKey(player.getUniqueId())) {
            waila = new PlayerWAILA(player);
            waila.runTaskTimer(
                    SlimeHUD.getInstance(),
                    0l,
                    SlimeHUD.getInstance().getConfig().getLong("waila.tick-rate"));
            wailas.put(player.getUniqueId(), waila);
        } else {
            waila = wailas.get(player.getUniqueId());
        }
        waila.setPaused(
                !SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waila", true));
    }

    private void pauseWAILA(Player player) {
        PlayerWAILA waila = wailas.get(player.getUniqueId());
        if (waila != null)
            waila.setPaused(true);
    }

    private void removeWAILA(Player player) {
        PlayerWAILA waila = wailas.remove(player.getUniqueId());
        if (waila != null)
            waila.cancel();
    }

    @EventHandler
    public void onPlayerJoin(@Nonnull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.disabled", false) &&
                !SlimeHUD.getInstance().getConfig().getList("waila.disabled-in", Collections.EMPTY_LIST)
                        .contains(player.getWorld().getName())) {
            generateWAILA(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent e) {
        removeWAILA(e.getPlayer());
    }

    @EventHandler
    public void onPlayerChangeWorld(@Nonnull PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        if (SlimeHUD.getInstance().getConfig().getList("waila.disabled-in", Collections.EMPTY_LIST)
                .contains(player.getWorld().getName())) {
            pauseWAILA(player);
        } else {
            generateWAILA(player);
        }
    }

    public static void setup() {
        Bukkit.getPluginManager().registerEvents(getInstance(), SlimeHUD.getInstance());
    }

}
