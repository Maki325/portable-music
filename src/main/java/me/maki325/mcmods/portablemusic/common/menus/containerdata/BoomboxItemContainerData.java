package me.maki325.mcmods.portablemusic.common.menus.containerdata;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.IBoomboxCapability;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class BoomboxItemContainerData implements ContainerData {


    ItemStack itemStack;
    IBoomboxCapability boomboxCapability;
    int count;

    public BoomboxItemContainerData(ItemStack itemStack, int count) {
        this.itemStack = itemStack;
        this.count = count;

        this.boomboxCapability = itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
    }

    @Override public int get(int key) {
        return switch (key) {
            case 0 -> boomboxCapability == null ? 0 : boomboxCapability.getSoundId();
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.itemStack + "'");
        };
    }

    @Override public void set(int key, int value) {
        if(boomboxCapability == null) {
            return;
        }
        switch (key) {
            case 0 -> boomboxCapability.setSoundId(value);
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.itemStack + "'");
        }
    }

    @Override public int getCount() {
        return count;
    }

}
