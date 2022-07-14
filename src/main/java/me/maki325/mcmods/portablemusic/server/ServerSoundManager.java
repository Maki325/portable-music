package me.maki325.mcmods.portablemusic.server;

import me.maki325.mcmods.portablemusic.common.network.AddSoundMessage;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;
import me.maki325.mcmods.portablemusic.common.sound.AbstractSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraftforge.network.PacketDistributor;
import static me.maki325.mcmods.portablemusic.common.Utils.vec3ToBlockPos;

public class ServerSoundManager extends AbstractSoundManager {

    @Override
    public int addSound(Sound sound) {
        int soundId = super.addSound(sound);

        Network.CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with(() ->
                        sound.level.getChunkAt(vec3ToBlockPos(sound.location))),
                new AddSoundMessage(soundId, sound)
        );

        return soundId;
    }

    @Override
    public void updateSound(int soundId, Sound sound) {
        super.updateSound(soundId, sound);

        Network.CHANNEL.send(
            PacketDistributor.TRACKING_CHUNK.with(() ->
                    sound.level.getChunkAt(vec3ToBlockPos(sound.location))),
            new AddSoundMessage(soundId, sound)
        );
    }

    @Override
    public boolean playSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        sound.soundState = SoundState.PLAYING;

        Network.CHANNEL.send(
            PacketDistributor.TRACKING_CHUNK.with(() ->
                    sound.level.getChunkAt(vec3ToBlockPos(sound.location))),
            new ToggleSoundMessage(soundId, SoundState.PLAYING)
        );
        return true;
    }

    @Override
    public boolean stopSound(int soundId) {
        return true;
    }

    @Override
    public boolean pauseSound(int soundId) {
        return true;
    }

    @Override public void handleMessage(ToggleSoundMessage message) {}

    private static final ServerSoundManager soundManager = new ServerSoundManager();

    public static ServerSoundManager getInstance() {
        return soundManager;
    }
}
