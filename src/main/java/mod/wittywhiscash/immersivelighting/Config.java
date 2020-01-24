package mod.wittywhiscash.immersivelighting;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue TORCH_TIMEUNTILBURNOUT;

    public static ForgeConfigSpec.IntValue FLINTANDTINDER_DURABILITY;

    public static ForgeConfigSpec.BooleanValue WORLDGEN_REPLACETORCHES;
    public static ForgeConfigSpec.BooleanValue WORLDGEN_STARTLIT;

    public static ForgeConfigSpec.BooleanValue DEBUG_SHOWDEBUG;

    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);

        TORCH_TIMEUNTILBURNOUT = COMMON_BUILDER.comment("The time, in minutes, it takes for the torch to burn out.")
                .defineInRange("torch_burnoutTime", 60, 1, 1440);

        FLINTANDTINDER_DURABILITY = COMMON_BUILDER.comment("The durability, in uses, of the flint and tinder.")
                .defineInRange("flintAndTinder_durability", 16, 1, 64);

        WORLDGEN_REPLACETORCHES = COMMON_BUILDER.comment("If set to true, the world will generate with this mod's lit torches as opposed to vanilla torches. If set to false, vanilla torches will generate.")
                .define("worldGen_replaceTorches", true);

        WORLDGEN_STARTLIT = COMMON_BUILDER.comment("If set to true, the generation will generate lit torches that eventually go out. If set to false, will generate unlit torches.")
                .define("worldGen_startLit", true);

        DEBUG_SHOWDEBUG = COMMON_BUILDER.comment("If set to true, will show what torches are updating and where they are in the world in the log. Will send lots of logging entries to the console.")
                .define("debug_showDebug", false);

        COMMON_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);

    }
}
