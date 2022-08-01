package me.maki325.mcmods.portablemusic.common.menus.containerdata;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import net.minecraft.world.inventory.SimpleContainerData;

public class BoomboxContainerData extends SimpleContainerData {

    BoomboxBlockEntity boomboxBlockEntity;

    public BoomboxContainerData(BoomboxBlockEntity boomboxBlockEntity, int amount) {
        super(amount);
        this.boomboxBlockEntity = boomboxBlockEntity;
    }

    @Override public int get(int key) {
        return switch (key) {
            case 0 -> this.boomboxBlockEntity.getSoundId();
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.boomboxBlockEntity + "'");
        };
    }

}
