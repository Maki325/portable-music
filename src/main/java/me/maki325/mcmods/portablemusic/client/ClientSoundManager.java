package me.maki325.mcmods.portablemusic.client;

import com.mojang.blaze3d.audio.Channel;
import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;
import me.maki325.mcmods.portablemusic.common.sound.AbstractSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClientSoundManager extends AbstractSoundManager {
    protected HashMap<Integer, MovableSound> movableSounds = new HashMap<>();
    protected HashMap<Integer, ChannelAccess.ChannelHandle> channels = new HashMap<>();

    @Override public int addSound(int soundId, Sound sound) {
        var soundState = sound.soundState;
        if(!sounds.containsKey(soundId)) {
            sound.soundState = SoundState.NONE;
        }
        super.updateSound(soundId, sound);
        setSoundState(soundId, soundState);
        return soundId;
    }

    @Override public boolean playSound(int soundId) {
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

        movableSound.updatePosition(sound.location);
        if(sound.playerUUID != null) {
            movableSound.updateSource(getPlayer(sound.playerUUID));
        }

        if(sound.soundState != SoundState.PLAYING) {
            if(channels.containsKey(soundId)) {
                channels.get(soundId).execute(Channel::play);
            } else {
                Minecraft.getInstance().getSoundManager().play(movableSound);
                channels.put(soundId,
                    Minecraft.getInstance().getSoundManager().soundEngine.instanceToChannel.get(movableSound));
            }

        }
        sound.soundState = SoundState.PLAYING;

        return true;
    }

    @Override public boolean stopSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        sound.soundState = SoundState.STOPPED;

        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) return false;

        if(channels.containsKey(soundId)) {
            channels.get(soundId).execute(Channel::stop);
            channels.remove(soundId);
        }

        return true;
    }

    @Override public boolean pauseSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        sound.soundState = SoundState.PAUSED;

        var movableSound = movableSounds.get(soundId);
        if(movableSound == null) return false;

        if(channels.containsKey(soundId)) {
            channels.get(soundId).execute(Channel::pause);
        }

        return true;
    }

    @Override public void removeSound(int soundId) {
        super.removeSound(soundId);

        if(movableSounds.containsKey(soundId))
            Minecraft.getInstance().getSoundManager().stop(movableSounds.get(soundId));
        movableSounds.remove(soundId);

        if(channels.containsKey(soundId))
            channels.get(soundId).execute(Channel::stop);
        channels.remove(soundId);
    }

    @Override public boolean setSoundState(int soundId, SoundState soundState) {
        return switch (soundState) {
            case PLAYING -> playSound(soundId);
            case PAUSED -> pauseSound(soundId);
            case STOPPED, FINISHED -> stopSound(soundId);
            default -> false;
        };
    }

    @Override public void handleMessage(ToggleSoundMessage message) {
        setSoundState(message.soundId, message.soundState);
    }

    @Override public void setDirty() {}

    @Override public void sync() {}

    @Override public void clear() {
        super.clear();
        movableSounds.clear();
        channels.clear();
    }

    private static final ClientSoundManager soundManager = new ClientSoundManager();

    public static ClientSoundManager getInstance() {
        return soundManager;
    }

    private Player getPlayer(UUID playerUUID) {
        if(Minecraft.getInstance() == null || Minecraft.getInstance().level == null) return null;
        return Minecraft.getInstance().level.getPlayerByUUID(playerUUID);
    }
}
