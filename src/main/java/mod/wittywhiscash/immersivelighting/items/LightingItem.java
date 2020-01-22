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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightingItem extends Item {

    public LightingItem(int maxDamage) {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(maxDamage).defaultMaxDamage(maxDamage));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        ItemStack heldItem = context.getItem();
        Block blockClicked = world.getBlockState(pos).getBlock();

        if (blockClicked instanceof ImmersiveTorchBlock) {
            if (!player.isCreative()) {
                heldItem.damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(context.getHand());
                });
            }
            ((ImmersiveTorchBlock) blockClicked).playLightingSound(world, pos);
            ((ImmersiveTorchBlock) blockClicked).changeBlockStateToLit(world, pos);
            return ActionResultType.SUCCESS;
        }
        else if (blockClicked instanceof ImmersiveWallTorchBlock) {
            if (!player.isCreative()) {
                heldItem.damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(context.getHand());
                });
            }
            ((ImmersiveWallTorchBlock) blockClicked).playLightingSound(world, pos);
            ((ImmersiveWallTorchBlock) blockClicked).changeBlockStateToLit(world, pos, world.getBlockState(pos).with(BlockStateProperties.HORIZONTAL_FACING, world.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING)));
            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(context);
    }
}
