package mod.wittywhiscash.immersivelighting.compat.waila;

import mcp.mobius.waila.api.*;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.BaseText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.stream.Stream;

public class ImmersiveLightingWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(new IComponentProvider() {
            @Override
            public void appendBody(List<Text> tooltip, IDataAccessor accessor, IPluginConfig config) {
                Block block = accessor.getBlock();
                BlockState state = accessor.getBlockState();
                if (!state.get(Properties.LIT)) {
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(Formatting.RED)));
                }
                else {
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(Formatting.DARK_GREEN)));
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_time", ((ImmersiveTorchBlock)block).getAgeInstance(state)).setStyle(new Style().setColor(Formatting.GOLD)));
                }

            }
        }, TooltipPosition.BODY, ImmersiveTorchBlock.class);

        registrar.registerComponentProvider(new IComponentProvider() {
            @Override
            public void appendBody(List<Text> tooltip, IDataAccessor accessor, IPluginConfig config) {
                Block block = accessor.getBlock();
                BlockState state = accessor.getBlockState();
                if (!state.get(Properties.LIT)) {
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(Formatting.RED)));
                }
                else {
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(Formatting.DARK_GREEN)));
                    tooltip.add(new TranslatableText("tooltip.immersivelighting.waila_time", ((ImmersiveWallTorchBlock)block).getAgeInstance(state)).setStyle(new Style().setColor(Formatting.GOLD)));
                }
            }
        }, TooltipPosition.BODY, ImmersiveWallTorchBlock.class);
    }
}
