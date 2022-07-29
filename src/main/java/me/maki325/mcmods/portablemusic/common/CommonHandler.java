package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.common.capabilities.boombox.CapabilityHandler;
import me.maki325.mcmods.portablemusic.common.network.Network;
import net.minecraftforge.common.MinecraftForge;

public class CommonHandler {

    public static void setup() {
        Network.register();
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

}
