package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;
import me.maki325.mcmods.portablemusic.common.sound.AbstractSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClientSoundManager extends AbstractSoundManager {
    protected HashMap<Integer, MovableSound> movableSounds = new HashMap<>();

    @Override
    public int addSound(int soundId, Sound sound) {
        super.updateSound(soundId, sound);
        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) return soundId;
        movableSound.updatePosition(sound.location);
        if(sound.playerUUID != null) {
            movableSound.updateSource(getPlayer(sound.playerUUID));
        } else {
            movableSound.updateSource(null);
        }
        return soundId;
    }

    @Override
    public boolean playSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) {
            movableSounds.put(soundId, new MovableSound(
                    sound.location,
                    new SoundEvent(new ResourceLocation(sound.sound))
            ));
            movableSound = movableSounds.get(soundId);
        }

        if(sound.soundState == SoundState.PLAYING) {
            if(sound.playerUUID != null) {
                movableSound.updateSource(getPlayer(sound.playerUUID));
            } else {
                movableSound.updatePosition(sound.location);
            }
        } else {
            Minecraft.getInstance().getSoundManager().stop();
            Minecraft.getInstance().getSoundManager().play(movableSound);
        }
        sound.soundState = SoundState.PLAYING;

        return true;
    }

    @Override
    public boolean stopSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) return false;

        Minecraft.getInstance().getSoundManager().stop(movableSound);
        sound.soundState = SoundState.STOPPED;

        return true;
    }

    @Override
    public boolean pauseSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) return false;

        Minecraft.getInstance().getSoundManager().stop(movableSound);
        sound.soundState = SoundState.PAUSED;

        return true;
    }

    @Override public void handleMessage(ToggleSoundMessage message) {
        switch (message.soundState) {
            case PLAYING -> playSound(message.soundId);
            case PAUSED -> pauseSound(message.soundId);
            case STOPPED, FINISHED -> stopSound(message.soundId);
        }
    }

    private static final ClientSoundManager soundManager = new ClientSoundManager();

    public static ClientSoundManager getInstance() {
        return soundManager;
    }

    private Player getPlayer(UUID playerUUID) {
        return Minecraft.getInstance().level.getPlayerByUUID(playerUUID);
    }
}
