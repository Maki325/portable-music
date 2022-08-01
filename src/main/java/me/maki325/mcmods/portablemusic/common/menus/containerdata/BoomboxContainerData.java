package me.maki325.mcmods.portablemusic.common.menus.containerdata;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import net.minecraft.world.inventory.ContainerData;

public class BoomboxContainerData implements ContainerData {

    BoomboxBlockEntity boomboxBlockEntity;
    int count;

    public BoomboxContainerData(BoomboxBlockEntity boomboxBlockEntity, int count) {
        this.boomboxBlockEntity = boomboxBlockEntity;
        this.count = count;
    }

    @Override public int get(int key) {
        return switch (key) {
            case 0 -> this.boomboxBlockEntity.getSoundId();
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.boomboxBlockEntity + "'");
        };
    }

    @Override public void set(int key, int value) {
        switch (key) {
            case 0 -> this.boomboxBlockEntity.setSoundId(value);
            default -> throw new UnsupportedOperationException(
                "There is no value corresponding to key: '" + key + "' in: '" + this.boomboxBlockEntity + "'");
        }
    }

    @Override public int getCount() {
        return count;
    }
}
