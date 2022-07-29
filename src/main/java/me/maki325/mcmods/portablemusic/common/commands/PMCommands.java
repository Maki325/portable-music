package me.maki325.mcmods.portablemusic.common.commands;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PMCommands {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES_INFO =
        DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, PortableMusic.MODID);

    public static final RegistryObject<?> SOUND_ID_ARGUMENT_TYPE =
        ARGUMENT_TYPES_INFO.register("sound_id", () -> ArgumentTypeInfos.registerByClass(SoundIdArgument.class, new SoundIdArgument.Info()));

    public static void register(IEventBus eventBus) {
        ARGUMENT_TYPES_INFO.register(eventBus);
    }

    @SubscribeEvent public static void register(RegisterCommandsEvent event) {
        DevelpmentCommand.register(event.getDispatcher());
    }
}
