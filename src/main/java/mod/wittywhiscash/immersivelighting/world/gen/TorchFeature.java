package mod.wittywhiscash.immersivelighting.world.gen;

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

    private static final boolean REPLACE_TORCHES = Config.WORLDGEN_REPLACETORCHES.get();
    private static final boolean START_LIT = Config.WORLDGEN_STARTLIT.get();

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable blockPos$mutable = new BlockPos.Mutable();
        int startX = pos.getX();
        int startZ = pos.getZ();
        if (REPLACE_TORCHES) {
            for (int x = 0; x < 16; ++x) {
                for (int y = 0; y < worldIn.getMaxHeight(); ++y) {
                    for (int z = 0; z < 16; ++z) {
                        blockPos$mutable.setPos(startX + x, y, startZ + z);
                        if (worldIn.getBlockState(blockPos$mutable).getBlock() == Blocks.TORCH) {
                            if (START_LIT) {
                                worldIn.setBlockState(blockPos$mutable, ModBlocks.TORCH.getDefaultState().with(ImmersiveTorchBlock.getLitProperty(), true).with(ImmersiveTorchBlock.getAgeProperty(), 15), 3);
                            }
                            else worldIn.setBlockState(blockPos$mutable, ModBlocks.TORCH.getDefaultState(), 3);
                        }
                        if (worldIn.getBlockState(blockPos$mutable).getBlock() == Blocks.WALL_TORCH) {
                            if (START_LIT){
                                worldIn.setBlockState(blockPos$mutable, ModBlocks.WALL_TORCH.getDefaultState().with(ImmersiveWallTorchBlock.getLitProperty(), true).with(ImmersiveWallTorchBlock.getAgeProperty(), 15).with(BlockStateProperties.HORIZONTAL_FACING, worldIn.getBlockState(blockPos$mutable).get(BlockStateProperties.HORIZONTAL_FACING)), 3);
                            }
                            else worldIn.setBlockState(blockPos$mutable, ModBlocks.WALL_TORCH.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, worldIn.getBlockState(blockPos$mutable).get(BlockStateProperties.HORIZONTAL_FACING)), 3);
                        }
                    }
                }
            }
        }
        return true;
    }
}
