package rwg2.support;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

import rwg2.api.RWGBiomes;
import rwg2.support.Support.BiomeCategory;
import rwg2.surface.SurfaceGrassland;
import rwg2.terrain.TerrainHighland;
import rwg2.terrain.TerrainHilly;

public class SupportCC {
    /*
     * ChromatiCraft BIOMES 46: "Ender Forest" 47: "Rainbow Forest" 48: "Luminous Cliffs" 49: "Luminous Cliffs Edge"
     */

    public static void init() {
        BiomeGenBase[] b = BiomeGenBase.getBiomeGenArray();

        for (int i = 0; i < b.length; i++) {
            if (b[i] != null) {
                if ("Ender Forest".equals(b[i].biomeName) || "Rainbow Forest".equals(b[i].biomeName)) {
                    Support.addBiome(
                            new RealisticBiomeSupport(
                                    b[i],
                                    RWGBiomes.baseTemperateForest,
                                    new TerrainHighland(6f, 120f, 65f, 150f),
                                    new SurfaceGrassland(
                                            b[i].topBlock,
                                            b[i].fillerBlock,
                                            Blocks.stone,
                                            Blocks.cobblestone)),
                            BiomeCategory.HOT);
                }

                if ("Luminous Cliffs".equals(b[i].biomeName) || "Luminous Cliffs Shores".equals(b[i].biomeName)) {
                    Support.addBiome(
                            new RealisticBiomeSupport(
                                    b[i],
                                    RWGBiomes.baseOceanTemperate,
                                    new TerrainHilly(100f, 120f, 0f),
                                    new SurfaceGrassland(
                                            b[i].topBlock,
                                            b[i].fillerBlock,
                                            Blocks.stone,
                                            Blocks.cobblestone)),
                            BiomeCategory.HOT);
                }
            }
        }
    }
}
