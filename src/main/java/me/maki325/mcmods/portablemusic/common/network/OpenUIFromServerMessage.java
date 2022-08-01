package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.capabilites.inventory.BoomboxItemStackHandler;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.menus.BoomboxMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

import static me.maki325.mcmods.portablemusic.common.Utils.vec3ToBlockPos;

public class OpenUIFromServerMessage {
    public OpenUIFromServerMessage() {}
    public OpenUIFromServerMessage(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                var player = ctx.get().getSender();
                var itemStack = player.getMainHandItem();
                if(itemStack == null || itemStack.isEmpty() || !itemStack.is(PMItems.BOOMBOX_ITEM.get())) {
                    return;
                }

                var handler = (BoomboxItemStackHandler) itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                handler.player = player;

                var blockPos = vec3ToBlockPos(player.position()).east(1);
                var container = new SimpleMenuProvider(
                    BoomboxMenu.getServerContainer(itemStack, blockPos),
                    BoomboxBlockEntity.TITLE
                );
                NetworkHooks.openScreen(player, container, blockPos);
            });
        }
        ctx.get().setPacketHandled(true);
    }
}