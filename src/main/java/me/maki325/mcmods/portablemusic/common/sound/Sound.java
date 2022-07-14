package me.maki325.mcmods.portablemusic.common.sound;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class Sound {

    public String sound;
    public Vec3 location;
    public SoundState soundState;
    public Level level;
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

    public Sound(String sound, Level level, Vec3 location, SoundState soundState) {
        this.sound = sound;
        this.level = level;
        this.location = location;
        this.soundState = soundState;
    }
}
