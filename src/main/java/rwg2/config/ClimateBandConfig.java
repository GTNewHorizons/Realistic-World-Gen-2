package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ConfigRWG.MOD_ID, category = "climate bands")
@Config.Comment("Replaces random climate placement with bands: temperature follows Z and humidity follows X. Moving "
        + "from Z = 0 toward the north passes through cold then snow; moving south passes through hot. Bands mirror "
        + "and repeat forever. Biome Placement Offset moves the band origin.")
@Config.RequiresWorldRestart
public class ClimateBandConfig {

    @Config.Name("Enable Climate Bands")
    @Config.Comment("Uses climate bands in the RWG2 and RWG2_CONTINENTS world types. " + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultBoolean(false)
    public static boolean enabled;

    @Config.Name("Snow Band Width")
    @Config.Comment("North-south width in blocks. " + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(6000f)
    @Config.RangeFloat(min = 256f, max = 1000000f)
    public static float snowWidth;

    @Config.Name("Cold Band Width")
    @Config.Comment("North-south width in blocks. " + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(6000f)
    @Config.RangeFloat(min = 256f, max = 1000000f)
    public static float coldWidth;

    @Config.Name("Hot Band Width")
    @Config.Comment("North-south width in blocks of the band shared by hot and wet climates. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(12000f)
    @Config.RangeFloat(min = 256f, max = 1000000f)
    public static float hotWidth;

    @Config.Name("Humidity Band Width")
    @Config.Comment("East-west width in blocks of each alternating dry and wet band. Only splits hot from wet. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(6000f)
    @Config.RangeFloat(min = 256f, max = 1000000f)
    public static float humidityWidth;

    @Config.Name("Blend Width")
    @Config.Comment("Width in blocks of the zone where climate patches interleave at each boundary. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(4000f)
    @Config.RangeFloat(min = 0f, max = 100000f)
    public static float blendWidth;

    @Config.Name("Boundary Warp Strength")
    @Config.Comment("Maximum displacement in blocks that makes band boundaries wavy. " + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(1500f)
    @Config.RangeFloat(min = 0f, max = 100000f)
    public static float warpStrength;

    @Config.Name("Band Drift Strength")
    @Config.Comment("Maximum large-scale displacement in blocks that bends bands and varies their local width. 0 keeps "
            + "bands straight and evenly sized. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(6000f)
    @Config.RangeFloat(min = 0f, max = 100000f)
    public static float driftStrength;

    @Config.Name("Band Drift Scale")
    @Config.Comment("Approximate distance in blocks over which band drift changes. Lower values bend bands more "
            + "tightly. "
            + ConfigRWG.WORLDGEN_WARNING)
    @Config.DefaultFloat(16000f)
    @Config.RangeFloat(min = 256f, max = 1000000f)
    public static float driftScale;
}
