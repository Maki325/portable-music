package me.maki325.mcmods.portablemusic.common.blockentities;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PMBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PortableMusic.MODID);

    public static final RegistryObject<BlockEntityType<BoomboxBlockEntity>> BOOMBOX_BLOCKENTITY =
        BLOCK_ENTITIES.register("boombox_block_entity",
                () -> BlockEntityType.Builder.of(BoomboxBlockEntity::new, BoomboxBlockEntity.VALID_BLOCKS).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
