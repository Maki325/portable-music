package me.maki325.mcmods.portablemusic.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlaySoundMessage {

    private boolean startOrStop;
    private UUID playerUUID;

    public PlaySoundMessage(boolean startOrStopIn, UUID entityUUIDIn) {
        this.startOrStop = startOrStopIn;
        this.playerUUID = entityUUIDIn;
    }

    public PlaySoundMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(startOrStop);
        buf.writeUUID(playerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer())
            return;

        if (this.startOrStop) {
            ctx.get().enqueueWork(() -> DistHelper.playDiscToPlayer(playerUUID));
        } else {
            // ctx.get().enqueueWork(() -> DistHelper.stopDisc(this.disc));
        }
    }

}
