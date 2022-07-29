package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    private static boolean isPaused = false;
    private static Map<Integer, SoundState> toUnpause = new HashMap<>();


    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ClientSoundManager.getInstance().clear();
    }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) return;
        boolean newPaused = Minecraft.getInstance().isPaused();
        if(newPaused != isPaused) {
            if(newPaused) {
                toUnpause.clear();
                ClientSoundManager.getInstance().getSounds().forEach((soundId, sound) -> {
                    toUnpause.put(soundId, sound.soundState);
                    if(sound.soundState == SoundState.PLAYING) {
                        ClientSoundManager.getInstance().pauseSound(soundId);
                    }
                });
            } else {
                toUnpause.forEach(ClientSoundManager.getInstance()::setSoundState);
                toUnpause.clear();
            }
            isPaused = newPaused;
        }
    }
}
