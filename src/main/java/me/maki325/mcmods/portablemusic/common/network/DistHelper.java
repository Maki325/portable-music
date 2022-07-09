package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.MovableSound;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.RecordItem;

import java.util.Objects;
import java.util.UUID;

public class DistHelper {

    public static void playDiscToPlayer(UUID playerUUID) {
        System.out.println("PlaySoundMessage handle client!");
        Minecraft.getInstance().getSoundManager().stop();
        Minecraft.getInstance().getSoundManager().play(
            new MovableSound(
                Minecraft.getInstance().level.getPlayerByUUID(playerUUID),
                ((RecordItem) Objects.requireNonNull(net.minecraft.world.item.Items.MUSIC_DISC_PIGSTEP)).getSound()
            )
        );
    }

}
