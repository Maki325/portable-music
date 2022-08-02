package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClearSoundsMessage {
    public ClearSoundsMessage() {}
    public ClearSoundsMessage(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        System.out.println("ClearSoundsMessage handle!!!");
        if(!ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> ClientSoundManager.getInstance().clear());
        }
        ctx.get().setPacketHandled(true);
    }
}
