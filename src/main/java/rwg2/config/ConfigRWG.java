package rwg2.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigFieldParser;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.relauncher.FMLInjectionData;

/** Registers RWG's GTNHLib config classes, which all share {@code config/rwg2.cfg}. */
public final class ConfigRWG {

    public static final String MOD_ID = "rwg2";
    public static final String DEBUG_CATEGORY = "debug";
    public static final String ENABLE_WORLDGEN_DIAGNOSTICS = "Enable Worldgen Diagnostics";
    static final String WORLDGEN_WARNING = "Changing this after creating a world causes borders between old and new chunks.";
    static final String OFFSET_WARNING = "Expert migration setting. Change only before generating new chunks. Positive "
            + "values sample the generator at larger coordinates, moving the corresponding map features toward "
            + "negative coordinates.";

    private static final Class<?>[] CONFIG_CLASSES = { BiomeIdConfig.class, SettingsConfig.class,
            ContinentalConfig.class, LandmarkDecorationsConfig.class, MigrationConfig.class, DebugConfig.class };

    private ConfigRWG() {}

    /** Registers every config class with GTNHLib, which also exposes them in the in-game mod config screen. */
    public static void init() {
        try {
            for (Class<?> configClass : CONFIG_CLASSES) {
                ConfigurationManager.registerConfig(configClass);
            }
        } catch (ConfigException exception) {
            throw new RuntimeException("Failed to load RWG config", exception);
        }
        Configuration config = ConfigurationManager.getConfig(ContinentalConfig.class);
        applyPropertyOrder(config);
        config.save();
    }

    /** Loads the config classes from an arbitrary file for the offline preview, which runs outside Forge. */
    public static void init(File file) {
        ensureForgeConfigEnvironment(file);
        Configuration config = new Configuration(file);
        config.load();
        try {
            for (Class<?> configClass : CONFIG_CLASSES) {
                String category = configClass.getAnnotation(Config.class).category();
                Config.Comment comment = configClass.getAnnotation(Config.Comment.class);
                if (comment != null) config.setCategoryComment(category, String.join("\n", comment.value()));
                for (Field field : configClass.getDeclaredFields()) {
                    if (!isConfigField(field)) continue;
                    ConfigFieldParser.loadField(null, field, config, category, ConfigFieldParser.getFieldName(field));
                }
            }
        } catch (ConfigException exception) {
            throw new IllegalStateException("Failed to load RWG preview config " + file, exception);
        }
        applyPropertyOrder(config);
        if (config.hasChanged()) config.save();
    }

    /** Keeps continental settings in declaration order instead of Forge's alphabetical order. */
    private static void applyPropertyOrder(Configuration config) {
        List<String> order = new ArrayList<String>();
        for (Field field : ContinentalConfig.class.getDeclaredFields()) {
            if (isConfigField(field)) order.add(ConfigFieldParser.getFieldName(field));
        }
        config.setCategoryPropertyOrder(ContinentalConfig.class.getAnnotation(Config.class).category(), order);
    }

    private static boolean isConfigField(Field field) {
        return !field.isSynthetic() && !field.isAnnotationPresent(Config.Ignore.class);
    }

    private static void ensureForgeConfigEnvironment(File configFile) {
        if (FMLInjectionData.data()[6] != null) return;
        File configDirectory = configFile.getAbsoluteFile().getParentFile();
        File minecraftDirectory = configDirectory != null && "config".equals(configDirectory.getName())
                ? configDirectory.getParentFile()
                : new File(System.getProperty("user.dir"));
        try {
            Field minecraftHome = FMLInjectionData.class.getDeclaredField("minecraftHome");
            minecraftHome.setAccessible(true);
            minecraftHome.set(null, minecraftDirectory);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Could not initialize Forge configuration for the offline preview",
                    exception);
        }
    }

}
