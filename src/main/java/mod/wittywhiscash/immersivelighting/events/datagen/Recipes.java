package mod.wittywhiscash.immersivelighting.events.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Blocks.TORCH, 4)
                .patternLine("G")
                .patternLine("S")
                .key('G', Items.GLOWSTONE_DUST)
                .key('S', Items.STICK)
                .setGroup("")
                .addCriterion("has_glowdust", hasItem(Items.GLOWSTONE_DUST))
                .build(consumer);

    }
}
