package me.maki325.mcmods.portablemusic;

import com.mojang.logging.LogUtils;
import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.CapabilityHandler;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.IBoomboxCapability;
import me.maki325.mcmods.portablemusic.common.commands.PMCommands;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(PortableMusic.MODID)
public class PortableMusic {
    public static final String MODID = "portablemusic";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PortableMusic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        PMBlocks.register();
        PMItems.register();
        PMBlockEntities.register();
        PMCommands.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", PMBlocks.BOOMBOX_BLOCK.get());

        Network.register();
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IBoomboxCapability.class);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        // Some client setup code
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getLevel().isClientSide) return;
        LOGGER.info("EntityJoinWorldEvent");
        ServerPlayer joinedPlayer = (ServerPlayer) event.getEntity();
        LOGGER.info("Player! " + joinedPlayer);

        List<Player> playerList = event.getLevel().getNearbyPlayers(
                TargetingConditions.forNonCombat(), joinedPlayer, joinedPlayer.getBoundingBox().inflate(10));

        for(Player player : playerList) {
            for(ItemStack itemStack : player.getInventory().items) {
                if(!itemStack.is(PMItems.BOOMBOX_ITEM.get())) continue;
                LazyOptional<IBoomboxCapability> lazyOptional = itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY);
                IBoomboxCapability iBoomboxCapability = lazyOptional.orElse(null);
                if(iBoomboxCapability == null) continue;
            }
        }

        for(ItemStack itemStack : joinedPlayer.getInventory().items) {
            if(!itemStack.is(PMItems.BOOMBOX_ITEM.get())) continue;
            LazyOptional<IBoomboxCapability> lazyOptional = itemStack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY);
            IBoomboxCapability iBoomboxCapability = lazyOptional.orElse(null);
            if(iBoomboxCapability == null) continue;
            System.out.println("iBoomboxCapability: " + iBoomboxCapability);
        }

        for(ItemStack itemStack : joinedPlayer.getInventory().items) {
            if(!itemStack.is(PMItems.BOOMBOX_ITEM.get())) continue;
            itemStack.getOrCreateTag();
        }

        /* Works!
        Network.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player),
            new PlaySoundMessage(true, player.getUUID())
        );*/

        /*Minecraft.getInstance().getSoundManager().stop();
        Minecraft.getInstance().getSoundManager().play(
                new MovableSound(
                        event.getEntity(),
                        ((RecordItem) Objects.requireNonNull(net.minecraft.world.item.Items.MUSIC_DISC_PIGSTEP)).getSound()
                )
        );*/
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            // TODO: Change this when you figure how
            ItemBlockRenderTypes.setRenderLayer(PMBlocks.BOOMBOX_BLOCK.get(), RenderType.translucent());
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {
        private static boolean isPaused = false;
        private static Map<Integer, SoundState> toUnpause = new HashMap<>();
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


}
