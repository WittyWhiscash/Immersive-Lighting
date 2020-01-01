package mod.wittywhiscash.realistictorchesreborn.items;

import mod.wittywhiscash.realistictorchesreborn.RealisticTorchesReborn;
import mod.wittywhiscash.realistictorchesreborn.blocks.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(RealisticTorchesReborn.MODID)
public class ModItems {

    private static final Item.Properties PROPERTIES = new Item.Properties().group(ItemGroup.DECORATIONS);

    @ObjectHolder("realistic_torch") public static final Item TORCH = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistry) {
        itemRegistry.getRegistry().registerAll(
                new WallOrFloorItem(ModBlocks.TORCH, ModBlocks.WALL_TORCH, PROPERTIES).setRegistryName(RealisticTorchesReborn.getId("realistic_torch"))
        );
    }

}
