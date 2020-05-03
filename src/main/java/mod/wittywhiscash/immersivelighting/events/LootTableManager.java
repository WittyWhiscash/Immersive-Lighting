package mod.wittywhiscash.immersivelighting.events;

import mod.wittywhiscash.immersivelighting.ImmersiveLighting;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

public class LootTableManager implements ModInitializer {

    private static Identifier grass_drops = new Identifier("minecraft", "blocks/grass");
    private static Identifier fern_drops = new Identifier("minecraft", "blocks/fern");
    private static Identifier tallgrass_drops = new Identifier("minecraft", "blocks/tall_grass");

    private static Identifier acacialeaves_drops = new Identifier("minecraft", "blocks/acacia_leaves");
    private static Identifier birchleaves_drops = new Identifier("minecraft", "blocks/birch_leaves");
    private static Identifier darkoakleaves_drops = new Identifier("minecraft", "blocks/dark_oak_leaves");
    private static Identifier jungleleaves_drops = new Identifier("minecraft", "blocks/jungle_leaves");
    private static Identifier oakleaves_drops = new Identifier("minecraft", "blocks/oak_leaves");
    private static Identifier spruceleaves_drops = new Identifier("minecraft", "blocks/spruce_leaves");

    @Override
    public void onInitialize() {
        LootTableLoadingCallback.EVENT.register(((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
            if (identifier.equals(grass_drops) || identifier.equals(fern_drops) || identifier.equals(tallgrass_drops)) {
                FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
                builder.withEntry(LootTableEntry.builder(new Identifier(ImmersiveLighting.MOD_ID, "blocks/grass_drops"))).build();
                fabricLootSupplierBuilder.withPool(builder);
                if (ImmersiveLighting.CONFIG.debug.showLootTableDebug) {
                    ImmersiveLighting.LOGGER.info("Appended tinder drops to %s", identifier.toString());
                }
            }
            if (identifier.equals(acacialeaves_drops) || identifier.equals(birchleaves_drops) || identifier.equals(darkoakleaves_drops) || identifier.equals(jungleleaves_drops) || identifier.equals(oakleaves_drops) || identifier.equals(spruceleaves_drops)) {
                FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
                builder.withEntry(LootTableEntry.builder(new Identifier(ImmersiveLighting.MOD_ID, "blocks/leaves_drops"))).build();
                fabricLootSupplierBuilder.withPool(builder);
                if (ImmersiveLighting.CONFIG.debug.showLootTableDebug) {
                    ImmersiveLighting.LOGGER.info("Appended tinder drops to %s", identifier.toString());
                }
            }
        }));
    }
}
