package mod.wittywhiscash.immersivelighting;

import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ImmersiveLighting implements ModInitializer, ClientModInitializer {

	public static final String MOD_ID = "immersivelighting";

	public static final Block TORCH_BLOCK = new ImmersiveTorchBlock();
	public static final Block WALL_TORCH_BLOCK = new ImmersiveWallTorchBlock();
	public static final Item TORCH_ITEM = new WallStandingBlockItem(TORCH_BLOCK, WALL_TORCH_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "immersivetorch"), TORCH_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "immersivetorch_wall"), WALL_TORCH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "immersivetorch"), TORCH_ITEM);
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(TORCH_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(WALL_TORCH_BLOCK, RenderLayer.getCutout());
	}
}
