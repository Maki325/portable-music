package me.maki325.mcmods.portablemusic.common.capabilites.inventory;

import me.maki325.mcmods.portablemusic.common.capabilites.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilites.boombox.IBoomboxCapability;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import me.maki325.mcmods.portablemusic.server.ServerSoundManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static me.maki325.mcmods.portablemusic.common.Utils.getSoundFromItemStack;

public class BoomboxItemStackHandler extends ItemStackHandler {
    public Player player;
    public ItemStack owner;
    public IBoomboxCapability boomboxCapability;

    public BoomboxItemStackHandler(ItemStack stack, int size) {
        super(size);
        this.owner = stack;
        this.boomboxCapability = owner.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY).orElse(null);
    }

    @Override protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        var stack = this.stacks.get(slot);
        if(stack.isEmpty()) {
            clearSound();
        } else {
            setSound(stack);
        }
    }

    public void setSound(ItemStack stack) {
        if(boomboxCapability == null) return;
        if(player == null) return;
        var soundManager = ServerSoundManager.getInstance();
        if(boomboxCapability.getSoundId() != 0) {
            clearSound();
        }

        var sound = new Sound(
            getSoundFromItemStack(stack),
            this.player.level.dimension(),
            this.player.position(),
            SoundState.FINISHED,
            this.player.getUUID()
        );

        boomboxCapability.setSoundId(soundManager.addSound(sound));
        boomboxCapability.setTime(0);
    }

    public void clearSound() {
        if(boomboxCapability == null) return;
        ServerSoundManager.getInstance().removeSound(boomboxCapability.getSoundId());
        boomboxCapability.setSoundId(0);
        boomboxCapability.setTime(0);
    }

    @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof RecordItem;
    }
}
