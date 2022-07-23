package me.maki325.mcmods.portablemusic.common.blocks;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import me.maki325.mcmods.portablemusic.common.entities.SoundItemEntity;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.network.SyncBoomboxTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BoomboxBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NS =
            Block.box(2, 0, 6, 14, 9, 10);
    private static final VoxelShape SHAPE_EW =
            Block.box(6, 0, 2, 10, 9, 14);

    public BoomboxBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(1.0F));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter p_52989_, BlockPos p_52990_, CollisionContext p_52991_) {
        Direction direction = blockState.getValue(FACING);
        if(direction == Direction.NORTH || direction == Direction.SOUTH) {
            return SHAPE_NS;
        }
        return SHAPE_EW;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if(!(itemStack.getItem() instanceof RecordItem)) {
            level.getBlockEntity(blockPos, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
            .ifPresent(blockEntity -> {
                blockEntity.play();
                String s = blockEntity.getSound() == null ? "No Sound" : ("Sound: " + blockEntity.getSound());
                player.sendSystemMessage(Component.literal(s));
            });

            return InteractionResult.PASS;
        }

        level.getBlockEntity(blockPos, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
                .ifPresent(blockEntity -> blockEntity.setDisc(itemStack));
        itemStack.shrink(1);

        level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);

        level.getBlockEntity(blockPos, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
        .ifPresent(blockEntity -> blockEntity.readFromItem(itemStack));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if (level instanceof ServerLevel serverLevel) {
            getDrops(blockState, serverLevel, blockPos, blockEntity, player, itemStack)
            .forEach((stack) -> {
                if (blockEntity instanceof BoomboxBlockEntity boomboxBlockEntity) {
                    boomboxBlockEntity.saveToItem(stack);
                    // im killing someone (the one who named them almost the same (me))
                }

                popResource(level, blockPos, stack);
            });
            blockState.spawnAfterBreak(serverLevel, blockPos, itemStack, true);
        }
    }

    public static void popResource(Level p_49841_, BlockPos p_49842_, ItemStack p_49843_) {
        float f = EntityType.ITEM.getHeight() / 2.0F;
        double d0 = (double)((float)p_49842_.getX() + 0.5F) + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        double d1 = (double)((float)p_49842_.getY() + 0.5F) + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D) - (double)f;
        double d2 = (double)((float)p_49842_.getZ() + 0.5F) + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        popResource(p_49841_, () ->
            new SoundItemEntity(p_49841_, d0, d1, d2, p_49843_)
        , p_49843_);
    }

    private static void popResource(Level p_152441_, Supplier<ItemEntity> p_152442_, ItemStack p_152443_) {
        if (!p_152441_.isClientSide && !p_152443_.isEmpty() && p_152441_.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !p_152441_.restoringBlockSnapshots) {
            ItemEntity itementity = p_152442_.get();
            itementity.setDefaultPickUpDelay();
            p_152441_.addFreshEntity(itementity);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoomboxBlockEntity(pos, state);
    }

}
