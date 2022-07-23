package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleSoundMessage {

    public SoundState soundState;
    public int soundId;

    public ToggleSoundMessage(int soundId, SoundState soundState) {
        this.soundId = soundId;
        this.soundState = soundState;
    }

    public ToggleSoundMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readEnum(SoundState.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(soundId);
        buf.writeEnum(soundState);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> ServerSoundManager.getInstance().handleMessage(this));
        } else {
            ctx.get().enqueueWork(() -> ClientSoundManager.getInstance().handleMessage(this));
        }
        ctx.get().setPacketHandled(true);
    }
}
