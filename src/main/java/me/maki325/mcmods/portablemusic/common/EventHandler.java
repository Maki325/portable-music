package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.entities.SoundItemEntity;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.savedata.SoundManagerSaveData;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        if(!(event.getEntity() instanceof ItemEntity itemEntity)) {
            return;
        }
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

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        ServerSoundManager.init(event.getServer());
        var stuff = event.getServer().overworld().getDataStorage().computeIfAbsent(
                EventHandler::loadSoundManagerSaveData,
                EventHandler::createSoundManagerSaveData,
                PortableMusic.MODID + "_sound_manager");

        System.out.println("onServerStart: SOUNDS!");
        for(var sound : ServerSoundManager.getInstance().getSounds().entrySet()) {
            System.out.println("Sound: (id: " + sound.getKey() + ", sound: " + sound.getValue() + ")");
        }
        System.out.println("onServerStart: SOUNDS END!");
        System.out.println("onServerStart: END");
    }

}
