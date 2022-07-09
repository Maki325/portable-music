package me.maki325.mcmods.portablemusic.common.items;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.blocks.BoomboxBlock;
import me.maki325.mcmods.portablemusic.common.blocks.PMBlocks;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.IBoomboxCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class BoomboxItem extends BlockItem {
    public BoomboxItem() {
        super(PMBlocks.BOOMBOX_BLOCK.get(),
                new Item.Properties().tab(CreativeModeTabs.PORTABLE_MUSIC_TAB));
        this.registerBlocks(Item.BY_BLOCK, this);
    }

    @Override
    public InteractionResult place(BlockPlaceContext blockPlaceContext) {
        /*if (!blockPlaceContext.canPlace()) {
            return InteractionResult.FAIL;
        }
        BlockPlaceContext blockplacecontext = this.updatePlacementContext(blockPlaceContext);
        if (blockplacecontext == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockstate = this.getPlacementState(blockplacecontext);
        if (blockstate == null) {
            return InteractionResult.FAIL;
        } else if (!this.placeBlock(blockplacecontext, blockstate)) {
            return InteractionResult.FAIL;
        }

        BlockPos blockpos = blockplacecontext.getClickedPos();
        Level level = blockplacecontext.getLevel();
        Player player = blockplacecontext.getPlayer();
        ItemStack itemstack = blockplacecontext.getItemInHand();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.is(blockstate.getBlock())) {
            blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
            this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
            blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }
        }

        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
        SoundType soundtype = blockstate1.getSoundType(level, blockpos, blockPlaceContext.getPlayer());
        level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, blockPlaceContext.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        if (player == null || !player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);*/

        InteractionResult result = super.place(blockPlaceContext);

        BlockPos blockpos = blockPlaceContext.getClickedPos();
        Level level = blockPlaceContext.getLevel();
        if(!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(blockpos);
            System.out.println("place blockEntity: " + blockEntity);
            if(blockEntity instanceof BoomboxBlockEntity boomboxBlockEntity) {
                System.out.println("place boomboxBlockEntity: " + boomboxBlockEntity);
                LazyOptional<IBoomboxCapability> lazyOptional =
                        blockPlaceContext.getItemInHand().getCapability(BoomboxProvider.BOOMBOX_CAPABILITY);
                IBoomboxCapability iBoomboxCapability = lazyOptional.orElse(null);
                System.out.println("place iBoomboxCapability: " + iBoomboxCapability);
                if (iBoomboxCapability != null) {
                    boomboxBlockEntity.setSound(iBoomboxCapability.getSound());
                    boomboxBlockEntity.setTime(iBoomboxCapability.getTime());
                }
            }
        }


        System.out.println("place: BlockPlaceContext: " + blockPlaceContext + ", result: " + result);

        // blockstate1.getBlock();
        return result;
    }
}
