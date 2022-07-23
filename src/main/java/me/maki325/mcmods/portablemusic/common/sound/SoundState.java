package me.maki325.mcmods.portablemusic.common.sound;

public enum SoundState {
    PLAYING(1),
    PAUSED(2),
    STOPPED(3),
    FINISHED(4);

    public static SoundState getSoundState(int value) {
        return switch (value) {
            case 1 -> PLAYING;
            case 2 -> PAUSED;
            case 3 -> STOPPED;
            case 4 -> FINISHED;
            default -> null;
        };
    }

    @Override public String toString() {
        return switch (value) {
            case 1 -> "PLAYING";
            case 2 -> "PAUSED";
            case 3 -> "STOPPED";
            case 4 -> "FINISHED";
            default -> "{value: " + value + "}";
        };
    }

    int value;
    SoundState(int value) {
        this.value = value;
    }
}
