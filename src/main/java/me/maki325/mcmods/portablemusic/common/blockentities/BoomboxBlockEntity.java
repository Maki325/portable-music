package me.maki325.mcmods.portablemusic.common.blockentities;

import com.google.common.base.MoreObjects;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.maki325.mcmods.portablemusic.common.Utils.*;

public class BoomboxBlockEntity extends BlockEntity {

    public static final Component TITLE = translatable("screen.%_%.boombox");

    public static final Block[] VALID_BLOCKS = { PMBlocks.BOOMBOX_BLOCK.get() };

    public final ItemStackHandler inventory;
    protected LazyOptional<ItemStackHandler> handler;
    double time = 0;
    int soundId;
    Sound sound;

    public BoomboxBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntities.BOOMBOX_BLOCKENTITY.get(), pos, state);

        this.inventory = new BoomboxItemStackHandler(1);
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    @Override public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Nullable @Override public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("time", time);
        tag.put("inventory", inventory.serializeNBT());
        tag.putInt("soundId", soundId);

        return tag;
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        this.time = tag.getDouble("time");

        this.inventory.deserializeNBT(tag.getCompound("inventory"));
        soundId = tag.getInt("soundId");
        sound = ServerSoundManager.getInstance().getSound(soundId);
    }

    @Override public void saveToItem(ItemStack itemStack) {
        super.saveToItem(itemStack);

        itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
            if(handler instanceof ItemStackHandler itemStackHandler) {
                itemStackHandler.setStackInSlot(0, inventory.getStackInSlot(0));
            } else {
                handler.insertItem(0, inventory.getStackInSlot(0), false);
            }
        });

        itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).ifPresent((handler) -> {
            handler.setSoundId(this.soundId);
            handler.setTime(this.time);
        });
    }

    public void readFromItem(ItemStack itemStack) {
        itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
            if(handler instanceof ItemStackHandler itemStackHandler) {
                inventory.deserializeNBT(itemStackHandler.serializeNBT());
            } else {
                inventory.insertItem(0, handler.getStackInSlot(0), false);
            }
        });
        itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).ifPresent((handler) -> {
            if(this.soundId != 0) {
                ServerSoundManager.getInstance().removeSound(this.soundId);
            }
            this.soundId = handler.getSoundId();
            this.time = handler.getTime();
        });

        if(soundId != 0) {
            sound = ServerSoundManager.getInstance().getSound(soundId);
        } else if(!inventory.getStackInSlot(0).isEmpty()) {
            sound = new Sound(getSound(), this.level.dimension(), vec3iToVec3(this.worldPosition), SoundState.PAUSED);
            soundId = ServerSoundManager.getInstance().addSound(sound);
        }

        if(soundId != 0 && sound != null) {
            sound.location = vec3iToVec3(getBlockPos());
            sound.playerUUID = null;
            ServerSoundManager.getInstance().updateSound(soundId, sound);
        }
    }

    public void update() {
        requestModelDataUpdate();
        setChanged();
        if (this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }

    public String getSound() {
        return getSoundFromItemStack(inventory.getStackInSlot(0));
    }

    public int getSoundId() {
        return soundId;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.handler.cast()
            : super.getCapability(cap, side);
    }

    @Override public void invalidateCaps() {
        super.invalidateCaps();
        this.handler.invalidate();
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("inventory", this.inventory)
            .add("sound", this.getSound())
            .add("time", this.getTime())
            .add("soundId", this.getSoundId())
            .add("sound", this.sound)
            .toString();
    }

    public class BoomboxItemStackHandler extends ItemStackHandler {
        public BoomboxItemStackHandler(int size) {
            super(size);
        }

        @Override protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            var stack = this.stacks.get(slot);

            if(BoomboxBlockEntity.this.level.isClientSide) return;
            if(stack.isEmpty()) {
                clearSound();
            } else {
                setSound(stack);
            }
            BoomboxBlockEntity.this.update();
        }

        public void setSound(ItemStack stack) {
            var soundManager = ServerSoundManager.getInstance();
            clearSound();

            BoomboxBlockEntity.this.time = 0;
            BoomboxBlockEntity.this.sound = new Sound(
                getSoundFromItemStack(stack),
                BoomboxBlockEntity.this.level.dimension(),
                vec3iToVec3(getBlockPos()),
                SoundState.FINISHED
            );
            BoomboxBlockEntity.this.soundId = soundManager.addSound(BoomboxBlockEntity.this.sound);
            BoomboxBlockEntity.this.sound = soundManager.getSound(BoomboxBlockEntity.this.soundId);
        }

        public void clearSound() {
            ServerSoundManager.getInstance().removeSound(BoomboxBlockEntity.this.soundId);
            BoomboxBlockEntity.this.soundId = 0;
            BoomboxBlockEntity.this.sound = null;
            BoomboxBlockEntity.this.time = 0;
        }

        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof RecordItem;
        }
    }
}
