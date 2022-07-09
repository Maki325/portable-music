package me.maki325.mcmods.portablemusic.common;

import net.minecraft.nbt.CompoundTag;

public class Utils {

    public static String getStringOr(CompoundTag tag, String key, String value) {
        String val = tag.getString(key);
        if(val == null) return value;
        return val;
    }

}
