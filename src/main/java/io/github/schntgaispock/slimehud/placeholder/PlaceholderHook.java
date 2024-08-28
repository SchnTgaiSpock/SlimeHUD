package io.github.schntgaispock.slimehud.placeholder;

import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.waila.PlayerWAILA;
import io.github.schntgaispock.slimehud.waila.WAILAManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    private final WAILAManager wailaManager = WAILAManager.getInstance();

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
        } else if (params.startsWith("hud")) {
            String[] split = params.split("_");
            PlayerWAILA playerWAILA = wailaManager.getWailas().get(player.getUniqueId());
            switch (split.length) {
                case 1 -> {
                    return playerWAILA.getFacing();
                }
                case 2 -> {
                    return playerWAILA.getFacingBlock();
                }
                case 3 -> {
                    return playerWAILA.getFacingBlockInfo();
                }
            }
        }

        return null;
    }
}
