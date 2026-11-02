package rwg2.config;

import com.gtnewhorizon.gtnhlib.config.Config;

/** Feature toggles read while chunks generate, so changes apply to newly generated chunks right away. */
@Config(modid = ConfigRWG.MOD_ID, category = "settings")
public class SettingsConfig {

    @Config.Name("Enable Cobblestone Boulders")
    @Config.DefaultBoolean(true)
    public static boolean enableCobblestoneBoulders;

    @Config.Name("Enable Flight Speed Command")
    @Config.Comment("Registers RWG2's /speed command. Disable this when another mod, such as Essentials, provides it. "
            + "Takes effect the next time a world or server starts.")
    @Config.DefaultBoolean(true)
    public static boolean enableFlightSpeedCommand;

    @Config.Name("Generate Caves")
    @Config.DefaultBoolean(true)
    public static boolean generateCaves;

    @Config.Name("Generate Emeralds")
    @Config.DefaultBoolean(true)
    public static boolean generateEmeralds;

    @Config.Name("Generate Mineshafts")
    @Config.DefaultBoolean(true)
    public static boolean generateMineshafts;

    @Config.Name("Generate Underground Lakes")
    @Config.DefaultBoolean(true)
    public static boolean generateUndergroundLakes;

    @Config.Name("Generate Underground Lava Lakes")
    @Config.DefaultBoolean(true)
    public static boolean generateUndergroundLavaLakes;

    @Config.Name("Generate Villages")
    @Config.DefaultBoolean(true)
    public static boolean generateVillages;
}
