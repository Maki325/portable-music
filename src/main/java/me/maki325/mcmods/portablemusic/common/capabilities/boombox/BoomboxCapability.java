package me.maki325.mcmods.portablemusic.common.capabilities.boombox;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import static me.maki325.mcmods.portablemusic.common.Utils.getStringOr;

public class BoomboxCapability implements IBoomboxCapability, INBTSerializable<CompoundTag> {

    String sound;
    double time;

    @Override public String getSound() {
        return sound;
    }

    @Override public void setSound(String sound) {
        this.sound = sound;
    }

    @Override public double getTime() {
        return time;
    }

    @Override public void setTime(double time) {
        this.time = time;
    }

    @Override public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("sound", sound);
        tag.putDouble("time", time);
        return tag;
    }

    @Override public void deserializeNBT(CompoundTag tag) {
        this.sound = getStringOr(tag, "sound", "");
        this.time = tag.getDouble("time");
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sound", this.getSound())
                .add("time", this.getTime())
                .toString();
    }

}
