package mod.wittywhiscash.immersivelighting.compat;

import mcp.mobius.waila.api.*;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveTorchBlock;
import mod.wittywhiscash.immersivelighting.blocks.ImmersiveWallTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

@WailaPlugin
public class ImmersiveLightingWailaPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(new IComponentProvider() {
            @Override
            public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
                Block block = accessor.getBlock();
                BlockState state = accessor.getBlockState();
                if (!state.get(BlockStateProperties.LIT)) {
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(TextFormatting.RED)));
                }
                else {
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_time", ((ImmersiveTorchBlock)block).getAgeInstance(state)).setStyle(new Style().setColor(TextFormatting.GOLD)));
                }
            }
        }, TooltipPosition.BODY, ImmersiveTorchBlock.class);

        registrar.registerComponentProvider(new IComponentProvider() {
            @Override
            public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
                Block block = accessor.getBlock();
                BlockState state = accessor.getBlockState();
                if (!state.get(BlockStateProperties.LIT)) {
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(TextFormatting.RED)));
                }
                else {
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_lit").setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
                    tooltip.add(new TranslationTextComponent("tooltip.immersivelighting.waila_time", ((ImmersiveWallTorchBlock)block).getAgeInstance(state)).setStyle(new Style().setColor(TextFormatting.GOLD)));
                }
            }
        }, TooltipPosition.BODY, ImmersiveWallTorchBlock.class);
    }
}
