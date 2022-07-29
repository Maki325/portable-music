package me.maki325.mcmods.portablemusic.client.sound;

import com.mojang.blaze3d.audio.Library;
import com.mojang.blaze3d.audio.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class PortableMusicSoundManager {

    Options options;

    public PortableMusicSoundManager() {
        options = Minecraft.getInstance().options;
    }

    public void play(SoundInstance soundInstance) {
        var soundManager = Minecraft.getInstance().getSoundManager();
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
        soundInstance = net.minecraftforge.client.ForgeHooksClient.playSound(soundEngine, soundInstance);
        if(soundInstance == null || !soundInstance.canPlaySound()) {
            return;
        }
        WeighedSoundEvents weighedsoundevents = soundInstance.resolve(soundManager);
        ResourceLocation resourcelocation = soundInstance.getLocation();
        if (weighedsoundevents == null) {
            return;
        }
        Sound sound = soundInstance.getSound();
        if (sound == SoundManager.EMPTY_SOUND) {
            return;
        }

        float f = soundInstance.getVolume();
        float f1 = Math.max(f, 1.0F) * (float)sound.getAttenuationDistance();
        SoundSource soundsource = soundInstance.getSource();
        float f2 = this.calculateVolume(f, soundsource);
        float f3 = this.calculatePitch(soundInstance);
        SoundInstance.Attenuation soundinstance$attenuation = soundInstance.getAttenuation();
        boolean flag = soundInstance.isRelative();
        if (f2 == 0.0F && !soundInstance.canStartSilent()) {
            return;
        }

        Vec3 vec3 = new Vec3(soundInstance.getX(), soundInstance.getY(), soundInstance.getZ());
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
        boolean loopAutomatically = shouldLoopAutomatically(soundInstance);
        boolean shouldStream = sound.shouldStream();
        CompletableFuture<ChannelAccess.ChannelHandle> completablefuture = channelAccess.createHandle(sound.shouldStream() ? Library.Pool.STREAMING : Library.Pool.STATIC);
        ChannelAccess.ChannelHandle channelaccess$channelhandle = completablefuture.join();
        /*if (channelaccess$channelhandle == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                LOGGER.warn("Failed to create new sound handle");
            }

        }
        else {
            LOGGER.debug(MARKER, "Playing sound {} for event {}", sound.getLocation(), resourcelocation);
            this.soundDeleteTime.put(soundInstance, this.tickCount + 20);
            this.instanceToChannel.put(soundInstance, channelaccess$channelhandle);
            this.instanceBySource.put(soundsource, soundInstance);
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
            final SoundInstance soundinstance = soundInstance;

            // Always stream
            soundinstance.getStream(this.soundBuffers, sound, loopAutomatically).thenAccept((p_194504_) -> {
                channelaccess$channelhandle.execute((p_194498_) -> {
                    p_194498_.attachBufferStream(p_194504_);
                    p_194498_.play();
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.sound.PlayStreamingSourceEvent(this, soundinstance, p_194498_));
                });
            });

            if (soundInstance instanceof TickableSoundInstance) {
                this.tickingSounds.add((TickableSoundInstance)soundInstance);
            }

        }*/
    }

    private float calculateVolume(float p_235258_, SoundSource p_235259_) {
        return Mth.clamp(p_235258_ * this.getVolume(p_235259_), 0.0F, 1.0F);
    }

    private float getVolume(@Nullable SoundSource p_120259_) {
        return p_120259_ != null && p_120259_ != SoundSource.MASTER ? this.options.getSoundSourceVolume(p_120259_) : 1.0F;
    }

    private float calculatePitch(SoundInstance p_120325_) {
        return Mth.clamp(p_120325_.getPitch(), 0.5F, 2.0F);
    }

    private static boolean requiresManualLooping(SoundInstance p_120316_) {
        return p_120316_.getDelay() > 0;
    }

    private static boolean shouldLoopManually(SoundInstance p_120319_) {
        return p_120319_.isLooping() && requiresManualLooping(p_120319_);
    }

    private static boolean shouldLoopAutomatically(SoundInstance p_120322_) {
        return p_120322_.isLooping() && !requiresManualLooping(p_120322_);
    }

}
