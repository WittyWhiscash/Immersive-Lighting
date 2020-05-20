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

        @Comment("How much durability the flint and tinder item has. Min: 1, Max: 64, Default: 16")
        public int flint_tinder_durability = 16;

        @Comment("How much durability the bow drill item has. Min: 1, Max: 128, Default: 32")
        public int bowDrill_durability = 32;

        @Override
        public void validatePostLoad() throws ValidationException {
            if (flint_tinder_durability <= 0 || flint_tinder_durability > 64) {
               flint_tinder_durability = MathHelper.clamp(flint_tinder_durability, 1, 64);
            }
            if (bowDrill_durability <= 0 || bowDrill_durability > 128) {
                bowDrill_durability = MathHelper.clamp(bowDrill_durability, 1, 128);
            }
        }
    }

    @Config(name = "lighting_module")
    public static class LightingModule implements ConfigData {

        @Comment("The chance, every random tick, that a torch will dim and shed one less light level. Min: 0.01, Max: 1, Default: 0.25")
        public double torch_chanceToDim = 0.25;

        @Comment("If set to true, will allow the player to relight torches that are in the middle of burning. If set to false, torches must burn out before lighting again.")
        public boolean torch_relightAllowed = false;

        @Comment("How many times maximum it can take for a flint and tinder to light a torch. Min: 1, Max: 16, Default: 8")
        public int torch_flintTinderMaxStrikes = 8;

        @Comment("How many times maximum it can take for the bow drill to light a torch. Min: 1, Max: 8, Default: 4")
        public int torch_bowDrillMaxStrikes = 4;

        @Override
        public void validatePostLoad() throws ValidationException {
            if (torch_chanceToDim <= 0 || torch_chanceToDim > 1.0) {
                torch_chanceToDim = MathHelper.clamp(torch_chanceToDim, 0.01, 1.0);
            }
            if (torch_flintTinderMaxStrikes <= 0 || torch_flintTinderMaxStrikes > 16) {
                torch_flintTinderMaxStrikes = MathHelper.clamp(torch_flintTinderMaxStrikes, 1, 16);
            }
            if (torch_bowDrillMaxStrikes <= 0 || torch_bowDrillMaxStrikes > 8) {
                torch_bowDrillMaxStrikes = MathHelper.clamp(torch_bowDrillMaxStrikes, 1, 8);
            }
        }

    }

    @Config(name = "debug_module")
    public static class DebugModule implements ConfigData {

        @Comment("Whether block/item registration debug messages should be shown.")
        public boolean showRegistrationDebug = false;

        @Comment("Whether loot table injection debug messages should be shown.")
        public boolean showLootTableDebug = false;
    }

    @Config(name = "worldgen_module")
    public static class WorldgenModule implements ConfigData {

        @Comment("Whether immersive torches should replace normal torches on worldgen.")
        public boolean replaceTorchesOnGen = true;

        @Comment("Whether the immersive torches generated should start lit or not.")
        public boolean startLitOnGen = true;

    }

}
