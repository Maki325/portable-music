package me.maki325.mcmods.portablemusic.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class DevelpmentCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pm").then(
            Commands.literal("soundManager")
                .then(Commands.literal("clear").executes((context) -> {
                    if (!(context.getSource().getEntity() instanceof ServerPlayer)) {
                        return 0;
                    }
                    ServerSoundManager.getInstance().clear();

                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("print").executes((context) -> {
                    if (!(context.getSource().getEntity() instanceof ServerPlayer)) {
                        return 0;
                    }
                    ServerSoundManager.getInstance().getSounds()
                        .forEach((key, value) -> {
                            String str = "Key(" + key + "): " + value;
                            context.getSource().getEntity().sendSystemMessage(Component.literal(str));
                        });

                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("get")
                    .then(Commands.argument("soundId", SoundIdArgument.soundId()).executes((context) -> {
                        int soundId = IntegerArgumentType.getInteger(context, "soundId");
                        if (!(context.getSource().getEntity() instanceof ServerPlayer)) {
                            return 0;
                        }
                        String str = ServerSoundManager.getInstance().getSound(soundId).toString();
                        context.getSource().getEntity().sendSystemMessage(Component.literal(str));

                        return Command.SINGLE_SUCCESS;
                    }))
                )
                .then(Commands.literal("set")
                    .then(Commands.literal("state")
                        .then(Commands.argument("soundState", EnumArgument.enumArgument(SoundState.class))
                            .then(Commands.argument("soundId", SoundIdArgument.soundId()).executes((context) -> {
                                int soundId = IntegerArgumentType.getInteger(context, "soundId");
                                SoundState state = context.getArgument("soundState", SoundState.class);
                                if (!(context.getSource().getEntity() instanceof ServerPlayer)) {
                                    return 0;
                                }
                                ServerSoundManager.getInstance().setSoundState(soundId, state);

                                return Command.SINGLE_SUCCESS;
                            }))
                        )
                    )
                )
        ));
    }

}
