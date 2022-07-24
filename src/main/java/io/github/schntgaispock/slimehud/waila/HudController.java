package io.github.schntgaispock.slimehud.waila;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class HudController {

    private final Map<Class<?>, Function<HudRequest, String>> defaultHandlers = new HashMap<>();
    private final Map<Class<?>, Function<HudRequest, String>> customHandlers = new HashMap<>();

    public HudController() {
        // Set up Slimefun default items
        registerDefaultHandler(EnergyRegulator.class, this::processEnergyNode);
        registerDefaultHandler(EnergyConnector.class, this::processEnergyNode);
        registerDefaultHandler(EnergyNetComponent.class, this::processMachine);
        registerDefaultHandler(CargoNode.class, this::processCargoNode);
        registerDefaultHandler(CargoConnectorNode.class, this::processCargoManagerConnector);
        registerDefaultHandler(CargoManager.class, this::processCargoManagerConnector);
    }

    @Nonnull
    private String processEnergyNode(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-energy-size")) {
            return "";
        }

        Network en = EnergyNet.getNetworkFromLocation(request.getLocation());

        if (en != null) {
            try {
                Field con = Network.class.getDeclaredField("connectorNodes");
                Field ter = Network.class.getDeclaredField("terminusNodes");

                con.setAccessible(true);
                ter.setAccessible(true);
                int conSize = ((Set<?>) con.get(en)).size();
                int terSize = ((Set<?>) ter.get(en)).size();
                con.setAccessible(false);
                ter.setAccessible(false);
                return "§7| Network Size: " + (conSize + terSize + 1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Nonnull
    private String processMachine(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-energy-stored")) {
            return "";
        }

        EnergyNetComponent enc = (EnergyNetComponent) request.getSlimefunItem();
        switch (enc.getEnergyComponentType()) {
            case CAPACITOR:
            case GENERATOR:
            case CONSUMER:
                if (enc.getCapacity() > 0) {
                    return "§7| " + enc.getCharge(request.getLocation()) + "J Stored";
                }
                break;
            default:
                break;
        }
        return "";
    }

    @Nonnull
    private String processCargoNode(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-channel")) {
            return "";
        }
        CargoNode cn = (CargoNode) request.getSlimefunItem();
        int channel = cn.getSelectedChannel(request.getLocation().getBlock()) + 1;
        return "§7| Channel: " + Util.getColorFromCargoChannel(channel).toString() + channel;
    }

    @Nonnull
    private String processCargoManagerConnector(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-size")) {
            return "";
        }
        Network en = CargoNet.getNetworkFromLocation(request.getLocation());

        if (en != null) {
            try {
                Field con = Network.class.getDeclaredField("connectorNodes");
                Field ter = Network.class.getDeclaredField("terminusNodes");

                con.setAccessible(true);
                ter.setAccessible(true);

                int conSize = ((Set<?>) con.get(en)).size();
                int terSize = ((Set<?>) ter.get(en)).size();

                con.setAccessible(false);
                ter.setAccessible(false);
                return "§7| Network Size: " + (conSize + terSize + 1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Nullable
    private Function<HudRequest, String> tryGetHandler(@Nonnull SlimefunItem slimefunItem) {
        for (Map.Entry<Class<?>, Function<HudRequest, String>> entry : customHandlers.entrySet()) {
            if (entry.getKey().isInstance(slimefunItem)) {
                return entry.getValue();
            }
        }
        for (Map.Entry<Class<?>, Function<HudRequest, String>> entry : defaultHandlers.entrySet()) {
            if (entry.getKey().isInstance(slimefunItem)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Nonnull
    public String processRequest(@Nonnull SlimefunItem slimefunItem, @Nonnull HudRequest request) {
        // First see if there is a custom handler from an addon (to allow overriding the default machine handler
        Function<HudRequest, String> handler = tryGetHandler(slimefunItem);
        if (handler == null) {
            // No handler found, return empty string
            return "";
        } else {
            return handler.apply(request);
        }
    }

    @ParametersAreNonnullByDefault
    private void registerDefaultHandler(Class<?> clazz, Function<HudRequest, String> handler) {
        defaultHandlers.put(clazz, handler);
    }

    @ParametersAreNonnullByDefault
    public void registerCustomHandler(Class<?> clazz, Function<HudRequest, String> handler) {
        customHandlers.put(clazz, handler);
    }

}
