package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ConfigRWG.MOD_ID, category = "biome ids")
@Config.RequiresMcRestart
public class BiomeIdConfig {

    @Config.Name("00 Ice River")
    @Config.Comment("Ice River")
    @Config.DefaultInt(300)
    @Config.RangeInt(min = 0, max = 65535)
    public static int iceRiver;

    @Config.Name("01 Cold River")
    @Config.Comment("Cold River")
    @Config.DefaultInt(301)
    @Config.RangeInt(min = 0, max = 65535)
    public static int coldRiver;

    @Config.Name("02 Temperate River")
    @Config.Comment("Temperate River")
    @Config.DefaultInt(302)
    @Config.RangeInt(min = 0, max = 65535)
    public static int temperateRiver;

    @Config.Name("03 Hot River")
    @Config.Comment("Hot River")
    @Config.DefaultInt(303)
    @Config.RangeInt(min = 0, max = 65535)
    public static int hotRiver;

    @Config.Name("04 Wet River")
    @Config.Comment("Wet River")
    @Config.DefaultInt(304)
    @Config.RangeInt(min = 0, max = 65535)
    public static int wetRiver;

    @Config.Name("05 River Oasis")
    @Config.Comment("River Oasis")
    @Config.DefaultInt(305)
    @Config.RangeInt(min = 0, max = 65535)
    public static int riverOasis;

    @Config.Name("06 Ice Ocean")
    @Config.Comment("Ice Ocean")
    @Config.DefaultInt(306)
    @Config.RangeInt(min = 0, max = 65535)
    public static int iceOcean;

    @Config.Name("07 Cold Ocean")
    @Config.Comment("Cold Ocean")
    @Config.DefaultInt(307)
    @Config.RangeInt(min = 0, max = 65535)
    public static int coldOcean;

    @Config.Name("08 Temperate Ocean")
    @Config.Comment("Temperate Ocean")
    @Config.DefaultInt(308)
    @Config.RangeInt(min = 0, max = 65535)
    public static int temperateOcean;

    @Config.Name("09 Hot Ocean")
    @Config.Comment("Hot Ocean")
    @Config.DefaultInt(309)
    @Config.RangeInt(min = 0, max = 65535)
    public static int hotOcean;

    @Config.Name("10 Wet Ocean")
    @Config.Comment("Wet Ocean")
    @Config.DefaultInt(310)
    @Config.RangeInt(min = 0, max = 65535)
    public static int wetOcean;

    @Config.Name("11 Ocean Oasis")
    @Config.Comment("Ocean Oasis")
    @Config.DefaultInt(311)
    @Config.RangeInt(min = 0, max = 65535)
    public static int oceanOasis;

    @Config.Name("12 Snow Desert")
    @Config.Comment("Snow Desert")
    @Config.DefaultInt(312)
    @Config.RangeInt(min = 0, max = 65535)
    public static int snowDesert;

    @Config.Name("13 Snow Forest")
    @Config.Comment("Snow Forest")
    @Config.DefaultInt(313)
    @Config.RangeInt(min = 0, max = 65535)
    public static int snowForest;

    @Config.Name("14 Cold Plains")
    @Config.Comment("Cold Plains")
    @Config.DefaultInt(314)
    @Config.RangeInt(min = 0, max = 65535)
    public static int coldPlains;

    @Config.Name("15 Cold Forest")
    @Config.Comment("Cold Forest")
    @Config.DefaultInt(315)
    @Config.RangeInt(min = 0, max = 65535)
    public static int coldForest;

    @Config.Name("16 Hot Plains")
    @Config.Comment("Hot Plains")
    @Config.DefaultInt(316)
    @Config.RangeInt(min = 0, max = 65535)
    public static int hotPlains;

    @Config.Name("17 Hot Forest")
    @Config.Comment("Hot Forest")
    @Config.DefaultInt(317)
    @Config.RangeInt(min = 0, max = 65535)
    public static int hotForest;

    @Config.Name("18 Hot Desert")
    @Config.Comment("Hot Desert")
    @Config.DefaultInt(318)
    @Config.RangeInt(min = 0, max = 65535)
    public static int hotDesert;

    @Config.Name("19 Plains (RWG)")
    @Config.Comment("Plains")
    @Config.DefaultInt(319)
    @Config.RangeInt(min = 0, max = 65535)
    public static int plains;

    @Config.Name("20 Tropical Island")
    @Config.Comment("Tropical Island")
    @Config.DefaultInt(320)
    @Config.RangeInt(min = 0, max = 65535)
    public static int tropicalIsland;

    @Config.Name("21 Redwood")
    @Config.Comment("Redwood")
    @Config.DefaultInt(321)
    @Config.RangeInt(min = 0, max = 65535)
    public static int redwood;

    @Config.Name("22 Jungle (RWG)")
    @Config.Comment("Jungle")
    @Config.DefaultInt(322)
    @Config.RangeInt(min = 0, max = 65535)
    public static int jungle;

    @Config.Name("23 Oasis")
    @Config.Comment("Oasis")
    @Config.DefaultInt(323)
    @Config.RangeInt(min = 0, max = 65535)
    public static int oasis;

    @Config.Name("24 Temperate Forest")
    @Config.Comment("Temperate Forest")
    @Config.DefaultInt(324)
    @Config.RangeInt(min = 0, max = 65535)
    public static int temperateForest;

    @Config.Name("25 Jungle Mesa")
    @Config.Comment("Jungle Mesa")
    @Config.DefaultInt(325)
    @Config.RangeInt(min = 0, max = 65535)
    public static int jungleMesa;

    @Config.Name("26 High Rainforest")
    @Config.Comment("High Rainforest")
    @Config.DefaultInt(326)
    @Config.RangeInt(min = 0, max = 65535)
    public static int highRainforest;

    @Config.Name("27 Jungle Hills")
    @Config.Comment("Jungle Hills")
    @Config.DefaultInt(327)
    @Config.RangeInt(min = 0, max = 65535)
    public static int jungleHills;

    @Config.Name("28 Jungle Canyon")
    @Config.Comment("Jungle Canyon")
    @Config.DefaultInt(328)
    @Config.RangeInt(min = 0, max = 65535)
    public static int jungleCanyon;

    @Config.Name("29 Redwood Jungle")
    @Config.Comment("Redwood Jungle")
    @Config.DefaultInt(329)
    @Config.RangeInt(min = 0, max = 65535)
    public static int redwoodJungle;
}
