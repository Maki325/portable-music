package me.maki325.mcmods.portablemusic.common.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeModeTabs {

    public static final CreativeModeTab PORTABLE_MUSIC_TAB = new CreativeModeTab("portablemusic") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(PMItems.BOOMBOX_ITEM.get());
        }
    };

}
