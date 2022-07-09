package me.maki325.mcmods.portablemusic.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class MovableSound extends AbstractTickableSoundInstance {

   Entity source;

    public MovableSound(Entity source, SoundEvent event) {
        super(event, SoundSource.RECORDS, RandomSource.create());
        this.source = source;
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override public void tick() {
        System.out.println("MovableSound tick client!");
        // ((RecordItem) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(disc))).getSound();
        // Sound Engine -> public void play(SoundInstance p_120313_)
        if (!this.source.isAlive()) {
            this.stop();
        } else {
            this.x = this.source.getX();
            this.y = this.source.getY();
            this.z = this.source.getZ();
        }
    }
}
