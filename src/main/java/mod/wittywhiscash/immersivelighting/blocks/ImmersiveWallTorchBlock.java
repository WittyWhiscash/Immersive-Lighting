package mod.wittywhiscash.immersivelighting.blocks;

import mod.wittywhiscash.immersivelighting.Config;
import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class ImmersiveWallTorchBlock extends WallTorchBlock {

    private static int secondCounter = 60;
    private static int burnoutTime = Config.TORCH_TIMEUNTILBURNOUT.get();
    private static final IntegerProperty AGE = IntegerProperty.create("age", 0, burnoutTime);
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected ImmersiveWallTorchBlock() {
        super(Block.Properties.from(Blocks.WALL_TORCH));
        this.setDefaultState(this.getDefaultState().with(LIT, false).with(AGE, 0));
    }

    public static IntegerProperty getAgeProperty() {
        return AGE;
    }

    public IntegerProperty getAgeInstance() { return AGE; }

    public static BooleanProperty getLitProperty() {
        return LIT;
    }

    public static int getBurnoutTime() {
        return burnoutTime;
    }

    // Set the light value to 14 when it is lit. Otherwise, it emits no light.
    @Override
    public int getLightValue(BlockState state) {
        if (state.get(LIT)) {
            return 14;
        }
        else return 0;
    }

    // Only animate the particles when the torch is lit.
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        else {
            super.animateTick(state, world, pos, random);
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        // Check if the block was right clicked with a flint and steel. If so, change the state to a lit torch, and damage the flint and steel.
        if (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
            playLightingSound(world, pos);
            if (!player.isCreative()) {
                ItemStack heldStack = player.getHeldItem(hand);
                heldStack.damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(hand);
                });}
            // Double check that it isn't raining. If so, play the extinguish sound. Otherwise, we are good to go. Light it.
            if (world.isRainingAt(pos)) {
                playExtinguishSound(world, pos);
            }
            else {
                changeBlockStateToLit(world, pos, state);
            }
            return true;
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Override
    public int tickRate(IWorldReader worldReader) {
        return 40;
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random) {
        if (!world.isRemote()) {
            boolean isTorchNotValid = checkTorchIsValid(state, world, pos);
            if (!state.get(LIT) || isTorchNotValid) {
                return;
            }
            secondCounter--;
            if (secondCounter != 0) {
                world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
                return;
            }
            int newAge = state.get(AGE) - 1;
            if (newAge <= 0) {
                newAge = 0;
            }
            if (newAge == 0) {
                playExtinguishSound(world, pos);
                changeBlockStateToUnlit(world, pos, state);
                world.notifyNeighbors(pos, this);
                return;
            }
            if (Config.DEBUG_SHOWDEBUG.get()) {
                ImmersiveLighting.LOGGER.debug("Torch Block at " + pos.getX() + " " + pos.getZ() + " is updating");
            }
            world.setBlockState(pos, state.with(AGE, newAge));
            world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
            secondCounter = 60;
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            this.updateNeighbors(state, worldIn, pos, 3);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public boolean checkTorchIsValid(BlockState state, World world, BlockPos pos) {
        // Check if we are raining and we are lit. If so, put out the torch.
        if (world.isRainingAt(pos) && state.get(LIT)) {
            playExtinguishSound(world, pos);
            changeBlockStateToUnlit(world, pos, state);
            return true;
        }
        else return false;
    }

    public void changeBlockStateToLit(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, ModBlocks.WALL_TORCH.getDefaultState().with(LIT, true).with(AGE, burnoutTime).with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)));
        world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    }

    public void changeBlockStateToUnlit(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, ModBlocks.WALL_TORCH.getDefaultState().with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)));
        world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
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
        builder.add(AGE);
        builder.add(LIT);
    }

}
