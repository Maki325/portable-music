package me.maki325.mcmods.portablemusic.common.menus;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PMMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, PortableMusic.MODID);

    public static final RegistryObject<MenuType<BoomboxMenu>> BOOMBOX_MENU =
        register("boombox", BoomboxMenu::new);

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier) {
        return MENUS.register(name, () -> new MenuType<>(supplier));
    }

}
