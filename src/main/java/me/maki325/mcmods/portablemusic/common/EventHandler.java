package me.maki325.mcmods.portablemusic.common;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.entities.SoundItemEntity;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PortableMusic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        System.out.println("!!!onPlayerTossEvent!!! START player: " + event.getPlayer());
        if(!(event.getEntity() instanceof ItemEntity itemEntity)) {
            return;
        }
        Player player = event.getPlayer();
        if(!itemEntity.getItem().is(PMItems.BOOMBOX_ITEM.get())) return;
        Integer id = ServerSoundManager.getInstance().getSoundId(s -> s.playerUUID == player.getUUID());

        var soundItemEntity =
                id == null ? new SoundItemEntity(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem()) :
                new SoundItemEntity(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem(), id);

        soundItemEntity.setDeltaMovement(itemEntity.getDeltaMovement());
        soundItemEntity.setPickUpDelay(40);
        soundItemEntity.setThrower(itemEntity.getThrower());

        player.getCommandSenderWorld().addFreshEntity(soundItemEntity);
        itemEntity.discard();
        System.out.println("!!!onPlayerTossEvent!!! END");
    }

}
