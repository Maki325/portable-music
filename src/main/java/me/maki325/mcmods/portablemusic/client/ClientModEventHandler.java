package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.client.screens.BoomboxUI;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.menus.PMMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {

    @SubscribeEvent public static void setup(FMLCommonSetupEvent event) {
        setRenderLayers();
        MenuScreens.register(PMMenus.BOOMBOX_MENU.get(), BoomboxUI::new);
    }

    public static void setRenderLayers() {
        // TODO: Change this when you figure how
        ItemBlockRenderTypes.setRenderLayer(PMBlocks.BOOMBOX_BLOCK.get(), RenderType.translucent());
    }

    @SubscribeEvent public static void registerKeys(RegisterKeyMappingsEvent event) {
        Keybinds.keyMappings.forEach(event::register);
    }

}
