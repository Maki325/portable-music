package me.maki325.mcmods.portablemusic.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBoomboxTileEntity {

    private BlockPos position;
    private CompoundTag tag;

    public SyncBoomboxTileEntity(BlockPos position, CompoundTag tag) {
        this.position = position;
        this.tag = tag;
    }

    public SyncBoomboxTileEntity(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(position);
        buf.writeNbt(tag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer())
            return;

        ctx.get().enqueueWork(() -> DistHelper.syncBoomboxTileEntity(position, tag));
    }

}
