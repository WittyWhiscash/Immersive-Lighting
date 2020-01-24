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
    private static ResourceLocation grass_drops = new ResourceLocation("minecraft", "blocks/grass");
    private static ResourceLocation fern_drops = new ResourceLocation("minecraft", "blocks/fern");
    private static ResourceLocation tallgrass_drops = new ResourceLocation("minecraft", "blocks/tall_grass");

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
    }
}
