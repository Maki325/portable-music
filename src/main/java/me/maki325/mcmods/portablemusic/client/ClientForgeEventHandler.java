package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.network.OpenUIFromServerMessage;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventHandler {
    private static boolean isPaused = false;
    private static Map<Integer, SoundState> toUnpause = new HashMap<>();

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) return;

        if(Keybinds.openBoombox.consumeClick()) {
            ItemStack itemStack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
            if(itemStack != null && itemStack.is(PMItems.BOOMBOX_ITEM.get())) {
                Network.CHANNEL.sendToServer(new OpenUIFromServerMessage());
            }
         }

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
