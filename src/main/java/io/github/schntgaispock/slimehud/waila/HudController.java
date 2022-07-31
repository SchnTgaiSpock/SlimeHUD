package io.github.schntgaispock.slimehud.waila;

import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.util.HudBuilder;
import io.github.schntgaispock.slimehud.util.Util;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoConnectorNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoNode;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyConnector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class HudController {

    // LinkedHashMaps preserve insertion order. Chances are, a MachineProcessHolder
    // is also an EnergyNetComponent, but the machine info should take priority over
    // energy info.
    private final Map<Class<?>, Function<HudRequest, String>> defaultHandlers = new LinkedHashMap<>();
    private final Map<Class<?>, Function<HudRequest, String>> customHandlers = new LinkedHashMap<>();

    public HudController() {
        // Set up Slimefun default items

        // Machines
        registerDefaultHandler(MachineProcessHolder.class, this::processMachine);

        // Generators
        registerDefaultHandler(AGenerator.class, this::processGenerator);
        registerDefaultHandler(SolarGenerator.class, this::processSolarGenerator);

        // Energy Network
        registerDefaultHandler(EnergyRegulator.class, this::processEnergyNode);
        registerDefaultHandler(EnergyConnector.class, this::processEnergyNode);
        registerDefaultHandler(EnergyNetComponent.class, this::processCapacitor);

        // Cargo Network
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
        int size = getNetworkSize(en);
        return size < 0 ? "" : "&7| Network Size: " + HudBuilder.getCommaNumber(size);
    }

    @Nonnull
    private String processCapacitor(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-energy-stored")) {
            return "";
        }

        EnergyNetComponent enc = (EnergyNetComponent) request.getSlimefunItem();
        EnergyNetComponentType enct = enc.getEnergyComponentType();
        if ((enct == EnergyNetComponentType.CAPACITOR ||
                enct == EnergyNetComponentType.GENERATOR ||
                enct == EnergyNetComponentType.CONSUMER) &&
                enc.getCapacity() > 0) {
            return HudBuilder.formatEnergyStored(enc.getCharge(request.getLocation()));
        }
        return "";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    private String processMachine(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-machine-progress")) {
            return "";
        }

        MachineProcessHolder<MachineOperation> machine = (MachineProcessHolder<MachineOperation>) request
                .getSlimefunItem();
        MachineOperation operation = machine.getMachineProcessor().getOperation(request.getLocation());

        if (operation == null) {
            return "&7| Idle";
        }

        int progress = operation.getProgress();
        int total = operation.getTotalTicks();
        return HudBuilder.formatProgressBar(progress, total);
    }

    @Nonnull
    private String processGenerator(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-generator-generation")) {
            return "";
        }

        AGenerator gen = (AGenerator) request.getSlimefunItem();
        int generation = gen.getEnergyProduction();
        if (generation > 0) {
            return HudBuilder.formatEnergyGenerated(generation);
        } else {
            return "&7| Not generating";
        }
    }

    @Nonnull
    private String processSolarGenerator(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-generator-generation")) {
            return "";
        }

        SolarGenerator gen = (SolarGenerator) request.getSlimefunItem();
        // Solar Generators dont use any fuel, so it's ok to call getGeneratedOutput
        int generation = gen.getGeneratedOutput(request.getLocation(), null);
        if (generation > 0) {
            return HudBuilder.formatEnergyGenerated(generation);
        } else {
            return "&7| Not generating";
        }
    }

    @Nonnull
    private String processCargoNode(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-channel")) {
            return "";
        }
        CargoNode cn = (CargoNode) request.getSlimefunItem();
        int channel = cn.getSelectedChannel(request.getLocation().getBlock()) + 1;
        return "&7| Channel: " + Util.getColorFromCargoChannel(channel).toString() + channel;
    }

    @Nonnull
    private String processCargoManagerConnector(@Nonnull HudRequest request) {
        if (!SlimeHUD.getInstance().getConfig().getBoolean("waila.show-cargo-size")) {
            return "";
        }
        Network cn = CargoNet.getNetworkFromLocation(request.getLocation());

        int size = getNetworkSize(cn);
        return size < 0 ? "" : "&7| Network Size: " + HudBuilder.getCommaNumber(size);
    }

    private int getNetworkSize(Network network) {
        if (network != null) {
            try {
                Field con = Network.class.getDeclaredField("connectorNodes");
                Field ter = Network.class.getDeclaredField("terminusNodes");

                con.setAccessible(true);
                ter.setAccessible(true);

                int conSize = ((Set<?>) con.get(network)).size();
                int terSize = ((Set<?>) ter.get(network)).size();

                con.setAccessible(false);
                ter.setAccessible(false);
                return conSize + terSize + 1;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return -1;
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
    public String processRequest(@Nonnull HudRequest request) {
        // First see if there is a custom handler from an addon (to allow overriding the
        // default machine handler
        Function<HudRequest, String> handler = tryGetHandler(request.getSlimefunItem());
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

    /**
     * Register a custom handler for when a player looks at a Slimefun Item
     * 
     * @param clazz   The class extending {@code SlimefunItem}
     * @param handler A function that takes a {@code HudRequest} and returns
     *                formatted text to be displayed on the WAILA HUD
     */
    @ParametersAreNonnullByDefault
    public void registerCustomHandler(Class<?> clazz, Function<HudRequest, String> handler) {
        customHandlers.put(clazz, handler);
    }

}
