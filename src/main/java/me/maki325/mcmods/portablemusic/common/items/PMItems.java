package me.maki325.mcmods.portablemusic.common.items;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PMItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PortableMusic.MODID);

    public static final RegistryObject<Item> BOOMBOX_ITEM =
            ITEMS.register("boombox", () -> new BoomboxItem());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
