package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ConfigRWG.MOD_ID, category = ConfigRWG.DEBUG_CATEGORY)
@Config.RequiresMcRestart
public class DebugConfig {

    /**
     * Read directly from the file by {@link rwg2.RWG2MixinPlugin}, since the diagnostic mixins apply before preInit.
     */
    @Config.Name(ConfigRWG.ENABLE_WORLDGEN_DIAGNOSTICS)
    @Config.Comment("Logs cross-chunk tree and village generation. Adds hooks to World.setBlock. Requires a restart.")
    @Config.DefaultBoolean(false)
    public static boolean enableWorldgenDiagnostics;
}
