package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class ClientHandler {

    public static void setup() {
        setRenderLayers();
    }

    public static void setRenderLayers() {
        // TODO: Change this when you figure how
        ItemBlockRenderTypes.setRenderLayer(PMBlocks.BOOMBOX_BLOCK.get(), RenderType.translucent());
    }

}
