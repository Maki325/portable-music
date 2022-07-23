package me.maki325.mcmods.portablemusic.common.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class SoundIdArgument implements ArgumentType<Integer> {

    public static SoundIdArgument soundId() {
        return new SoundIdArgument();
    }

    @Override public Integer parse(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ServerSoundManager.getInstance().getSounds().keySet().stream().map(String::valueOf), builder);
    }

    @Override public Collection<String> getExamples() {
        return Collections.EMPTY_LIST;
    }

    static class Info implements ArgumentTypeInfo<SoundIdArgument, Info.Template> {

        @Override public void serializeToNetwork(Template p_235375_, FriendlyByteBuf p_235376_) {}

        @Override public Template deserializeFromNetwork(FriendlyByteBuf p_235377_) {
            return new Template();
        }

        @Override public void serializeToJson(Template p_235373_, JsonObject p_235374_) {}

        @Override public Template unpack(SoundIdArgument p_235372_) {
            return new Template();
        }

        class Template implements ArgumentTypeInfo.Template<SoundIdArgument> {

            @Override public SoundIdArgument instantiate(CommandBuildContext p_235378_) {
                return new SoundIdArgument();
            }

            @Override
            public ArgumentTypeInfo<SoundIdArgument, ?> type() {
                return Info.this;
            }
        }

    }

}
