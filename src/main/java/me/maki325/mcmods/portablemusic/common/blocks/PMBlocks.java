package me.maki325.mcmods.portablemusic.common.blocks;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PMBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PortableMusic.MODID);

    public static final RegistryObject<Block> BOOMBOX_BLOCK = BLOCKS.register("boombox", BoomboxBlock::new);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
