package mod.wittywhiscash.immersivelighting.items;

import mod.wittywhiscash.immersivelighting.Config;
import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import mod.wittywhiscash.immersivelighting.blocks.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ImmersiveLighting.MODID)
public class ModItems {

    private static final Item.Properties PROPERTIES = new Item.Properties().group(ItemGroup.DECORATIONS);

    @ObjectHolder("immersive_torch") public static final Item TORCH = null;
    @ObjectHolder("flint_and_tinder") public static final Item FLINT_AND_TINDER = null;
    @ObjectHolder("tinder") public static final Item TINDER = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistry) {
        itemRegistry.getRegistry().registerAll(
                new WallOrFloorItem(ModBlocks.TORCH, ModBlocks.WALL_TORCH, PROPERTIES).setRegistryName(ImmersiveLighting.getId("immersive_torch")),
                new LightingItem(Config.FLINTANDTINDER_DURABILITY.get()).setRegistryName(ImmersiveLighting.getId("flint_and_tinder")),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ImmersiveLighting.getId("tinder"))
        );
    }

}
