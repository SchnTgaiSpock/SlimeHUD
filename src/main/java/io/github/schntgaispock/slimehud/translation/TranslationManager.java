package io.github.schntgaispock.slimehud.translation;

import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.guizhanss.slimefuntranslation.api.SlimefunTranslationAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TranslationManager {
    private boolean translationEnabled;

    public TranslationManager() {
        if (SlimeHUD.getInstance().getConfig().getBoolean("options.slimefun-translation-support", true)) {
            if (Bukkit.getPluginManager().getPlugin("SlimefunTranslation") != null) {
                translationEnabled = true;
            } else {
                SlimeHUD.getInstance().getLogger().info("SlimefunTranslation is not installed and has been ignored.");
                translationEnabled = false;
            }
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getItemName(Player p, SlimefunItem sfItem) {
        if (!translationEnabled) {
            return sfItem.getItemName();
        }
        
        try {
            return SlimefunTranslationAPI.getItemName(SlimefunTranslationAPI.getUser(p), sfItem);
        } catch (NoClassDefFoundError e) {
            SlimeHUD.getInstance().getLogger().info("Could not get item translation! Please update SlimefunTranslation");
            translationEnabled = false;
            return sfItem.getItemName();
        }
    }
}
