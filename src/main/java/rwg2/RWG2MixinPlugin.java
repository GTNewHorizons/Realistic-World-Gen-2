package rwg2;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import rwg2.config.ConfigRWG;

/** Skips debug-only mixins unless enabled in the config, which Forge has not loaded yet at this point. */
public final class RWG2MixinPlugin implements IMixinConfigPlugin {

    private static final Set<String> DIAGNOSTIC_MIXINS = new HashSet<String>(
            Arrays.asList("MixinWorld", "MixinMapGenStructure", "MixinMapGenVillage"));

    private static Boolean diagnostics;

    static boolean diagnosticsEnabled() {
        load();
        return diagnostics;
    }

    private static synchronized void load() {
        if (diagnostics != null) return;
        diagnostics = false;
        File file = new File(new File(Launch.minecraftHome, "config"), "rwg2.cfg");
        if (!file.isFile()) return;
        Configuration config = new Configuration(file);
        diagnostics = config.get(ConfigRWG.DEBUG_CATEGORY, ConfigRWG.ENABLE_WORLDGEN_DIAGNOSTICS, false).getBoolean();
    }

    @Override
    public void onLoad(String mixinPackage) {
        load();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !DIAGNOSTIC_MIXINS.contains(mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1))
                || diagnostics;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
