package me.maki325.mcmods.portablemusic.common.entities;

import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static me.maki325.mcmods.portablemusic.common.Utils.vec3iToVec3;

public class SoundItemEntity extends ItemEntity {
    private int soundId;
    private Sound sound;

    public SoundItemEntity(EntityType<? extends ItemEntity> p_31991_, Level p_31992_) {
        super(p_31991_, p_31992_);
        initSound();
    }

    public SoundItemEntity(Level p_32001_, double p_32002_, double p_32003_, double p_32004_, ItemStack p_32005_) {
        super(p_32001_, p_32002_, p_32003_, p_32004_, p_32005_);
        initSound();
    }

    public SoundItemEntity(Level p_149663_, double p_149664_, double p_149665_, double p_149666_, ItemStack p_149667_, double p_149668_, double p_149669_, double p_149670_) {
        super(p_149663_, p_149664_, p_149665_, p_149666_, p_149667_, p_149668_, p_149669_, p_149670_);
        initSound();
    }

    public void initSound() {
        if(this.level.isClientSide) return;
        CompoundTag tag = getItem().getOrCreateTag();
        if(!tag.getBoolean("hasDisc")) return;
        this.soundId = tag.getInt("soundId");
        if(this.soundId != 0) {
            this.sound = ServerSoundManager.getInstance().getSound(this.soundId);
            this.sound.playerUUID = null;
            ServerSoundManager.getInstance().updateSound(this.soundId, this.sound);
        }
    }

    @Override public void tick() {
        super.tick();
        if(this.sound == null) return;
        Vec3 position = vec3iToVec3(new BlockPos(this.getPosition(0)));
        if(this.sound.location != null && this.sound.location.distanceTo(position) < 1) return;
        this.sound.location = position;
        ServerSoundManager.getInstance().updateSound(soundId, this.sound);
    }

    @Override public void playerTouch(Player player) {
        if (!this.level.isClientSide) {
            if (this.pickupDelay > 0) return;
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(this, player);
            if (hook < 0) return;

            ItemStack copy = itemstack.copy();
            if (this.pickupDelay == 0 && (this.owner == null || lifespan - this.age <= 200 || this.owner.equals(player.getUUID())) && (hook == 1 || i <= 0 || player.getInventory().add(itemstack))) {
                copy.setCount(copy.getCount() - getItem().getCount());
                net.minecraftforge.event.ForgeEventFactory.firePlayerItemPickupEvent(player, this, copy);
                player.take(this, i);
                if (itemstack.isEmpty()) {
                    this.discard();
                    itemstack.setCount(i);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                player.onItemPickup(this);

                this.sound.location = player.position();
                this.sound.playerUUID = player.getUUID();
                ServerSoundManager.getInstance().updateSound(soundId, this.sound);
            }

        }
    }
}
