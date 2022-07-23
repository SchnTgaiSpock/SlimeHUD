package io.github.schntgaispock.slimehud.waila;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.util.Util;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoConnectorNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyConnector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerWAILA extends BukkitRunnable {
    
    @Getter Player player;
    private String WAILALocation;
    private String previousFacing = "";

    @Getter BossBar WAILABar; // Bossbar

    private boolean automaticWAILABarColor;
    private boolean lastFacingWasVanilla;
    private boolean useOriginalColors;

    public PlayerWAILA(Player player) {
        this.WAILALocation = SlimeHUD.getInstance().getConfig().getString("waila.location");

        this.player = player;
        this.lastFacingWasVanilla = true;

        String bossbarColor = SlimeHUD.getInstance().getConfig().getString("waila.bossbar-color").trim();
        this.automaticWAILABarColor = bossbarColor.toLowerCase().equals("inherit");
        this.WAILABar = Bukkit.createBossBar("", Util.pickBarColorFromColor(bossbarColor), BarStyle.SOLID);
        WAILABar.addPlayer(player);
        WAILABar.setVisible(false);

        this.useOriginalColors = SlimeHUD.getInstance().getConfig().getBoolean("waila.use-original-colors");
    }

    /**
     * Returns an empty string if not a Slimefun item. Otherwise returns the formatted item name
     */
    public String getFacing() {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            return "";
        }
        Location target = targetBlock.getLocation();

        SlimefunItem item = BlockStorage.check(targetBlock);
        if (item == null) {
            return "";
        }

        StringBuffer name = new StringBuffer(item.getItemName());
        if ( // Is an Energy Regulator or Energy Connector
            (item instanceof EnergyRegulator ||
            item instanceof EnergyConnector) && 
            SlimeHUD.getInstance().getConfig().getBoolean("waila.show-energy-size")
        ) {
            Network en = EnergyNet.getNetworkFromLocation(target);

            if (en != null) {
                try {
                    Field con = Network.class.getDeclaredField("connectorNodes");
                    Field ter = Network.class.getDeclaredField("terminusNodes");

                    con.setAccessible(true);
                    ter.setAccessible(true);

                    name.append(" §7| Network Size: ").append(
                        ((Set<?>) con.get(en)).size() +
                        ((Set<?>) ter.get(en)).size() + 1
                    );

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } else if ( // Is a Generator, Capacitor, or Machine
            item instanceof EnergyNetComponent &&
            SlimeHUD.getInstance().getConfig().getBoolean("waila.show-energy-stored")
        ) {
            EnergyNetComponent enc = (EnergyNetComponent) item;
            switch (enc.getEnergyComponentType()) {
                case CAPACITOR:
                case GENERATOR:
                case CONSUMER:
                    if (enc.getCapacity() <= 0) break;
                    name.append(" §7| ").append(enc.getCharge(target)).append("J Stored");
                    break;
            
                default:
                    break;
            }
        } else if ( // Is a Cargo Input/Output/Advanced Output Node
            item instanceof CargoNode &&
            SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-channel")
        ) {
            CargoNode cn = (CargoNode) item;
            int channel = cn.getSelectedChannel(targetBlock) + 1;
            name.append(" §7| Channel: ").append(Util.getColorFromCargoChannel(channel).toString()).append(channel);
        } else if ( // Is a Cargo Connector Node or Cargo Manager
            (item instanceof CargoConnectorNode ||
            item instanceof CargoManager) &&
            SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-size")
        ) {
            Network en = CargoNet.getNetworkFromLocation(target);

            if (en != null) {            
                try {
                    Field con = Network.class.getDeclaredField("connectorNodes");
                    Field ter = Network.class.getDeclaredField("terminusNodes");

                    con.setAccessible(true);
                    ter.setAccessible(true);

                    name.append(" §7| Network Size: ").append(
                        ((Set<?>) con.get(en)).size() +
                        ((Set<?>) ter.get(en)).size() + 1
                    );

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        
        return name.toString();
    }

    @Override
    public void run() {
        if (!SlimeHUD.getInstance().getPlayerData().getBoolean(player.getUniqueId().toString() + ".waila", true)) {
            WAILABar.setVisible(false);
            return;
        }

        String facing = getFacing();
        if (facing == previousFacing) {
            return; // Nothing changed, skip for now
        }

        previousFacing = facing;
        switch (WAILALocation) {
            case "bossbar":
                if (facing.equals("")) {
                    lastFacingWasVanilla = true;
                    WAILABar.setVisible(false);
                    break;
                } else if (lastFacingWasVanilla) {
                    lastFacingWasVanilla = false;
                    WAILABar.setVisible(true);
                }

                WAILABar.setTitle(useOriginalColors ? facing : ChatColor.stripColor(facing));

                if (automaticWAILABarColor) {
                    WAILABar.setColor(Util.pickBarColorFromName(facing));
                }

                break;
            
            case "hotbar":
                getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(facing));
        
            default:
                break;
        }
        
    }

}
