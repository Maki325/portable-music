package me.maki325.mcmods.portablemusic.common.capabilites.boombox;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class BoomboxCapability implements IBoomboxCapability, INBTSerializable<CompoundTag> {

    int soundId;
    double time;

    @Override public int getSoundId() {
        return soundId;
    }

    @Override public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    @Override public double getTime() {
        return time;
    }

    @Override public void setTime(double time) {
        this.time = time;
    }

    @Override public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("soundId", soundId);
        tag.putDouble("time", time);
        return tag;
    }

    @Override public void deserializeNBT(CompoundTag tag) {
        this.soundId = tag.getInt("soundId");
        this.time = tag.getDouble("time");
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("soundId", this.getSoundId())
            .add("time", this.getTime())
            .toString();
    }

}
