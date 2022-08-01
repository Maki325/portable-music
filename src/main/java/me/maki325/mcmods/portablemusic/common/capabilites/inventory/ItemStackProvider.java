package me.maki325.mcmods.portablemusic.common.capabilites.inventory;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackProvider implements ICapabilitySerializable<CompoundTag> {

    private ItemStack itemStack;
    public ItemStackProvider(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> new BoomboxItemStackHandler(itemStack, 1));

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? holder.cast() : LazyOptional.empty();
    }

    @Override public CompoundTag serializeNBT() {
        var handler = (BoomboxItemStackHandler) this.holder.orElseGet(null);
        if(handler != null) {
            return handler.serializeNBT();
        }
        return new CompoundTag();
    }

    @Override public void deserializeNBT(CompoundTag nbt) {
        var handler = (BoomboxItemStackHandler) this.holder.orElseGet(null);
        if(handler != null) {
            handler.deserializeNBT(nbt);
        }
    }

}
