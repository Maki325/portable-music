package me.maki325.mcmods.portablemusic;

import com.mojang.logging.LogUtils;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.commands.PMCommands;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.menus.PMMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PortableMusic.MODID)
public class PortableMusic {
    public static final String MODID = "portablemusic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PortableMusic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PMBlocks.register(modEventBus);
        PMItems.register(modEventBus);
        PMBlockEntities.register(modEventBus);
        PMCommands.register(modEventBus);
        PMMenus.register(modEventBus);
    }

}
