package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveSoundMessage {
    public int soundId;

    public RemoveSoundMessage(int soundId) {
        this.soundId = soundId;
    }

    public RemoveSoundMessage(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(soundId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (!ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() ->
                ClientSoundManager.getInstance().removeSound(soundId));
        }
        ctx.get().setPacketHandled(true);
    }
}
