package me.maki325.mcmods.portablemusic.client.sound;

import com.mojang.blaze3d.audio.Listener;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MP3Sound {

    public void play(SoundInstance p_120313_) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        // For obfuscated names grep in 'build/createSrgToMcp/output.srg' file
        SoundEngine soundEngine = ObfuscationReflectionHelper
                        .getPrivateValue(SoundManager.class, soundManager, "f_120349_");

        boolean isSoundEngineActive =
                ObfuscationReflectionHelper.getPrivateValue(SoundEngine.class, soundEngine, "f_120219_");
        ChannelAccess channelAccess =
                ObfuscationReflectionHelper.getPrivateValue(SoundEngine.class, soundEngine, "f_120224_");
        Listener listener =
                ObfuscationReflectionHelper.getPrivateValue(SoundEngine.class, soundEngine, "f_120221_");

        if(!isSoundEngineActive) return;
        p_120313_ = net.minecraftforge.client.ForgeHooksClient.playSound(soundEngine, p_120313_);
        if(p_120313_ == null || !p_120313_.canPlaySound()) {
            return;
        }
        WeighedSoundEvents weighedsoundevents = p_120313_.resolve(soundManager);
        ResourceLocation resourcelocation = p_120313_.getLocation();
        if (weighedsoundevents == null) {
            return;
        }
        Sound sound = p_120313_.getSound();
        if (sound == SoundManager.EMPTY_SOUND) {
            return;
        }

        /*float f = p_120313_.getVolume();
        float f1 = Math.max(f, 1.0F) * (float)sound.getAttenuationDistance();
        SoundSource soundsource = p_120313_.getSource();
        float f2 = this.calculateVolume(f, soundsource);
        float f3 = this.calculatePitch(p_120313_);
        SoundInstance.Attenuation soundinstance$attenuation = p_120313_.getAttenuation();
        boolean flag = p_120313_.isRelative();
        if (f2 == 0.0F && !p_120313_.canStartSilent()) {
            return;
        }

        Vec3 vec3 = new Vec3(p_120313_.getX(), p_120313_.getY(), p_120313_.getZ());
        // if (!this.listeners.isEmpty()) {
        //     boolean flag1 = flag || soundinstance$attenuation == SoundInstance.Attenuation.NONE || this.listener.getListenerPosition().distanceToSqr(vec3) < (double)(f1 * f1);
        //     if (flag1) {
        //         for(SoundEventListener soundeventlistener : this.listeners) {
        //             soundeventlistener.onPlaySound(p_120313_, weighedsoundevents);
        //         }
        //     } else {
        //         LOGGER.debug(MARKER, "Did not notify listeners of soundEvent: {}, it is too far away to hear", (Object)resourcelocation);
        //     }
        // }

        if (listener.getGain() <= 0.0F) {
            return;
        }
        boolean loopAutomatically = shouldLoopAutomatically(p_120313_);
        boolean shouldStream = sound.shouldStream();
        CompletableFuture<ChannelAccess.ChannelHandle> completablefuture = channelAccess.createHandle(sound.shouldStream() ? Library.Pool.STREAMING : Library.Pool.STATIC);
        ChannelAccess.ChannelHandle channelaccess$channelhandle = completablefuture.join();
        if (channelaccess$channelhandle == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                LOGGER.warn("Failed to create new sound handle");
            }

        }
        else {
            LOGGER.debug(MARKER, "Playing sound {} for event {}", sound.getLocation(), resourcelocation);
            this.soundDeleteTime.put(p_120313_, this.tickCount + 20);
            this.instanceToChannel.put(p_120313_, channelaccess$channelhandle);
            this.instanceBySource.put(soundsource, p_120313_);
            channelaccess$channelhandle.execute((p_194488_) -> {
                p_194488_.setPitch(f3);
                p_194488_.setVolume(f2);
                if (soundinstance$attenuation == SoundInstance.Attenuation.LINEAR) {
                    p_194488_.linearAttenuation(f1);
                } else {
                    p_194488_.disableAttenuation();
                }

                p_194488_.setLooping(loopAutomatically && !shouldStream);
                p_194488_.setSelfPosition(vec3);
                p_194488_.setRelative(flag);
            });
            final SoundInstance soundinstance = p_120313_;

            // Always stream
            soundinstance.getStream(this.soundBuffers, sound, loopAutomatically).thenAccept((p_194504_) -> {
                channelaccess$channelhandle.execute((p_194498_) -> {
                    p_194498_.attachBufferStream(p_194504_);
                    p_194498_.play();
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.sound.PlayStreamingSourceEvent(this, soundinstance, p_194498_));
                });
            });

            if (p_120313_ instanceof TickableSoundInstance) {
                this.tickingSounds.add((TickableSoundInstance)p_120313_);
            }

        }*/
    }

    public CompletableFuture<AudioStream> getStream() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new MP3AudioStream(new FileInputStream(""));
            } catch (IOException ioexception) {
                throw new CompletionException(ioexception);
            }
        }, Util.backgroundExecutor());
    }

}
