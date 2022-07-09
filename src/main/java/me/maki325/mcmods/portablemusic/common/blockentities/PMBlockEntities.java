package me.maki325.mcmods.portablemusic.common.blockentities;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PMBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, PortableMusic.MODID);

    public static final RegistryObject<BlockEntityType<BoomboxBlockEntity>> BOOMBOX_BLOCKENTITY =
        BLOCK_ENTITIES.register("mybe",
                () -> BlockEntityType.Builder.of(BoomboxBlockEntity::new, BoomboxBlockEntity.VALID_BLOCKS).build(null));


    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
