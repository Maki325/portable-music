package me.maki325.mcmods.portablemusic.common.blocks;

import me.maki325.mcmods.portablemusic.common.blockentities.BoomboxBlockEntity;
import me.maki325.mcmods.portablemusic.common.blockentities.PMBlockEntities;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.BoomboxProvider;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.CapabilityHandler;
import me.maki325.mcmods.portablemusic.common.capabilities.boombox.IBoomboxCapability;
import me.maki325.mcmods.portablemusic.common.items.PMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack itemStack = super.getCloneItemStack(state, target, level, pos, player);

        level.getBlockEntity(pos, PMBlockEntities.BOOMBOX_BLOCKENTITY.get())
        .ifPresent(blockEntity -> blockEntity.saveToItem(itemStack));

        return itemStack;
    }

//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
//        System.out.println("CLICK!");
//        if(hand == InteractionHand.MAIN_HAND) {
//            System.out.println("MAIN_HAND!");
//        } else {
//            System.out.println("OFF_HAND!");
//        }
//        ItemStack item = player.getItemInHand(hand);
//        System.out.println("item: " + item);
//        if(item.is(Items.AIR)) {
//            return InteractionResult.PASS;
//        }
//        item.shrink(1);
//        player.setItemInHand(hand, item);
//        return InteractionResult.SUCCESS;
//    }

//    @Override
//    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
//        player.awardStat(Stats.BLOCK_MINED.get(this));
//        player.causeFoodExhaustion(0.005F);
//
//        if (level instanceof ServerLevel serverLevel) {
//            getDrops(blockState, serverLevel, blockPos, blockEntity, player, itemStack)
//            .forEach((stack) -> {
//                if (blockEntity instanceof BoomboxBlockEntity boomboxBlockEntity) {
//                    LazyOptional<IBoomboxCapability> lazyOptional = stack.getCapability(BoomboxProvider.BOOMBOX_CAPABILITY);
//                    IBoomboxCapability iBoomboxCapability = lazyOptional.orElse(null);
//                    if (iBoomboxCapability != null && boomboxBlockEntity != null) {
//                        System.out.println("playerDestroy boomboxBlockEntity: " + boomboxBlockEntity);
//                        System.out.println("playerDestroy iBoomboxCapability before: " + iBoomboxCapability);
//                        iBoomboxCapability.setSound(boomboxBlockEntity.getSound());
//                        iBoomboxCapability.setTime(boomboxBlockEntity.getTime());
//                        iBoomboxCapability.setTime(5);
//                        System.out.println("playerDestroy iBoomboxCapability after: " + iBoomboxCapability);
//                    }
//                }
//
//                popResource(level, blockPos, stack);
//            });
//            blockState.spawnAfterBreak(serverLevel, blockPos, itemStack, true);
//        }
//    }

    @Override
    public void onPlace(BlockState p_60566_, Level p_60567_, BlockPos p_60568_, BlockState p_60569_, boolean p_60570_) {
        super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
        System.out.println(
            "onPlace: BlockState: " + p_60566_ +
            ", Level: " + p_60567_ +
            ", BlockPos: " + p_60568_ +
            ", BlockState: " + p_60569_ +
            ", boolean: " + p_60570_
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoomboxBlockEntity(pos, state);
    }

}
