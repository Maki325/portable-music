package me.maki325.mcmods.portablemusic.common.sound;

import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface ISoundManager {

    int addSound(Sound sound);
    int addSound(int soundId, Sound sound);
    void updateSound(int soundId, Sound sound);
    boolean playSound(int soundId);
    boolean stopSound(int soundId);
    boolean pauseSound(int soundId);

    @Nullable Sound getSound(int soundId);
    @Nullable Sound getSound(Predicate<Sound> filter);
    @Nullable Integer getSoundId(Predicate<Sound> filter);

    void handleMessage(ToggleSoundMessage message);

}
