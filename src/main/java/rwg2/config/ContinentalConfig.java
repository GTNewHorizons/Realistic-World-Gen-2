package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

/** Continent, ocean and island geometry used by the RWG2_CONTINENTS world type. */
@Config(modid = ConfigRWG.MOD_ID, category = "continental rwg")
@Config.RequiresWorldRestart
public class ContinentalConfig {

    @Config.Ignore
    private static final String CONTINENT_WIDTH = "Distance in blocks from a continent's Voronoi centre to its coast. "
            + ConfigRWG.WORLDGEN_WARNING;
    @Config.Ignore
    private static final String ISLAND_WIDTH = "Distance in blocks from an island seed to its coast. "
            + ConfigRWG.WORLDGEN_WARNING;

    @Config.Name("Minimum Continent Width")
    @Config.Comment(CONTINENT_WIDTH)
    @Config.DefaultFloat(2700f)
    @Config.RangeFloat(min = 1f, max = 100000f)
    public static float minimumContinentWidth;

    @Config.Name("Maximum Continent Width")
    @Config.Comment(CONTINENT_WIDTH)
    @Config.DefaultFloat(2700f)
    @Config.RangeFloat(min = 1f, max = 100000f)
    public static float maximumContinentWidth;

    @Config.Name("Average Ocean Width")
    @Config.Comment("Average ocean band width in blocks; continent plus ocean width sets the Voronoi radius. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(100f)
    @Config.RangeFloat(min = 1f, max = 100000f)
    public static float averageOceanWidth;

    @Config.Name("Minimum Ocean Width")
    @Config.Comment("Minimum total water gap in blocks between two maximum-sized continents. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(50f)
    @Config.RangeFloat(min = 0f, max = 100000f)
    public static float minimumOceanWidth;

    @Config.Name("Maximum Ocean Fraction")
    @Config.Comment("Approximate upper limit on the fraction of the world covered by ocean. 1.0 disables the limit. "
            + "Lower values dilate continents after placement, so oceans that normally separate continents may become "
            + "interior seas. The result is approximate and varies with the world seed and the other continent "
            + "settings. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(0.4f)
    @Config.RangeFloat(min = 0f, max = 1f)
    public static float maximumOceanFraction;

    @Config.Name("Minimum Island Width")
    @Config.Comment(ISLAND_WIDTH)
    @Config.DefaultFloat(300f)
    @Config.RangeFloat(min = 1f, max = 100000f)
    public static float minimumIslandWidth;

    @Config.Name("Maximum Island Width")
    @Config.Comment(ISLAND_WIDTH)
    @Config.DefaultFloat(600f)
    @Config.RangeFloat(min = 1f, max = 100000f)
    public static float maximumIslandWidth;

    @Config.Name("Island Placement Chance")
    @Config.Comment("Chance to retain an island after valid island seeds have been identified. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(0.30f)
    @Config.RangeFloat(min = 0f, max = 1f)
    public static float islandPlacementChance;

    @Config.Name("Large Island Volcano Chance")
    @Config.Comment("Chance for a large island to contain a volcano. " + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(0.15f)
    @Config.RangeFloat(min = 0f, max = 1f)
    public static float largeIslandVolcanoChance;

    @Config.Name("Average Landmarks Per Type And Continent")
    @Config.Comment("Approximate average number of each enabled landmark type per continent. Candidate probability "
            + "is corrected for the configured continental land fraction. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(0.25f)
    @Config.RangeFloat(min = 0f, max = 100f)
    public static float averageLandmarksPerTypeAndContinent;

    @Config.Name("Biome Size Multiplier")
    @Config.Comment("Scales biome extent by this factor, conceptually similar to vanilla Large Biomes. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(1f)
    @Config.RangeFloat(min = 0.25f, max = 10f)
    public static float biomeSizeMultiplier;

    @Config.Name("Per Continent Tainted Land")
    @Config.Comment("Seeds Tainted Land from a per-continent budget instead of the shared small-biome field, which "
            + "scatters it across every continent. Continental worlds only. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultBoolean(true)
    public static boolean perContinentTaintedLand;

    @Config.Name("Average Tainted Land Per Continent")
    @Config.Comment("Average Tainted Land patches per continent, when the option above is enabled. 0 removes "
            + "Tainted Land from world generation. The estimate runs high: 1.0 gives roughly 2 patches. Check the "
            + "result with runPreview. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(0.75f)
    @Config.RangeFloat(min = 0f, max = 16f)
    public static float averageTaintedLandPerContinent;
}
