package mod.wittywhiscash.immersivelighting;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public String getModId() {
        return ImmersiveLighting.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen(ImmersiveLightingConfig.class, screen));
    }
}
