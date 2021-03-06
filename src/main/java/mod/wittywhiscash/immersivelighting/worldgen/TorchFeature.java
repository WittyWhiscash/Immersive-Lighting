package mod.wittywhiscash.immersivelighting.worldgen;

import com.mojang.datafixers.Dynamic;
import mod.wittywhiscash.immersivelighting.Config;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class TorchFeature extends Feature<NoFeatureConfig> {

    public TorchFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockPos.MutableBlockPos blockPos$mutableBlockPos = new BlockPos.MutableBlockPos();
        int startX = pos.getX();
        int startZ = pos.getZ();
        if (Config.WORLDGEN_REPLACETORCHES.get()) {
            for (int x = 0; x < 16; ++x) {
                for (int y = 0; y < worldIn.getMaxHeight(); ++y) {
                    for (int z = 0; z < 16; ++z) {
                        blockPos$mutableBlockPos.setPos(startX + x, y, startZ + z);
                        if (worldIn.getBlockState(blockPos$mutableBlockPos).getBlock() == Blocks.TORCH) {
                            if (Config.WORLDGEN_STARTLIT.get()) {
                                worldIn.setBlockState(blockPos$mutableBlockPos, ModBlocks.TORCH.getDefaultState().with(ImmersiveTorchBlock.getLitProperty(), true).with(ImmersiveTorchBlock.getAgeProperty(), ImmersiveTorchBlock.getBurnoutTime()), 3);
                                worldIn.getPendingBlockTicks().scheduleTick(blockPos$mutableBlockPos, worldIn.getBlockState(blockPos$mutableBlockPos).getBlock(), worldIn.getBlockState(blockPos$mutableBlockPos).getBlock().tickRate(worldIn));
                            }
                            else worldIn.setBlockState(blockPos$mutableBlockPos, ModBlocks.TORCH.getDefaultState(), 3);
                        }
                        if (worldIn.getBlockState(blockPos$mutableBlockPos).getBlock() == Blocks.WALL_TORCH) {
                            if (Config.WORLDGEN_STARTLIT.get()){
                                worldIn.setBlockState(blockPos$mutableBlockPos, ModBlocks.WALL_TORCH.getDefaultState().with(ImmersiveWallTorchBlock.getLitProperty(), true).with(ImmersiveWallTorchBlock.getAgeProperty(), ImmersiveWallTorchBlock.getBurnoutTime()).with(BlockStateProperties.HORIZONTAL_FACING, worldIn.getBlockState(blockPos$mutableBlockPos).get(BlockStateProperties.HORIZONTAL_FACING)), 3);
                                worldIn.getPendingBlockTicks().scheduleTick(blockPos$mutableBlockPos, worldIn.getBlockState(blockPos$mutableBlockPos).getBlock(), worldIn.getBlockState(blockPos$mutableBlockPos).getBlock().tickRate(worldIn));
                            }
                            else worldIn.setBlockState(blockPos$mutableBlockPos, ModBlocks.WALL_TORCH.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, worldIn.getBlockState(blockPos$mutableBlockPos).get(BlockStateProperties.HORIZONTAL_FACING)), 3);
                        }
                    }
                }
            }
        }
        return true;
    }
}
