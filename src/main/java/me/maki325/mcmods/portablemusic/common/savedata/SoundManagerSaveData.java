package me.maki325.mcmods.portablemusic.common.savedata;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.EventHandler;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class SoundManagerSaveData extends SavedData {
    @Override
    public CompoundTag save(CompoundTag tag) {
        if(tag == null) tag = new CompoundTag();
        var soundManager = ServerSoundManager.getInstance();

        var sounds = soundManager.getSounds();
        var soundsTag = new CompoundTag();
        sounds.entrySet().forEach(set -> soundsTag.put("sound_" + set.getKey(), set.getValue().save()));
        tag.put("sounds", soundsTag);

        return tag;
    }

    public void load(CompoundTag tag) {
        if(!tag.contains("sounds")) return;
        var sounds = ServerSoundManager.getInstance().getSounds();
        var soundsTag = tag.getCompound("sounds");
        soundsTag.getAllKeys().forEach(key ->
            sounds.put(Integer.parseInt(key.split("_")[1]),
                Sound.create(tag.getCompound(key))));
    }

    public static SoundManagerSaveData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
            EventHandler::loadSoundManagerSaveData,
            EventHandler::createSoundManagerSaveData,
            PortableMusic.MODID + "_sound_manager");
    }
}
