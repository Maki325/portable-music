package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.CapabilityHandler;
import me.maki325.mcmods.portablemusic.common.network.Network;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonHandler {

    @SubscribeEvent public static void setup(FMLCommonSetupEvent event) {
        Network.register();
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

}
