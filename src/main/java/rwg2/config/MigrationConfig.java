package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ConfigRWG.MOD_ID, category = "expert / worldgen migration")
@Config.Comment("EXPERT ONLY! With the same seed, coordinates, and biome, RWG produces the same terrain shape. New "
        + "world-generation versions can move continents and biomes, so these offsets let you place an existing base "
        + "inside the appropriate biome and on land again. After aligning the new map with runPreview, Server "
        + "Utilities can restore claimed chunks from backup into the surrounding terrain more seamlessly. Finalize "
        + "these values before generating new chunks.")
@Config.RequiresWorldRestart
public class MigrationConfig {

    @Config.Name("Biome Placement Offset X")
    @Config.Comment("Offsets climate and biome selection without moving the selected biome's terrain noise. "
            + ConfigRWG.OFFSET_WARNING)
    @Config.DefaultInt(0)
    @Config.RangeInt(min = -30000000, max = 30000000)
    public static int biomeOffsetX;

    @Config.Name("Biome Placement Offset Z")
    @Config.Comment("Offsets climate and biome selection without moving the selected biome's terrain noise. "
            + ConfigRWG.OFFSET_WARNING)
    @Config.DefaultInt(0)
    @Config.RangeInt(min = -30000000, max = 30000000)
    public static int biomeOffsetZ;

    @Config.Name("Landmass Placement Offset X")
    @Config.Comment("Offsets continent and island placement without moving biome terrain noise. "
            + ConfigRWG.OFFSET_WARNING)
    @Config.DefaultInt(0)
    @Config.RangeInt(min = -30000000, max = 30000000)
    public static int landmassOffsetX;

    @Config.Name("Landmass Placement Offset Z")
    @Config.Comment("Offsets continent and island placement without moving biome terrain noise. "
            + ConfigRWG.OFFSET_WARNING)
    @Config.DefaultInt(0)
    @Config.RangeInt(min = -30000000, max = 30000000)
    public static int landmassOffsetZ;
}
