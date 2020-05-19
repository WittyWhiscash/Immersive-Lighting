package mod.wittywhiscash.immersivelighting.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class LightingItem extends Item {

    public LightingItem(int maxDamage) {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(maxDamage).defaultMaxDamage(maxDamage));
        this.addPropertyOverride(new ResourceLocation("immersivelighting", "lighting"), (stack, world, entity) -> {
            return entity != null && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
        });
    }

}
