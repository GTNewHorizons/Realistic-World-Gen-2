package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ConfigRWG.MOD_ID, category = "landmark decorations")
@Config.Comment("Centerpieces placed on top of RWG's landmark pillars. These only apply to chunks generated after the "
        + "change.")
public class LandmarkDecorationsConfig {

    @Config.Name("Twilight Forest Portal Chance")
    @Config.Comment("Chance for an eligible river junction landmark to carry a ready-made Twilight Forest portal "
            + "instead of a LootGames puzzle. 0 disables these portals entirely. Requires Twilight Forest.")
    @Config.DefaultFloat(0.25f)
    @Config.RangeFloat(min = 0f, max = 1f)
    @Config.RequiresMod("TwilightForest")
    public static float twilightForestPortalChance;

    @Config.Name("Twilight Forest Portal Unlit Chance")
    @Config.Comment("Chance for one of those portals to generate as a plain water pool that a player still has to "
            + "light with Twilight Forest's activation item. 1 leaves every pool unlit.")
    @Config.DefaultFloat(0.5f)
    @Config.RangeFloat(min = 0f, max = 1f)
    @Config.RequiresMod("TwilightForest")
    public static float twilightForestPortalUnlitChance;

    @Config.Name("Twilight Forest Portal Minimum Spawn Distance")
    @Config.Comment("Twilight Forest portals are not placed within this many blocks of x=0, z=0. "
            + "Junctions inside the radius fall back to a LootGames puzzle.")
    @Config.DefaultInt(3500)
    @Config.RangeInt(min = 0, max = 30000000)
    @Config.RequiresMod("TwilightForest")
    public static int twilightForestPortalMinimumSpawnDistance;
}
