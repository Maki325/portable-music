package me.maki325.mcmods.portablemusic.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class MovableSound extends AbstractTickableSoundInstance {

    Vec3 location;
    Player source;

    public MovableSound(Vec3 location, SoundEvent event) {
        super(event, SoundSource.RECORDS, RandomSource.create());
        this.location = location;
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override public void tick() {
        if(source != null) {
            this.x = this.source.getX();
            this.y = this.source.getY();
            this.z = this.source.getZ();
        } else {
            this.x = this.location.x;
            this.y = this.location.y;
            this.z = this.location.z;
        }
    }

    public void updatePosition(Vec3 location) {
        this.location = location;
    }

    public void updateSource(Player source) {
        this.source = source;
    }
}
