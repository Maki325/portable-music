package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

    public static CompoundTag saveVec3(Vec3 vec3) {
        var tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    public static Vec3 loadVec3(CompoundTag tag) {
        return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public static CompoundTag saveResourceKey(ResourceKey resourceKey) {
        var tag = new CompoundTag();
        tag.putString("registry", resourceKey.registry().toString());
        tag.putString("location", resourceKey.location().toString());
        return tag;
    }

    public static ResourceKey loadResourceKey(CompoundTag tag) {
        return ResourceKey.create(
            ResourceKey.createRegistryKey(new ResourceLocation(tag.getString("registry"))),
            new ResourceLocation(tag.getString("location"))
        );
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

    public static Component translatable(String key) {
        return Component.translatable(key.replace("%_%", PortableMusic.MODID));
    }
}
