package mod.wittywhiscash.immersivelighting.blocks;

import mod.wittywhiscash.immersivelighting.Config;
import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import mod.wittywhiscash.immersivelighting.items.LightingItem;
import mod.wittywhiscash.immersivelighting.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class ImmersiveWallTorchBlock extends WallTorchBlock {

    // The variable chance to dim (in decimal format, so 25% is actually 0.25) a torch on a random tick.
    private static final double CHANCE_TO_DIM = Config.TORCH_CHANCETODIM.get();

    // Variables to track the amount of strikes the player has striked with a lighting item.
    private final int MAX_STRIKES_TINDER = Config.FLINTANDTINDER_MAXSTRIKES.get();
    private final int MAX_STRIKES_BOW_DRILL = Config.BOWDRILL_MAXSTRIKES.get();
    private int currentStrikes = 0;

    private static final boolean RELIGHT_ALLOWED = Config.TORCH_RELIGHTALLOWED.get();

    private static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;

    public ImmersiveWallTorchBlock() {
        super(Block.Properties.from(Blocks.WALL_TORCH));
        this.setDefaultState(this.getDefaultState().with(LIT, false).with(AGE, 0));
    }

    public static BooleanProperty getLitProperty() {
        return LIT;
    }

    public int getAgeInstance(BlockState state) { return state.get(AGE); }

    public static IntegerProperty getAgeProperty() { return AGE; }

    // The light value equals the age of the torch. Higher age means more light shed.
    @Override
    public int getLightValue(BlockState state) {
        return state.get(AGE);
    }

    // Only animate the particles when the torch is lit.
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            super.animateTick(state, world, pos, random);
        }
    }

    // Whether the block ticks randomly.
    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    // Check if the torch is right-clicked with a lighting item of some sort.
    // If so, damage it and light the torch only if it isn't raining
    // where the torch resides or if the lighting item was successful.
    // Otherwise, do nothing.
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        // Do absolutely nothing if the torch is already lit, or if relighting is not allowed and the torch is still lit.
        if (state.get(LIT) && state.get(AGE) == 15 || !RELIGHT_ALLOWED && state.get(LIT)) {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
        // Set the current hand and prep the lighting item for animation.
        // NEEDED FOR .addPropertyGetter() method's active hand method.

        player.setActiveHand(hand);

        // The torch is being right clicked by a player holding a lighting item.
        // Lighting items should have varying degrees of usefulness, so configure
        // them properly using the maximum strike count for each item in config.
        if (player.getHeldItem(hand).getItem() instanceof LightingItem) {

            playLightingSound(world, pos);

            // Light it up immediately if the player is in creative and return.
            if (player.isCreative()) {
                changeBlockStateToLit(state, world, pos);
                return ActionResultType.SUCCESS;
            }

            // Damage the lighting item, breaking it if it loses all its durability.
            ItemStack heldStack = player.getHeldItem(hand);
            heldStack.damageItem(1, player, playerEntity -> {
                playerEntity.sendBreakAnimation(hand);
            });

            // If the world is raining on this torch, play the extinguish sound and do nothing else.
            if (world.isRainingAt(pos)) {
                playExtinguishSound(world, pos);
                return ActionResultType.PASS;
            }

            if (state.get(AGE) >= 1 && RELIGHT_ALLOWED) {
                changeBlockStateToLit(state, world, pos);
                return ActionResultType.SUCCESS;
            }

            // At a random chance, if configured, check if the random number equals zero.
            // If so, light the torch and reset the amount of strikes for when it burns out again.

            // If you are unsuccessful, increase the amount of strikes and do nothing.

            // The chance should be more likely the more times you strike the torch,
            // meaning that it should be less common to have a lot of strikes to light the torch.
            if (player.getHeldItem(hand).getItem() == ModItems.FLINT_AND_TINDER) {
                if (world.rand.nextInt(MAX_STRIKES_TINDER) - currentStrikes <= 0) {
                    changeBlockStateToLit(state, world, pos);
                    currentStrikes = 0;
                    return ActionResultType.SUCCESS;
                } else {
                    currentStrikes++;
                    return ActionResultType.CONSUME;
                }
            }
            if (player.getHeldItem(hand).getItem() == ModItems.BOW_DRILL) {
                if (world.rand.nextInt(MAX_STRIKES_BOW_DRILL) - currentStrikes <= 0) {
                    changeBlockStateToLit(state, world, pos);
                    currentStrikes = 0;
                    return ActionResultType.SUCCESS;
                } else {
                    currentStrikes++;
                    return ActionResultType.CONSUME;
                }
            }
        }

        // Flint and Steel is much more reliable, so should light up the torch
        // in one shot if the torch is away from rain. No need to employ strike counts.
        if (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
            playLightingSound(world, pos);
            if (!player.isCreative()) {
                ItemStack heldStack = player.getHeldItem(hand);
                heldStack.damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(hand);
                });
            }
            if (world.isRainingAt(pos)) {
                playExtinguishSound(world, pos);
                return ActionResultType.PASS;
            }
            if (!world.isRainingAt(pos) && !state.get(LIT) || RELIGHT_ALLOWED && state.get(AGE) >= 1) {
                changeBlockStateToLit(state, world, pos);
            }
            return ActionResultType.SUCCESS;
        }
        ImmersiveLighting.LOGGER.info(world.getGameTime());
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        checkTorchIsValid(state, world, pos);
        if (!(state.get(AGE) <= 0)) {
            if (world.rand.nextDouble() <= CHANCE_TO_DIM) {
                int newAge = state.get(AGE) - 1;
                world.setBlockState(pos, state.with(AGE, newAge));
            }
        }
        if (state.get(AGE) == 0 && state.get(LIT)) {
            playExtinguishSound(world, pos);
            changeBlockStateToUnlit(state, world, pos);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            this.updateNeighbors(state, worldIn, pos, 3);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    // Check if we are raining and we are lit. If so, put out the torch.
    public void checkTorchIsValid(BlockState state, World world, BlockPos pos) {
        if (world.isRainingAt(pos) && state.get(LIT)) {
            playExtinguishSound(world, pos);
            changeBlockStateToUnlit(state, world, pos);
        }
    }

    // Only called when the torch is lit, so schedule the proper time to burn out with it.
    public void changeBlockStateToLit(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, ModBlocks.WALL_TORCH.getDefaultState().with(LIT, true).with(AGE, 15).with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)));
    }

    public void changeBlockStateToUnlit(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, ModBlocks.WALL_TORCH.getDefaultState().with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)));

    }

    public void playLightingSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 0.9F, world.rand.nextFloat() * 0.1F + 0.9F);
    }

    public void playExtinguishSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.9F, world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIT, AGE);
    }

}
