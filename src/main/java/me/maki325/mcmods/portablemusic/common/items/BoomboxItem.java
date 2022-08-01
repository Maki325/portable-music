package me.maki325.mcmods.portablemusic.common.items;

import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BoomboxItem extends BlockItem {

    public BoomboxItem() {
        super(PMBlocks.BOOMBOX_BLOCK.get(),
                new Item.Properties().tab(CreativeModeTabs.PORTABLE_MUSIC_TAB));
        this.registerBlocks(Item.BY_BLOCK, this);
    }

//    @Override public @Nullable CompoundTag getShareTag(ItemStack stack) {
//        var result = new CompoundTag();
//        var tag = super.getShareTag(stack);
//
//        var inventoryCap = (BoomboxItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
//        var boomboxCap = (BoomboxCapability) stack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
//
//        if (tag != null) {
//            result.put("tag", tag);
//        }
//        if (inventoryCap != null) {
//            result.put("inventoryCap", inventoryCap.serializeNBT());
//        }
//        if (boomboxCap != null) {
//            result.put("inventoryCap", boomboxCap.serializeNBT());
//        }
//        return result;
//    }
//
//    @Override public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
//        assert nbt != null;
//        stack.setTag(nbt.getCompound("tag"));
//
//        var inventoryCap = (BoomboxItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
//        var boomboxCap = (BoomboxCapability) stack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
//
//        if (inventoryCap != null) {
//            inventoryCap.deserializeNBT(nbt.getCompound("inventoryCap"));
//        }
//        if (boomboxCap != null) {
//            boomboxCap.deserializeNBT(nbt.getCompound("boomboxCap"));
//        }
//    }
}
