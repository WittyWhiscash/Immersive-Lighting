package mod.wittywhiscash.immersivelighting.blocks;

import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
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
import net.minecraft.world.WorldView;
import org.lwjgl.system.CallbackI;

import java.util.Random;

public class ImmersiveWallTorchBlock extends WallTorchBlock {

    private int secondCounter = 60;
    private static int burnoutTime = ImmersiveLighting.CONFIG.lighting.torch_timeUntilBurnout;

    private static int maxStrikes_tinder = ImmersiveLighting.CONFIG.lighting.torch_flintTinderMaxStrikes;
    private int currentStrikes = 0;

    private static BooleanProperty LIT = Properties.LIT;
    private static IntProperty AGE = IntProperty.of("age", 0, burnoutTime);

    public ImmersiveWallTorchBlock() {
        super(Settings.copy(Blocks.WALL_TORCH));
        setDefaultState(getDefaultState().with(LIT, false).with(AGE, 0));
    }

    public static int getBurnoutTime() {
        return burnoutTime;
    }

    public IntProperty getAgeInstance() {
        return AGE;
    }

    public static IntProperty getAgeProperty() { return AGE; }

    public static BooleanProperty getLitProperty() { return LIT; }

    // Set the light value to 14 when it is lit. Otherwise, set the light value to 0.
    @Override
    public int getLuminance(BlockState state) {
        if (state.get(LIT)) {
            return 14;
        }
        else return 0;
    }

    // Only animate the particles when the torch is lit.
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        else {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
        world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
        super.onBlockAdded(state, world, pos, oldState, moved);
    }

    // Check if the torch is right-clicked with a lighting item of some sort.
    // If so, damage it and light the torch only if it isn't raining
    // where the torch resides or if the lighting item was successful.
    // Otherwise, do nothing.
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // Do absolutely nothing if the torch is already lit.
        if (state.get(LIT)) {
            return super.onUse(state, world, pos, player, hand, hit);
        }

        // The torch is being right clicked by a player holding Flint and Tinder.
        // Flint and Tinder is not as effective as other lighting measures, so
        // it should have the highest maximum strike count in config.
        if (player.getStackInHand(hand).getItem() == ImmersiveLighting.FLINT_AND_TINDER) {

            // Set the current hand and prep the Flint and Tinder for animation.
            // NEEDED FOR .addPropertyGetter() method's active hand method.
            player.setCurrentHand(hand);

            playLightingSound(world, pos);

            // Light it up immediately if the player is in creative and return.
            if (player.isCreative()) {
                changeBlockStateToLit(world, pos, state);
                return ActionResult.SUCCESS;
            }

            // Damage the Flint and Tinder, breaking it if it loses all its durability.
            ItemStack heldStack = player.getStackInHand(hand);
            heldStack.damage(1, player, playerEntity -> {
                playerEntity.sendToolBreakStatus(hand);
            });

            // If the world is raining on this torch, play the extinguish sound and do nothing else.
            if (world.hasRain(pos)) {
                playExtinguishSound(world, pos);
                return ActionResult.PASS;
            }

            // At a random chance, if configured, check if the random number equals zero.
            // If so, light the torch and reset the amount of strikes for when it burns out again.

            // If you are unsuccessful, increase the amount of strikes and do nothing.

            // The chance should be more likely the more times you strike the torch,
            // meaning that it should be less common to have a lot of strikes to light the torch.
            if (world.random.nextInt(maxStrikes_tinder) - currentStrikes <= 0) {
                changeBlockStateToLit(world, pos, state);
                currentStrikes = 0;
                return ActionResult.SUCCESS;
            }
            else {
                currentStrikes++;
                return ActionResult.CONSUME;
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
            }
            if (!world.hasRain(pos)) {
                changeBlockStateToLit(world, pos, state);
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    // Poll the block once every second.
    @Override
    public int getTickRate(WorldView worldView) {
        return 20;
    }

    // What the torch does every time it ticks.
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient()) {
            // Check if the torch is not lit or if it is lit and under rain. If so, do nothing.
            boolean isTorchNotValid = checkTorchIsValid(state, world, pos);
            if (!state.get(LIT) || isTorchNotValid) {
                return;
            }

            // Decrement the second counter. If it isn't at zero, schedule another tick
            // for the next second and do nothing else.
            secondCounter--;
            if (secondCounter != 0) {
                world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
                return;
            }

            // We've hit zero seconds on the clock. Do some sanity checking to make sure
            // we don't hit any sort of negative age integer.
            int newAge = state.get(AGE) - 1;
            if (newAge <= 0) {
                newAge = 0;
            }

            // Our minute counter has changed to zero. Time to put it out and let the blocks beside
            // it know we've been put out to update their lighting.
            if (newAge == 0) {
                playExtinguishSound(world, pos);
                changeBlockStateToUnlit(world, pos, state);
                world.updateNeighbors(pos, this);
                return;
            }

            if (ImmersiveLighting.CONFIG.debug.showTorchUpdateDebug) {
                ImmersiveLighting.LOGGER.info("Torch at %s is updating", pos.toShortString());
            }
            // The age counter is not zero, but some other number. Update the blockstate with the new age,
            // and schedule the next tick as well as resetting the second counter.
            world.setBlockState(pos, this.getDefaultState().with(LIT, true).with(AGE, newAge).with(FACING, state.get(FACING)));
            world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
            secondCounter = 60;
        }
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && state.getBlock() != newState.getBlock()) {
            this.updateNeighborStates(state, world, pos, 3);
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    public boolean checkTorchIsValid(BlockState state, World world, BlockPos pos) {
        // Check if we are raining and we are lit. If so, put out the torch.
        if (world.hasRain(pos) && state.get(LIT)) {
            playExtinguishSound(world, pos);
            changeBlockStateToUnlit(world, pos, state);
            return true;
        }
        else return false;
    }

    public void changeBlockStateToLit(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, this.getDefaultState().with(LIT, true).with(AGE, burnoutTime).with(FACING, state.get(FACING)));
        world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
    }

    public void changeBlockStateToUnlit(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, this.getDefaultState().with(FACING, state.get(FACING)));
        world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
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
        builder.add(LIT);
        builder.add(AGE);
    }
}
