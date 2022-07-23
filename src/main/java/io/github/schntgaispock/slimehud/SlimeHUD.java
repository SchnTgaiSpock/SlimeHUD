package io.github.schntgaispock.slimehud;


import javax.annotation.Nonnull;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.NamespacedKey;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.core.AddonConfig;
import io.github.schntgaispock.slimehud.command.CommandManager;
import io.github.schntgaispock.slimehud.waila.WAILAManager;
import lombok.Getter;

public class SlimeHUD extends AbstractAddon {

    @Getter AddonConfig playerData;
    static SlimeHUD instance;

    public SlimeHUD() {
        super("SchnTgaiSpock", "SlimeHUD", "master", "options.auto-update");
    }

    public static SlimeHUD getInstance() {
        return instance;
    }

    @Override
    public void enable() {
        instance = this;

        getLogger().info("#=================================#");
        getLogger().info("#    SlimeHUD by SchnTgaiSpock    #");
        getLogger().info("#          Version: 1.0.0         #");
        getLogger().info("#=================================#");

        Metrics metrics = new Metrics(this, 15883);
        metrics.addCustomChart(
            new SimplePie("disabled", () -> {
                return "" + getConfig().getBoolean("waila.disabled");
            })
        );
        metrics.addCustomChart(
            new SimplePie("waila_location", () -> {
                return getConfig().getString("waila.location");
            })
        );

        playerData = new AddonConfig("player.yml");

        WAILAManager.setup();
        CommandManager.setup();
    }

    @Override
    public void disable() {
        instance = null;
        getPlayerData().save();
        getConfig().save();
    }

    public static NamespacedKey newNamespacedKey(@Nonnull String name) {
        return new NamespacedKey(SlimeHUD.getInstance(), name);
    }
}
