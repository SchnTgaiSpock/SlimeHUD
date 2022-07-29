package io.github.schntgaispock.slimehud.waila;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.entity.Player;


@AllArgsConstructor
@Getter
public class HudRequest {
    private final SlimefunItem slimefunItem;
    private final Location location;
    private final Player player;
}
