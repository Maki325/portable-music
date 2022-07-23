package me.maki325.mcmods.portablemusic.common.sound;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import static me.maki325.mcmods.portablemusic.common.Utils.*;

public class Sound {

    public String sound;
    public Vec3 location;
    public SoundState soundState;
    public ResourceKey<Level> level;
    public UUID playerUUID;

    public Sound(String sound, Vec3 location, SoundState soundState) {
        this.sound = sound;
        this.location = location;
        this.soundState = soundState;
    }

    public Sound(String sound, Vec3 location, SoundState soundState, UUID playerUUID) {
        this.sound = sound;
        this.location = location;
        this.soundState = soundState;
        this.playerUUID = playerUUID;
    }

    public Sound(String sound, ResourceKey<Level> level, Vec3 location, SoundState soundState) {
        this.sound = sound;
        this.level = level;
        this.location = location;
        this.soundState = soundState;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("sound", sound);
        tag.putInt("soundState", soundState.value);
        if(location != null) tag.put("location", saveVec3(location));
        if(level != null) tag.put("level", saveResourceKey(level));
        if(playerUUID != null) tag.putUUID("playerUUID", playerUUID);
        return tag;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("sound", sound)
            .add("soundState", soundState)
            .add("location", location)
            .add("level", level)
            .add("playerUUID", playerUUID)
            .toString();
    }

    public static Sound create(CompoundTag tag) {
        var sound = new Sound(
            tag.getString("sound"),
            tag.contains("level") ? loadResourceKey(tag.getCompound("level")) : null,
            tag.contains("location") ? loadVec3(tag.getCompound("location")) : null,
            SoundState.getSoundState(tag.getInt("soundState"))
        );
        sound.playerUUID =
            tag.contains("playerUUID") ? tag.getUUID("playerUUID") : null;
        return sound;
    }
}
