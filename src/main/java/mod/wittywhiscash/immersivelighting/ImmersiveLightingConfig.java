package mod.wittywhiscash.immersivelighting;


import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.util.math.MathHelper;

@Config(name = ImmersiveLighting.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/spruce_planks.png")
public class ImmersiveLightingConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public DurabilityModule durability = new DurabilityModule();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public LightingModule lighting = new LightingModule();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public DebugModule debug = new DebugModule();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public WorldgenModule worldgen = new WorldgenModule();

    @Config(name = "durability_module")
    public static class DurabilityModule implements ConfigData {

        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @Comment("How much durability the flint and tinder item has. Min: 1, Max: 64, Default: 16")
        public int flint_tinder_durability = 16;

        @Override
        public void validatePostLoad() throws ValidationException {
            if (flint_tinder_durability <= 0 || flint_tinder_durability > 64) {
               flint_tinder_durability = MathHelper.clamp(flint_tinder_durability, 1, 64);
            }
        }
    }

    @Config(name = "lighting_module")
    public static class LightingModule implements ConfigData {

        @Comment("How much time it takes, in minutes, for a torch to burn out. Min: 1, Max: 1440 (24 hours), Default: 60")
        public int torch_timeUntilBurnout = 60;

        @Comment("How many times maximum it can take for a flint and tinder to light a torch. Min: 1, Max: 16, Default: 8")
        public int torch_flintTinderMaxStrikes = 8;

        @Override
        public void validatePostLoad() throws ValidationException {
            if (torch_timeUntilBurnout <= 0 || torch_timeUntilBurnout > 1440) {
                torch_timeUntilBurnout = MathHelper.clamp(torch_timeUntilBurnout, 1, 1440);
            }
            if (torch_flintTinderMaxStrikes <= 0 || torch_flintTinderMaxStrikes > 16) {
                torch_flintTinderMaxStrikes = MathHelper.clamp(torch_flintTinderMaxStrikes, 1, 16);
            }
        }

    }

    @Config(name = "debug_module")
    public static class DebugModule implements ConfigData {

        @Comment("Whether block/item registration debug messages should be shown.")
        public boolean showRegistrationDebug = false;

        @Comment("Whether loot table injection debug messages should be shown.")
        public boolean showLootTableDebug = false;

        @Comment("Whether torch update debug messages should be shown. Can cause log spam if a lot of torches are in the area.")
        public boolean showTorchUpdateDebug = false;
    }

    @Config(name = "worldgen_module")
    public static class WorldgenModule implements ConfigData {

        @Comment("Whether immersive torches should replace normal torches on worldgen.")
        public boolean replaceTorchesOnGen = true;

        @Comment("Whether the immersive torches generated should start lit or not.")
        public boolean startLitOnGen = true;

    }

}
