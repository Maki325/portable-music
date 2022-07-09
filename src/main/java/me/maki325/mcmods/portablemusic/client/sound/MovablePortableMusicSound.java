package me.maki325.mcmods.portablemusic.client.sound;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class MovablePortableMusicSound {

    Entity source;
    SoundSource soundSource;
    PortableMusicSound sound;
    float volume = 1.0F;

    double x = 0, y = 0, z = 0;

    public MovablePortableMusicSound(Entity source, PortableMusicSound sound) {
        this.source = source;
        this.sound = sound;
        this.volume = 1.0F;
    }

    public void tick() {
        // ((RecordItem) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(disc))).getSound();
        // Sound Engine -> public void play(SoundInstance p_120313_)
        if (!this.source.isAlive()) {
            // this.stop();
        } else {
            this.x = this.source.getX();
            this.y = this.source.getY();
            this.z = this.source.getZ();
        }
    }
}
