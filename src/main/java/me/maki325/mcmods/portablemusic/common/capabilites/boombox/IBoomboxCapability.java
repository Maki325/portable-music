package me.maki325.mcmods.portablemusic.common.capabilites.boombox;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBoomboxCapability extends INBTSerializable<CompoundTag> {

    int getSoundId();
    void setSoundId(int soundId);

    double getTime();
    void setTime(double time);

}
