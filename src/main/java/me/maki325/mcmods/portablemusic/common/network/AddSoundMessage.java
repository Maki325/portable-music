package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class AddSoundMessage {
    public static final UUID ZERO_UUID = new UUID(0, 0);

    public int soundId;
    public Sound sound;

    public AddSoundMessage(int soundId, Sound sound) {
        this.soundId = soundId;
        this.sound = sound;
        if(this.sound.playerUUID == ZERO_UUID) this.sound.playerUUID = null;
    }

    public AddSoundMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), new Sound(
            buf.readUtf(),
            new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
            buf.readEnum(SoundState.class),
            buf.readUUID()
        ));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(soundId);
        buf.writeUtf(sound.sound);
        buf.writeDouble(sound.location.x);
        buf.writeDouble(sound.location.y);
        buf.writeDouble(sound.location.z);
        buf.writeEnum(sound.soundState);
        buf.writeUUID(sound.playerUUID == null ? ZERO_UUID : sound.playerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (!ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() ->
                    ClientSoundManager.getInstance().addSound(soundId, sound));
        }
        ctx.get().setPacketHandled(true);
    }
}
