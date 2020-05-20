package mod.wittywhiscash.immersivelighting.blocks;

import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import mod.wittywhiscash.immersivelighting.items.LightingItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ImmersiveWallTorchBlock extends WallTorchBlock {

    // The variable chance to dim (in decimal format, so 25% is actually 0.25) a torch on a random tick.
    private static final double CHANCE_TO_DIM = ImmersiveLighting.CONFIG.lighting.torch_chanceToDim;

    // Variables to track the amount of strikes the player has striked with a lighting item.
    private final int MAX_STRIKES_TINDER = ImmersiveLighting.CONFIG.lighting.torch_flintTinderMaxStrikes;
    private final int MAX_STRIKES_BOW_DRILL = ImmersiveLighting.CONFIG.lighting.torch_bowDrillMaxStrikes;
    private int currentStrikes = 0;

    private static final boolean RELIGHT_ALLOWED = ImmersiveLighting.CONFIG.lighting.torch_relightAllowed;

    private static final BooleanProperty LIT = Properties.LIT;
    private static final IntProperty AGE = Properties.AGE_15;

    public ImmersiveWallTorchBlock() {
        super(Settings.copy(Blocks.WALL_TORCH));
        setDefaultState(getDefaultState().with(LIT, false).with(AGE, 0));
    }

    public static BooleanProperty getLitProperty() { return LIT; }

    public int getAgeInstance(BlockState state) { return state.get(AGE); }

    public static IntProperty getAgeProperty() { return AGE; }

    // The light value equals the age of the torch. Higher age means more light shed.
    @Override
    public int getLuminance(BlockState state) {
        return state.get(AGE);
    }

    // Only animate the particles when the torch is lit.
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    // Whether the block ticks randomly.
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    // Check if the torch is right-clicked with a lighting item of some sort.
    // If so, damage it and light the torch only if it isn't raining
    // where the torch resides or if the lighting item was successful.
    // Otherwise, do nothing.
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // Do absolutely nothing if the torch is already lit, or if relighting is not allowed and the torch is still lit.
        if (state.get(LIT) && state.get(AGE) == 15 || !RELIGHT_ALLOWED && state.get(LIT)) {
            return super.onUse(state, world, pos, player, hand, hit);
        }

        // The torch is being right clicked by a player holding a lighting item.
        // Lighting items should have varying degrees of usefulness, so configure
        // them properly using the maximum strike count for each item in config.
        if (player.getStackInHand(hand).getItem() instanceof LightingItem) {

            // Set the current hand and prep the lighting item for animation.
            // NEEDED FOR .addPropertyGetter() method's active hand method.
            player.setCurrentHand(hand);

            playLightingSound(world, pos);

            // Light it up immediately if the player is in creative and return.
            if (player.isCreative()) {
                changeBlockStateToLit(state, world, pos);
                return ActionResult.SUCCESS;
            }

            // Damage the lighting item, breaking it if it loses all its durability.
            ItemStack heldStack = player.getStackInHand(hand);
            heldStack.damage(1, player, playerEntity -> {
                playerEntity.sendToolBreakStatus(hand);
            });

            // If the world is raining on this torch, play the extinguish sound and do nothing else.
            if (world.hasRain(pos)) {
                playExtinguishSound(world, pos);
                return ActionResult.PASS;
            }

            if (state.get(LIT)) {
                changeBlockStateToLit(state, world, pos);
                return ActionResult.SUCCESS;
            }

            // At a random chance, if configured, check if the random number equals zero.
            // If so, light the torch and reset the amount of strikes for when it burns out again.

            // If you are unsuccessful, increase the amount of strikes and do nothing.

            // The chance should be more likely the more times you strike the torch,
            // meaning that it should be less common to have a lot of strikes to light the torch.
            else if (player.getStackInHand(hand).getItem() == ImmersiveLighting.FLINT_AND_TINDER) {
                if (world.random.nextInt(MAX_STRIKES_TINDER) - currentStrikes <= 0) {
                    changeBlockStateToLit(state, world, pos);
                    currentStrikes = 0;
                    return ActionResult.SUCCESS;
                } else {
                    currentStrikes++;
                    return ActionResult.CONSUME;
                }
            }
            else if (player.getStackInHand(hand).getItem() == ImmersiveLighting.BOW_DRILL) {
                if (world.random.nextInt(MAX_STRIKES_BOW_DRILL) - currentStrikes <= 0) {
                    changeBlockStateToLit(state, world, pos);
                    currentStrikes = 0;
                    return ActionResult.SUCCESS;
                } else {
                    currentStrikes++;
                    return ActionResult.CONSUME;
                }
            }
        }

        // Flint and Steel is much more reliable, so should light up the torch
        // in one shot if the torch is away from rain. No need to employ strike counts.
        if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL) {
            playLightingSound(world, pos);
            if (!player.isCreative()) {
                ItemStack heldStack = player.getStackInHand(hand);
                heldStack.damage(1, player, playerEntity -> {
                    playerEntity.sendToolBreakStatus(hand);
                });
            }
            if (world.hasRain(pos)) {
                playExtinguishSound(world, pos);
                return ActionResult.PASS;
            }
            if (!world.hasRain(pos) && !state.get(LIT) || RELIGHT_ALLOWED && state.get(LIT)) {
                changeBlockStateToLit(state, world, pos);
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    // Called every so often to check we are still in a valid place to not be extinguished.
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        checkTorchIsValid(state, world, pos);
        if (!(state.get(AGE) <= 0)) {
            if (world.random.nextDouble() <= CHANCE_TO_DIM) {
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
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && state.getBlock() != newState.getBlock()) {
            this.updateNeighborStates(state, world, pos, 3);
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    // Check if we are raining and we are lit. If so, put out the torch.
    public boolean checkTorchIsValid(BlockState state, World world, BlockPos pos) {
        if (world.hasRain(pos) && state.get(LIT)) {
            playExtinguishSound(world, pos);
            changeBlockStateToUnlit(state, world, pos);
            return true;
        }
        else return false;
    }

    // Only called when the torch is lit, so schedule the proper time to burn out with it.
    public void changeBlockStateToLit(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, this.getDefaultState().with(LIT, true).with(AGE, 15).with(FACING, state.get(FACING)));;
    }

    public void changeBlockStateToUnlit(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, this.getDefaultState().with(FACING, state.get(FACING)));
    }

    public void playLightingSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 0.9F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    public void playExtinguishSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.9F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT, AGE);
    }
}
