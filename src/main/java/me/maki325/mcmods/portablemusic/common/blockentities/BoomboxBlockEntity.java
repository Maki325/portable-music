package me.maki325.mcmods.portablemusic.common.blockentities;

import com.google.common.base.MoreObjects;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.IBoomboxCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

import static me.maki325.mcmods.portablemusic.common.Utils.getStringOr;

public class BoomboxBlockEntity extends BlockEntity {

    public static final Block[] VALID_BLOCKS = { PMBlocks.BOOMBOX_BLOCK.get() };

    String sound = "";
    double time = 0;

    public BoomboxBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntities.BOOMBOX_BLOCKENTITY.get(), pos, state);
    }

    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("sound", sound == null ? "" : sound);
        tag.putDouble("time", time);
    }

    @Override public void saveToItem(ItemStack itemStack) {
        super.saveToItem(itemStack);

        IBoomboxCapability iBoomboxCapability =
                itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
        if (iBoomboxCapability == null) return;

        System.out.println("playerDestroy boomboxBlockEntity: " + this);
        System.out.println("playerDestroy iBoomboxCapability before: " + iBoomboxCapability);
        iBoomboxCapability.setSound(getSound());
        iBoomboxCapability.setTime(getTime());
        iBoomboxCapability.setTime(5);
        System.out.println("playerDestroy iBoomboxCapability after: " + iBoomboxCapability);
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        this.sound = getStringOr(tag, "sound", "");
        this.time = tag.getDouble("time");
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sound", this.getSound())
                .add("time", this.getTime())
                .toString();
    }
}
