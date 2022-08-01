package me.maki325.mcmods.portablemusic.common.capabilites.boombox;

import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.common.capabilites.inventory.ItemStackProvider;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {

    public static final ResourceLocation BOOMBOX_CAPABILITY =
        new ResourceLocation(PortableMusic.MODID, "boombox");
    public static final ResourceLocation BOOMBOX_ITEM_CAPABILITY =
        new ResourceLocation(PortableMusic.MODID, "boombox_item");

    @SubscribeEvent public static void attachCapabilityItem(AttachCapabilitiesEvent<ItemStack> event) {
        if (!event.getObject().is(PMItems.BOOMBOX_ITEM.get())) return;

        event.addCapability(BOOMBOX_CAPABILITY, new BoomboxProvider());
        event.addCapability(BOOMBOX_ITEM_CAPABILITY, new ItemStackProvider(event.getObject()));
    }

}
