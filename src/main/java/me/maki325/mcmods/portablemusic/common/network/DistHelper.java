package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.MovableSound;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

import java.util.Objects;
import java.util.UUID;

public class DistHelper {

    public static void playDiscToPlayer(int entityId, String sound) {
        // System.out.println("PlaySoundMessage handle client!");
        // Minecraft.getInstance().getSoundManager().stop();
        // Minecraft.getInstance().getSoundManager().play(
        //     new MovableSound(
        //         Minecraft.getInstance().level.getEntity(entityId),
        //         new SoundEvent(new ResourceLocation(sound))
        //     )
        // );
    }

    public static void syncBoomboxTileEntity(BlockPos position, CompoundTag tag) {
        Minecraft.getInstance().level.getBlockEntity(position, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
                .ifPresent(boomboxBlockEntity -> boomboxBlockEntity.load(tag));
    }

}
