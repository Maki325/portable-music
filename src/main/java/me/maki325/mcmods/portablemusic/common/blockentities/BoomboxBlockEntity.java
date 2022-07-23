package me.maki325.mcmods.portablemusic.common.blockentities;

import com.google.common.base.MoreObjects;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static me.maki325.mcmods.portablemusic.common.Utils.getSoundFromItemStack;
import static me.maki325.mcmods.portablemusic.common.Utils.vec3iToVec3;

public class BoomboxBlockEntity extends BlockEntity {

    public static final Block[] VALID_BLOCKS = { PMBlocks.BOOMBOX_BLOCK.get() };

    ItemStack disc;
    double time = 0;
    int soundId;
    Sound sound;

    public BoomboxBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntities.BOOMBOX_BLOCKENTITY.get(), pos, state);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("time", time);

        boolean hasDisc = disc != null;
        tag.putBoolean("hasDisc", hasDisc);
        if(hasDisc) {
            tag.put("disc", disc.save(new CompoundTag()));
        }
        tag.putInt("soundId", soundId);
        return tag;
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        this.time = tag.getDouble("time");

        if(tag.getBoolean("hasDisc")) {
            disc = ItemStack.of(tag.getCompound("disc"));
        } else {
            disc = null;
        }
        soundId = tag.getInt("soundId");
        sound = ServerSoundManager.getInstance().getSound(soundId);
    }

    @Override public void saveToItem(ItemStack itemStack) {
        super.saveToItem(itemStack);

        CompoundTag tag = itemStack.getOrCreateTag();

        tag.putDouble("time", getTime());

        boolean hasDisc = disc != null;
        tag.putBoolean("hasDisc", hasDisc);
        if(hasDisc) {
            tag.put("disc", disc.save(new CompoundTag()));
        }
        tag.putInt("soundId", soundId);
    }

    public void readFromItem(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        setTime(tag.getDouble("time"));

        if(tag.getBoolean("hasDisc")) {
            disc = ItemStack.of(tag.getCompound("disc"));
        } else {
            disc = null;
        }
        soundId = tag.getInt("soundId");
        if(soundId != 0) {
            sound = ServerSoundManager.getInstance().getSound(soundId);
        } else if(disc != null) {
            sound = new Sound(getSound(), this.level.dimension(), vec3iToVec3(this.worldPosition), SoundState.PAUSED);
            soundId = ServerSoundManager.getInstance().addSound(sound);
        }
    }

    public void play() {
        ServerSoundManager.getInstance().playSound(soundId);
    }

    public String getSound() {
        return getSoundFromItemStack(disc);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ItemStack getDisc() {
        return disc;
    }

    public void setDisc(ItemStack disc) {
        this.disc = disc.copy();
        this.disc.setCount(1);
        setChanged();
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sound", this.getSound())
                .add("time", this.getTime())
                .toString();
    }
}
