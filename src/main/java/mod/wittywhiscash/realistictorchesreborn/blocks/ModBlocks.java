package mod.wittywhiscash.realistictorchesreborn.blocks;

import mod.wittywhiscash.realistictorchesreborn.RealisticTorchesReborn;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(RealisticTorchesReborn.MODID)
public class ModBlocks {

    @ObjectHolder("realistic_torch") public static final Block TORCH = null;
    @ObjectHolder("realistic_torch_wall") public static final Block WALL_TORCH = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistry) {
        blockRegistry.getRegistry().registerAll(
            new RealisticTorchBlock().setRegistryName(RealisticTorchesReborn.getId("realistic_torch")),
            new RealisticWallTorchBlock().setRegistryName(RealisticTorchesReborn.getId("realistic_torch_wall"))
        );
    }

}
