package rwg2.support;

import cpw.mods.fml.common.Loader;

/**
 * Optional mods RWG integrates with, looked up once. Only read these after FML has discovered mods (pre-init or later),
 * since the first read freezes the answer.
 */
public final class Mods {

    public static final boolean BIOMES_O_PLENTY = Loader.isModLoaded("BiomesOPlenty");
    public static final boolean CHROMATICRAFT = Loader.isModLoaded("ChromatiCraft");
    public static final boolean ET_FUTURUM = Loader.isModLoaded("etfuturum");
    /** Roadhog360's Et Futurum 2.6.x releases share the GTNH fork's mod id but predate its cave vines. */
    public static final boolean ET_FUTURUM_CAVE_VINES = ET_FUTURUM
            && hasClass("ganymedes01.etfuturum.world.generate.decorate.WorldGenCaveVines");
    public static final boolean EXTRABIOMES_XL = Loader.isModLoaded("ExtrabiomesXL");
    public static final boolean LOOTGAMES = Loader.isModLoaded("lootgames");
    public static final boolean NATURA = Loader.isModLoaded("Natura");
    public static final boolean THAUMCRAFT = Loader.isModLoaded("Thaumcraft");
    public static final boolean TWILIGHT_FOREST = Loader.isModLoaded("TwilightForest");

    private Mods() {}

    /** Looks for the class file without loading it, so a miss does not land in the launch class loader's caches. */
    private static boolean hasClass(String name) {
        return Mods.class.getClassLoader().getResource(name.replace('.', '/') + ".class") != null;
    }
}
