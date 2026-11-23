package rwg2.biomes.base;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import rwg2.api.RWGBiomes;
import rwg2.config.BiomeIdConfig;

public class BaseBiomes {

    private static final BiomeGenBase[] RWG_BIOMES = new BiomeGenBase[30];

    public static void load() {
        RWGBiomes.baseRiverIce = withColor(new BaseBiomeRiver(BiomeIdConfig.iceRiver, 0, "Ice River"), 0xA0A0FF);
        RWGBiomes.baseRiverCold = withColor(new BaseBiomeRiver(BiomeIdConfig.coldRiver, 1, "Cold River"), 0x4848FF);
        RWGBiomes.baseRiverTemperate = withColor(
                new BaseBiomeRiver(BiomeIdConfig.temperateRiver, 2, "Temperate River"),
                0x0000FF);
        RWGBiomes.baseRiverHot = withColor(new BaseBiomeRiver(BiomeIdConfig.hotRiver, 3, "Hot River"), 0x0000C8);
        RWGBiomes.baseRiverWet = withColor(new BaseBiomeRiver(BiomeIdConfig.wetRiver, 4, "Wet River"), 0x0048DC);
        RWGBiomes.baseRiverOasis = withColor(new BaseBiomeRiver(BiomeIdConfig.riverOasis, 5, "River Oasis"), 0x0088FF);

        RWGBiomes.baseOceanIce = withColor(new BaseBiomeOcean(BiomeIdConfig.iceOcean, 0, "Ice Ocean"), 0x9090A0);
        RWGBiomes.baseOceanCold = withColor(new BaseBiomeOcean(BiomeIdConfig.coldOcean, 1, "Cold Ocean"), 0x181890);
        RWGBiomes.baseOceanTemperate = withColor(
                new BaseBiomeOcean(BiomeIdConfig.temperateOcean, 2, "Temperate Ocean"),
                0x000070);
        RWGBiomes.baseOceanHot = withColor(new BaseBiomeOcean(BiomeIdConfig.hotOcean, 3, "Hot Ocean"), 0x000050);
        RWGBiomes.baseOceanWet = withColor(new BaseBiomeOcean(BiomeIdConfig.wetOcean, 4, "Wet Ocean"), 0x003070);
        RWGBiomes.baseOceanOasis = withColor(new BaseBiomeOcean(BiomeIdConfig.oceanOasis, 5, "Ocean Oasis"), 0x005A78);

        RWGBiomes.baseSnowDesert = withColor(
                new BaseBiomeSnowDesert(BiomeIdConfig.snowDesert, "Snow Desert"),
                0xFFFFFF);
        RWGBiomes.baseSnowForest = withColor(
                new BaseBiomeSnowForest(BiomeIdConfig.snowForest, "Snow Forest"),
                0x31554A);
        RWGBiomes.baseColdPlains = withColor(
                new BaseBiomeColdPlains(BiomeIdConfig.coldPlains, "Cold Plains"),
                0x8FB0A0);
        RWGBiomes.baseColdForest = withColor(
                new BaseBiomeColdForest(BiomeIdConfig.coldForest, "Cold Forest"),
                0x0B6659);
        RWGBiomes.baseHotPlains = withColor(new BaseBiomeHotPlains(BiomeIdConfig.hotPlains, "Hot Plains"), 0xBDB25F);
        RWGBiomes.baseHotForest = withColor(new BaseBiomeHotForest(BiomeIdConfig.hotForest, "Hot Forest"), 0xA79D64);
        RWGBiomes.baseHotDesert = withColor(new BaseBiomeHotDesert(BiomeIdConfig.hotDesert, "Hot Desert"), 0xFA9418);
        RWGBiomes.basePlains = withColor(new BaseBiomePlains(BiomeIdConfig.plains, "Plains (RWG)"), 0x8DB360);
        RWGBiomes.baseTropicalIsland = withColor(
                new BaseBiomeTropicalIsland(BiomeIdConfig.tropicalIsland, "Tropical Island"),
                0x5BC25B);
        RWGBiomes.baseRedwood = withColor(new BaseBiomeRedwood(BiomeIdConfig.redwood, "Redwood"), 0x596651);
        RWGBiomes.baseJungle = withColor(new BaseBiomeJungle(BiomeIdConfig.jungle, "Jungle (RWG)"), 0x537B09);
        RWGBiomes.baseOasis = withColor(new BaseBiomeRiver(BiomeIdConfig.oasis, 5, "Oasis"), 0x00B3A4);
        RWGBiomes.baseTemperateForest = withColor(
                new BaseBiomeTemperateForest(BiomeIdConfig.temperateForest, "Temperate Forest"),
                0x056621);
        RWGBiomes.baseJungleMesa = withColor(
                new BaseBiomeJungleMesa(BiomeIdConfig.jungleMesa, "Jungle Mesa"),
                0xD94515);
        RWGBiomes.baseHighRainforest = withColor(
                new BaseBiomeJungle(BiomeIdConfig.highRainforest, "High Rainforest"),
                0x2E7D1E);
        RWGBiomes.baseJungleHills = withColor(new BaseBiomeJungle(BiomeIdConfig.jungleHills, "Jungle Hills"), 0x2C4205);
        RWGBiomes.baseJungleCanyon = withColor(
                new BaseBiomeJungle(BiomeIdConfig.jungleCanyon, "Jungle Canyon"),
                0x7A8F2A);
        RWGBiomes.baseRedwoodJungle = withColor(
                new BaseBiomeJungle(BiomeIdConfig.redwoodJungle, "Redwood Jungle"),
                0x44704A);

        System.arraycopy(
                new BiomeGenBase[] { RWGBiomes.baseRiverIce, RWGBiomes.baseRiverCold, RWGBiomes.baseRiverTemperate,
                        RWGBiomes.baseRiverHot, RWGBiomes.baseRiverWet, RWGBiomes.baseRiverOasis,
                        RWGBiomes.baseOceanIce, RWGBiomes.baseOceanCold, RWGBiomes.baseOceanTemperate,
                        RWGBiomes.baseOceanHot, RWGBiomes.baseOceanWet, RWGBiomes.baseOceanOasis,
                        RWGBiomes.baseSnowDesert, RWGBiomes.baseSnowForest, RWGBiomes.baseColdPlains,
                        RWGBiomes.baseColdForest, RWGBiomes.baseHotPlains, RWGBiomes.baseHotForest,
                        RWGBiomes.baseHotDesert, RWGBiomes.basePlains, RWGBiomes.baseTropicalIsland,
                        RWGBiomes.baseRedwood, RWGBiomes.baseJungle, RWGBiomes.baseOasis, RWGBiomes.baseTemperateForest,
                        RWGBiomes.baseJungleMesa, RWGBiomes.baseHighRainforest, RWGBiomes.baseJungleHills,
                        RWGBiomes.baseJungleCanyon, RWGBiomes.baseRedwoodJungle },
                0,
                RWG_BIOMES,
                0,
                RWG_BIOMES.length);

        // RIVER
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverIce, Type.RIVER, Type.COLD, Type.SNOWY);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverCold, Type.RIVER, Type.COLD, Type.CONIFEROUS, Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverTemperate, Type.RIVER, Type.COLD, Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverHot, Type.RIVER, Type.HOT, Type.DRY, Type.SANDY);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverWet, Type.RIVER, Type.HOT, Type.WET, Type.JUNGLE);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRiverOasis, Type.RIVER, Type.HOT, Type.WET, Type.JUNGLE);

        // OCEAN
        BiomeDictionary.registerBiomeType(RWGBiomes.baseOceanIce, Type.OCEAN, Type.BEACH, Type.COLD, Type.SNOWY);
        BiomeDictionary.registerBiomeType(
                RWGBiomes.baseOceanCold,
                Type.OCEAN,
                Type.BEACH,
                Type.COLD,
                Type.CONIFEROUS,
                Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseOceanTemperate, Type.OCEAN, Type.BEACH, Type.COLD, Type.FOREST);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseOceanHot, Type.OCEAN, Type.BEACH, Type.HOT, Type.DRY, Type.SANDY);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseOceanWet, Type.OCEAN, Type.BEACH, Type.HOT, Type.WET, Type.JUNGLE);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseOceanOasis, Type.OCEAN, Type.BEACH, Type.HOT, Type.WET, Type.JUNGLE);

        // LAND
        BiomeDictionary.registerBiomeType(RWGBiomes.baseSnowDesert, Type.COLD, Type.SNOWY, Type.WASTELAND);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseSnowForest, Type.COLD, Type.SNOWY, Type.CONIFEROUS, Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseColdPlains, Type.COLD, Type.WASTELAND);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseColdForest, Type.COLD, Type.CONIFEROUS, Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseHotPlains, Type.HOT, Type.SAVANNA, Type.PLAINS, Type.SPARSE);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseHotForest, Type.HOT, Type.SAVANNA, Type.PLAINS, Type.SPARSE);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseHotDesert, Type.HOT, Type.DRY, Type.SANDY);
        BiomeDictionary.registerBiomeType(RWGBiomes.basePlains, Type.PLAINS);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseTropicalIsland, Type.HOT, Type.WET, Type.JUNGLE);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseRedwood, Type.COLD, Type.CONIFEROUS, Type.FOREST);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseJungle, Type.HOT, Type.WET, Type.JUNGLE);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseJungleMesa, Type.HOT, Type.WET, Type.JUNGLE, Type.FOREST, Type.HILLS);
        BiomeDictionary.registerBiomeType(
                RWGBiomes.baseHighRainforest,
                Type.HOT,
                Type.WET,
                Type.JUNGLE,
                Type.FOREST,
                Type.DENSE,
                Type.HILLS);
        BiomeDictionary
                .registerBiomeType(RWGBiomes.baseJungleHills, Type.HOT, Type.WET, Type.JUNGLE, Type.FOREST, Type.HILLS);
        BiomeDictionary.registerBiomeType(
                RWGBiomes.baseJungleCanyon,
                Type.HOT,
                Type.WET,
                Type.JUNGLE,
                Type.FOREST,
                Type.HILLS);
        BiomeDictionary.registerBiomeType(
                RWGBiomes.baseRedwoodJungle,
                Type.HOT,
                Type.WET,
                Type.JUNGLE,
                Type.FOREST,
                Type.DENSE,
                Type.HILLS);
        BiomeDictionary.registerBiomeType(RWGBiomes.baseColdForest, Type.FOREST, Type.DENSE, Type.HILLS);
    }

    private static <T extends BiomeGenBase> T withColor(T biome, int color) {
        biome.setColor(color);
        return biome;
    }

    public static void validateRegistrations() {
        BiomeGenBase[] registry = BiomeGenBase.getBiomeGenArray();
        for (int index = 0; index < RWG_BIOMES.length; index++) {
            BiomeGenBase expected = RWG_BIOMES[index];
            int id = expected.biomeID;
            BiomeGenBase registered = id >= 0 && id < registry.length ? registry[id] : null;
            if (registered != expected) {
                String actualName = registered == null ? "nothing" : '"' + registered.biomeName + '"';
                throw new IllegalStateException(
                        "RWG biome ID collision at " + id
                                + ": expected \""
                                + expected.biomeName
                                + "\", but the registry contains "
                                + actualName
                                + ". Assign unique IDs in config/rwg2.cfg before loading a world.");
            }
        }
    }
}
