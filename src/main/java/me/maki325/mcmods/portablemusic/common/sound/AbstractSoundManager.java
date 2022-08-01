package me.maki325.mcmods.portablemusic.common.sound;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

public abstract class AbstractSoundManager implements ISoundManager {

    protected HashMap<Integer, Sound> sounds = new HashMap<>();
    protected final Random random = new Random();

    private int getRandomId() {
        // Container data seems to not sync more than 32768
        // So I'll limit it to that number for now
        // Though, no server should have even close to that many sounds
        // Active at once, so it should be fine
        return random.nextInt() % 32768;
    }

    @Override public int addSound(Sound sound) {
        int soundId = getRandomId();
        while(sounds.containsKey(soundId) || soundId == 0) soundId = getRandomId();

        sounds.put(soundId, sound);
        setDirty();
        return soundId;
    }

    @Override public int addSound(int soundId, Sound sound) {
        sounds.put(soundId, sound);
        setDirty();
        return soundId;
    }

    @Override public void updateSound(int soundId, Sound sound) {
        sounds.put(soundId, sound);
        setDirty();
    }

    @Override public void removeSound(int soundId) {
        sounds.remove(soundId);
        setDirty();
    }

    @Nullable @Override public Sound getSound(int soundId) {
        return sounds.get(soundId);
    }

    @Override public Sound getSound(Predicate<Sound> filter) {
        for(var pair : sounds.entrySet()) {
            if(filter.test(pair.getValue()))
                return pair.getValue();
        }
        return null;
    }

    @Override public Integer getSoundId(Predicate<Sound> filter) {
        for(var pair : sounds.entrySet()) {
            if(filter.test(pair.getValue()))
                return pair.getKey();
        }
        return null;
    }

    @Override public HashMap<Integer, Sound> getSounds() {
        return sounds;
    }

    @Override public void setSounds(HashMap<Integer, Sound> sounds) {
        this.sounds = sounds;
    }

    @Override public void clear() {
        sounds.clear();
    }

    @Override public void sync(Player player) {}
}
