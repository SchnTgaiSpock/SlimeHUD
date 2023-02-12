package io.github.schntgaispock.slimehud.waila;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
@Getter
public class HudRequest {
    private final @Nonnull SlimefunItem slimefunItem;
    private final @Nonnull Location location;
    private final @Nonnull Player player;
}
