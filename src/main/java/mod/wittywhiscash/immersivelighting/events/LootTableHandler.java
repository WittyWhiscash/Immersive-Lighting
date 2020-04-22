package mod.wittywhiscash.immersivelighting.events;

import mod.wittywhiscash.immersivelighting.Config;
import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootTableHandler {

    // References to loot tables for comparing against when the loot table is loaded.

    // Grass/Fern Drops
    private static ResourceLocation grass_drops = new ResourceLocation("minecraft", "blocks/grass");
    private static ResourceLocation fern_drops = new ResourceLocation("minecraft", "blocks/fern");
    private static ResourceLocation tallgrass_drops = new ResourceLocation("minecraft", "blocks/tall_grass");

    // Leaves Drops
    private static ResourceLocation acacialeaves_drops = new ResourceLocation("minecraft", "blocks/acacia_leaves");
    private static ResourceLocation birchleaves_drops = new ResourceLocation("minecraft", "blocks/birch_leaves");
    private static ResourceLocation darkoakleaves_drops = new ResourceLocation("minecraft", "blocks/dark_oak_leaves");
    private static ResourceLocation jungleleaves_drops = new ResourceLocation("minecraft", "blocks/jungle_leaves");
    private static ResourceLocation oakleaves_drops = new ResourceLocation("minecraft", "blocks/oak_leaves");
    private static ResourceLocation spruceleaves_drops = new ResourceLocation("minecraft", "blocks/spruce_leaves");

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(grass_drops) || event.getName().equals(fern_drops) || event.getName().equals(tallgrass_drops)) {
            event.getTable().addPool(LootPool.builder()
                    .addEntry(TableLootEntry.builder(new ResourceLocation(ImmersiveLighting.MODID, "blocks/grass_drops")))
                    .name("il_grass_drops").build());
            if (Config.DEBUG_SHOWDEBUG.get()) {
                ImmersiveLighting.LOGGER.debug("Added Grass Drops to Grass Drop Loot Tables.");
            }
        }
        if (event.getName().equals(acacialeaves_drops) || event.getName().equals(birchleaves_drops) || event.getName().equals(darkoakleaves_drops) || event.getName().equals(jungleleaves_drops) || event.getName().equals(oakleaves_drops) || event.getName().equals(spruceleaves_drops)) {
            event.getTable().addPool(LootPool.builder()
                    .addEntry(TableLootEntry.builder(new ResourceLocation(ImmersiveLighting.MODID, "blocks/leaves_drops")))
                    .name("il_leaves_drops").build());
            if (Config.DEBUG_SHOWDEBUG.get()) {
                ImmersiveLighting.LOGGER.debug("Added Leaves Drops to Leaves Drop Loot Tables.");
            }
        }
    }
}
