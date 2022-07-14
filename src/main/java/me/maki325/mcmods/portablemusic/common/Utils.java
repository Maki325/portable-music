package me.maki325.mcmods.portablemusic.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.phys.Vec3;

public class Utils {

    public static String getStringOr(CompoundTag tag, String key, String value) {
        String val = tag.getString(key);
        if(val == null) return value;
        return val;
    }

    public static String getSoundFromItemStack(ItemStack stack) {
        if(stack == null) return null;

        if(stack.getItem() instanceof RecordItem record) {
            return record.getSound().getLocation().toString();
        }

        return null;
    }

    public static Vec3 vec3iToVec3(Vec3i pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3i vec3ToVec3i(Vec3 pos) {
        return new Vec3i(pos.x, pos.y, pos.z);
    }

    public static BlockPos vec3ToBlockPos(Vec3 pos) {
        return new BlockPos(pos.x, pos.y, pos.z);
    }
}
