package io.github.schntgaispock.slimehud.waila;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;

import javax.annotation.ParametersAreNonnullByDefault;

public class HudRequest {
    private final SlimefunItem slimefunItem;
    private final Location location;

    @ParametersAreNonnullByDefault
    public HudRequest(SlimefunItem slimefunItem, Location location) {
        this.slimefunItem = slimefunItem;
        this.location = location;
    }

    public SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    public Location getLocation() {
        return location;
    }
}
