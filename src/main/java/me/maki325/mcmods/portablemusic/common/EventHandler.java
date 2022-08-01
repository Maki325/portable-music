package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.IBoomboxCapability;
import me.maki325.mcmods.portablemusic.common.capabilites.inventory.BoomboxItemStackHandler;
import me.maki325.mcmods.portablemusic.common.entities.SoundItemEntity;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.savedata.SoundManagerSaveData;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        event.register(BoomboxItemStackHandler.class);
    }

}
