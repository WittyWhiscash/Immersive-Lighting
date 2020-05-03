package mod.wittywhiscash.immersivelighting.items;

import net.minecraft.item.*;
import net.minecraft.util.Identifier;

public class LightingItem extends Item {

    public LightingItem(int maxDamage) {
        super(new Item.Settings().group(ItemGroup.TOOLS).maxDamage(maxDamage));
        this.addPropertyGetter(new Identifier("immersivelighting", "lighting"), (stack, world, entity) -> {
            return entity != null && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }
}
