package me.maki325.mcmods.portablemusic.server;

import me.maki325.mcmods.portablemusic.common.network.AddSoundMessage;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;
import me.maki325.mcmods.portablemusic.common.savedata.SoundManagerSaveData;
import me.maki325.mcmods.portablemusic.common.sound.AbstractSoundManager;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.network.PacketDistributor;
import static me.maki325.mcmods.portablemusic.common.Utils.vec3ToBlockPos;

public class ServerSoundManager extends AbstractSoundManager {

    @Override
    public int addSound(Sound sound) {
        int soundId = super.addSound(sound);

        Network.CHANNEL.send(
            getMessageTarget(sound),
            new AddSoundMessage(soundId, sound)
        );

        return soundId;
    }

    @Override
    public void updateSound(int soundId, Sound sound) {
        super.updateSound(soundId, sound);

        Network.CHANNEL.send(
            getMessageTarget(sound),
            new AddSoundMessage(soundId, sound)
        );
    }

    @Override
    public boolean playSound(int soundId) {
        var sound = sounds.get(soundId);
        if(sound == null) return false;
        sound.soundState = SoundState.PLAYING;

        Network.CHANNEL.send(
            getMessageTarget(sound),
            new ToggleSoundMessage(soundId, SoundState.PLAYING)
        );
        setDirty();

        return true;
    }

    @Override public boolean stopSound(int soundId) {
        return true;
    }

    @Override public boolean pauseSound(int soundId) {
        return true;
    }

    @Override public void handleMessage(ToggleSoundMessage message) {}

    @Override public void setDirty() {
        SoundManagerSaveData.getData(minecraftServer.overworld()).setDirty();
    }

    private PacketDistributor.PacketTarget getMessageTarget(Sound sound) {
        if(minecraftServer == null)
            return PacketDistributor.ALL.noArg();
        return PacketDistributor.TRACKING_CHUNK.with(() ->
                minecraftServer.getLevel(sound.level).getChunkAt(vec3ToBlockPos(sound.location)));
    }

    private static final ServerSoundManager soundManager = new ServerSoundManager();
    private static MinecraftServer minecraftServer;

    public static void init(MinecraftServer m_minecraftServer) {
        minecraftServer = m_minecraftServer;
    }

    public static ServerSoundManager getInstance() {
        return soundManager;
    }
}
