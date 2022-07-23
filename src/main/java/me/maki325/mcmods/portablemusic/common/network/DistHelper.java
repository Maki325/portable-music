package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class DistHelper {

    public static void syncBoomboxTileEntity(BlockPos position, CompoundTag tag) {
        Minecraft.getInstance().level.getBlockEntity(position, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
                .ifPresent(boomboxBlockEntity -> boomboxBlockEntity.load(tag));
    }

}
