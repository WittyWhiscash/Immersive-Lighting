package mod.wittywhiscash.immersivelighting.items;

import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Resource;

public class LightingItem extends Item {

    public LightingItem(int maxDamage) {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(maxDamage).defaultMaxDamage(maxDamage));
        this.addPropertyOverride(new ResourceLocation("immersivelighting", "lighting"), (stack, world, entity) -> {
            return entity != null && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
        });
    }

}
