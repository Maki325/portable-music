package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.IBoomboxCapability;
import me.maki325.mcmods.portablemusic.common.entities.SoundItemEntity;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.savedata.SoundManagerSaveData;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent public static void onPlayerToss(ItemTossEvent event) {
        ItemEntity itemEntity = event.getEntity();
        Player player = event.getPlayer();
        if(!itemEntity.getItem().is(PMItems.BOOMBOX_ITEM.get())) return;
        var soundItemEntity = new SoundItemEntity(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem());

        soundItemEntity.setDeltaMovement(itemEntity.getDeltaMovement());
        soundItemEntity.setPickUpDelay(40);
        soundItemEntity.setThrower(itemEntity.getThrower());

        player.getCommandSenderWorld().addFreshEntity(soundItemEntity);
        itemEntity.discard();
    }

    public static SoundManagerSaveData createSoundManagerSaveData() {
        return new SoundManagerSaveData();
    }

    public static SoundManagerSaveData loadSoundManagerSaveData(CompoundTag tag) {
        var data = createSoundManagerSaveData();
        data.load(tag);
        return data;
    }

    @SubscribeEvent public static void onServerStart(ServerStartedEvent event) {
        ServerSoundManager.init(event.getServer());
        event.getServer().overworld().getDataStorage().computeIfAbsent(
            EventHandler::loadSoundManagerSaveData,
            EventHandler::createSoundManagerSaveData,
            PortableMusic.MODID + "_sound_manager");

    }

    @SubscribeEvent public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer)) return;
        ServerSoundManager.getInstance().sync(event.getEntity());
    }

    @SubscribeEvent public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IBoomboxCapability.class);
    }

    @SubscribeEvent public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        // Some client setup code
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getLevel().isClientSide) return;
        ServerPlayer joinedPlayer = (ServerPlayer) event.getEntity();

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
        }

        for(ItemStack itemStack : joinedPlayer.getInventory().items) {
            if(!itemStack.is(PMItems.BOOMBOX_ITEM.get())) continue;
            itemStack.getOrCreateTag();
        }
    }

}
