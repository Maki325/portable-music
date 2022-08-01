package me.maki325.mcmods.portablemusic.common.menus.containerdata;

import me.maki325.mcmods.portablemusic.common.capabilites.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.IBoomboxCapability;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class BoomboxItemContainerData  extends SimpleContainerData {

    ItemStack itemStack;
    IBoomboxCapability boomboxCapability;

    public BoomboxItemContainerData(ItemStack itemStack, int amount) {
        super(amount);
        this.itemStack = itemStack;

        this.boomboxCapability = itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
    }

    @Override public int get(int key) {
        return switch (key) {
            case 0 -> boomboxCapability == null ? 0 : boomboxCapability.getSoundId();
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.itemStack + "'");
        };
    }

}
