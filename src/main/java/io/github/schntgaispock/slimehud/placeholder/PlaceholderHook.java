package io.github.schntgaispock.slimehud.placeholder;

import io.github.schntgaispock.slimehud.SlimeHUD;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    @Override
    public @Nonnull String getIdentifier() {
        return "slimehud";
    }

    @Override
    public @Nonnull String getAuthor() {
        return "TheLittle_Yang";
    }

    @Override
    public @Nonnull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @Nonnull String params) {
        if (params.equalsIgnoreCase("toggle")) {
            return SlimeHUD.getInstance().getPlayerData().getString(player.getUniqueId() + ".waila", "true");
        } else {
            return null;
        }
    }
}
