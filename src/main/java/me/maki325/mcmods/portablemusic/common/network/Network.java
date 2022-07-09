package me.maki325.mcmods.portablemusic.common.network;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(PortableMusic.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        int discriminator = 0;
        CHANNEL.registerMessage(
            discriminator++,
            PlaySoundMessage.class,
            PlaySoundMessage::encode,
            PlaySoundMessage::new,
            PlaySoundMessage::handle
        );
    }

}
