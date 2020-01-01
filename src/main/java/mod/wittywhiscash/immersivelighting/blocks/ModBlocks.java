package mod.wittywhiscash.immersivelighting.blocks;

import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ImmersiveLighting.MODID)
public class ModBlocks {

    @ObjectHolder("immersive_torch") public static final Block TORCH = null;
    @ObjectHolder("immersive_torchwall") public static final Block WALL_TORCH = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistry) {
        blockRegistry.getRegistry().registerAll(
            new ImmersiveTorchBlock().setRegistryName(ImmersiveLighting.getId("immersive_torch")),
            new ImmersiveWallTorchBlock().setRegistryName(ImmersiveLighting.getId("immersive_torchwall"))
        );
    }

}
