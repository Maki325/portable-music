package me.maki325.mcmods.portablemusic;

import com.mojang.logging.LogUtils;
import me.maki325.mcmods.portablemusic.client.ClientHandler;
import me.maki325.mcmods.portablemusic.common.CommonHandler;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.commands.PMCommands;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PortableMusic.MODID)
public class PortableMusic {
    public static final String MODID = "portablemusic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PortableMusic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener((FMLClientSetupEvent e) -> e.enqueueWork(ClientHandler::setup));
        modEventBus.addListener((FMLCommonSetupEvent e) -> e.enqueueWork(CommonHandler::setup));

        PMBlocks.register(modEventBus);
        PMItems.register(modEventBus);
        PMBlockEntities.register(modEventBus);
        PMCommands.register(modEventBus);
    }

}
