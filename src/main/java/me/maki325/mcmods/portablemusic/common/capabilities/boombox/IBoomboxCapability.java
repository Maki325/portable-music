package me.maki325.mcmods.portablemusic.common.capabilities.boombox;

public interface IBoomboxCapability {

    String getSound();
    void setSound(String sound);

    double getTime();
    void setTime(double time);

    // void play();
    // boolean isPlayingSound();

}
