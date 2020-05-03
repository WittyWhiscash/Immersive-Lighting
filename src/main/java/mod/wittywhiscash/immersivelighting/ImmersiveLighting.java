package mod.wittywhiscash.immersivelighting;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import mod.wittywhiscash.immersivelighting.items.LightingItem;
import mod.wittywhiscash.immersivelighting.worldgen.TorchFeature;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class ImmersiveLighting implements ModInitializer, ClientModInitializer, ModMenuApi {

	public static final String MOD_ID = "immersivelighting";
	public static final ImmersiveLightingConfig CONFIG = AutoConfig.register(ImmersiveLightingConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new)).getConfig();
	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_ID);

	public static final Block TORCH_BLOCK = new ImmersiveTorchBlock();
	public static final Block WALL_TORCH_BLOCK = new ImmersiveWallTorchBlock();
	public static final Item TORCH_ITEM = new WallStandingBlockItem(TORCH_BLOCK, WALL_TORCH_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS));

	public static final Item TINDER = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item FLINT_AND_TINDER = new LightingItem(CONFIG.durability.flint_tinder_durability);

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "immersivetorch"), TORCH_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "immersivetorch_wall"), WALL_TORCH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "immersivetorch"), TORCH_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flint_and_tinder"), FLINT_AND_TINDER);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tinder"), TINDER);
		if (CONFIG.debug.showRegistrationDebug) {
			LOGGER.info("Registered blocks/items for %s", MOD_ID);
		}

		final Feature<DefaultFeatureConfig> torchFeature = Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "torch_feature"), new TorchFeature(DefaultFeatureConfig::deserialize));
		Registry.BIOME.forEach(biome -> {
			biome.addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, torchFeature.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(NopeDecoratorConfig.DEFAULT)));
		});
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(TORCH_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(WALL_TORCH_BLOCK, RenderLayer.getCutout());
	}

	@Override
	public String getModId() {
		return ImmersiveLighting.MOD_ID;
	}

	@Override
	public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
		return Optional.of(AutoConfig.getConfigScreen(ImmersiveLightingConfig.class, screen));
	}
}
