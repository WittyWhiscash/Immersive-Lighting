package mod.wittywhiscash.immersivelighting.worldgen;

import com.mojang.datafixers.Dynamic;
import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class TorchFeature extends Feature<DefaultFeatureConfig> {

    public TorchFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable blockPos$mutable = new BlockPos.Mutable();
        int startX = pos.getX();
        int startZ = pos.getZ();
        if (ImmersiveLighting.CONFIG.worldgen.replaceTorchesOnGen) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        blockPos$mutable.set(startX + x, y, startZ + z);
                        if (world.getBlockState(blockPos$mutable).getBlock() == Blocks.TORCH) {
                            if (ImmersiveLighting.CONFIG.worldgen.startLitOnGen) {
                                world.setBlockState(blockPos$mutable, ImmersiveLighting.TORCH_BLOCK.getDefaultState().with(ImmersiveTorchBlock.getLitProperty(), true).with(ImmersiveTorchBlock.getAgeProperty(), ImmersiveTorchBlock.getBurnoutTime()), 3);
                                world.getBlockTickScheduler().schedule(blockPos$mutable, world.getBlockState(blockPos$mutable).getBlock(), world.getBlockState(blockPos$mutable).getBlock().getTickRate(world));
                            }
                            else {
                                world.setBlockState(blockPos$mutable, ImmersiveLighting.TORCH_BLOCK.getDefaultState(), 3);
                            }
                        }
                        if (world.getBlockState(blockPos$mutable).getBlock() == Blocks.WALL_TORCH) {
                            if (ImmersiveLighting.CONFIG.worldgen.startLitOnGen) {
                                world.setBlockState(blockPos$mutable, ImmersiveLighting.WALL_TORCH_BLOCK.getDefaultState().with(ImmersiveWallTorchBlock.getLitProperty(), true).with(ImmersiveWallTorchBlock.getAgeProperty(), ImmersiveWallTorchBlock.getBurnoutTime()).with(ImmersiveWallTorchBlock.FACING, world.getBlockState(blockPos$mutable).get(WallTorchBlock.FACING)), 3);
                                world.getBlockTickScheduler().schedule(blockPos$mutable, world.getBlockState(blockPos$mutable).getBlock(), world.getBlockState(blockPos$mutable).getBlock().getTickRate(world));
                            }
                            else {
                                world.setBlockState(blockPos$mutable, ImmersiveLighting.WALL_TORCH_BLOCK.getDefaultState().with(WallTorchBlock.FACING, world.getBlockState(blockPos$mutable).get(WallTorchBlock.FACING)), 3);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
