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
    private static final int FLINT_AND_TINDER_DURABILITY = Config.FLINTANDTINDER_DURABILITY.get();
    private static final int BOW_DRILL_DURABILITY = Config.BOWDRILL_DURABILITY.get();

    @ObjectHolder("immersive_torch") public static final Item TORCH = null;
    @ObjectHolder("flint_and_tinder") public static final Item FLINT_AND_TINDER = null;
    @ObjectHolder("tinder") public static final Item TINDER = null;
    @ObjectHolder("bow_drill") public static final Item BOW_DRILL = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistry) {
        itemRegistry.getRegistry().registerAll(
                new WallOrFloorItem(ModBlocks.TORCH, ModBlocks.WALL_TORCH, PROPERTIES).setRegistryName(ImmersiveLighting.getId("immersive_torch")),
                new LightingItem(FLINT_AND_TINDER_DURABILITY).setRegistryName(ImmersiveLighting.getId("flint_and_tinder")),
                new LightingItem(BOW_DRILL_DURABILITY).setRegistryName(ImmersiveLighting.getId("bow_drill")),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ImmersiveLighting.getId("tinder"))
        );
    }

}
