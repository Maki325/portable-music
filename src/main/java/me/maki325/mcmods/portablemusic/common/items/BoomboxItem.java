package me.maki325.mcmods.portablemusic.common.items;

import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BoomboxItem extends BlockItem {

    public BoomboxItem() {
        super(PMBlocks.BOOMBOX_BLOCK.get(),
                new Item.Properties().tab(CreativeModeTabs.PORTABLE_MUSIC_TAB).stacksTo(1));
        this.registerBlocks(Item.BY_BLOCK, this);
    }

}
