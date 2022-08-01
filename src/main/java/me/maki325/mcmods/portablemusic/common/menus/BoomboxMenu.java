package me.maki325.mcmods.portablemusic.common.menus;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import me.maki325.mcmods.portablemusic.common.menus.containerdata.BoomboxContainerData;
import me.maki325.mcmods.portablemusic.common.menus.containerdata.BoomboxItemContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BoomboxMenu extends AbstractContainerMenu {
    private static final int DATA_COUNT = 1;
    private final ContainerLevelAccess containerAccess;
    private boolean fromItem = false;
    public ContainerData data;

    public BoomboxMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(1), BlockPos.ZERO, new SimpleContainerData(DATA_COUNT));
    }

    public BoomboxMenu(int id, Inventory playerInventory, ItemStack stack, BlockPos pos, ContainerData data) {
        this(id, playerInventory, createIItemHandlerFromItemStack(stack), pos, data);
        this.fromItem = true;
    }

    public BoomboxMenu(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(PMMenus.BOOMBOX_MENU.get(), id);
        this.data = data;
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);

        this.addSlot(new SlotItemHandler(slots, 0, 131, 34));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        addDataSlots(data);
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < 1) {
                if (!moveItemStackTo(item, 1, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return retStack;
    }

    @Override public boolean stillValid(Player player) {
        if(this.fromItem) {
            return player.getMainHandItem().is(PMItems.BOOMBOX_ITEM.get());
        }
        return stillValid(this.containerAccess, player, PMBlocks.BOOMBOX_BLOCK.get());
    }

    public static IItemHandler createIItemHandlerFromItemStack(ItemStack stack) {
        if(stack.is(PMItems.BOOMBOX_ITEM.get())) {
            var handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
            if(handler != null) return handler;
        }
        var handler = new ItemStackHandler(1);
        handler.deserializeNBT(stack.getOrCreateTag().getCompound("inventory"));
        return handler;
    }

    public static MenuConstructor getServerContainer(BoomboxBlockEntity boomboxBlockEntity, BlockPos pos) {
        return (id, playerInv, player) -> new BoomboxMenu(id, playerInv, boomboxBlockEntity.inventory, pos, new BoomboxContainerData(boomboxBlockEntity, DATA_COUNT));
    }

    public static MenuConstructor getServerContainer(ItemStack itemStack, BlockPos pos) {
        return (id, playerInv, player) -> new BoomboxMenu(id, playerInv, itemStack, pos, new BoomboxItemContainerData(itemStack, DATA_COUNT));
    }
}
