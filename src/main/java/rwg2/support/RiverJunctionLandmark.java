package rwg2.support;

import rwg2.biomes.realistic.land.RealisticBiomeMountainChain;
import rwg2.config.LandmarkDecorationsConfig;
import rwg2.world.ChunkManagerRealistic;

/** Where river junction landmarks go and what they carry. Shared by world generation and the offline preview. */
public final class RiverJunctionLandmark {

    public enum Centerpiece {
        NONE,
        PUZZLE,
        LIT_PORTAL,
        UNLIT_PORTAL
    }

    private RiverJunctionLandmark() {}

    /** Returns the junction center owned by the chunk at these block coordinates, or null if it has none. */
    public static int[] findCenter(ChunkManagerRealistic manager, int chunkX, int chunkZ) {
        int bestX = chunkX + 8;
        int bestZ = chunkZ + 8;
        float best = -1f;
        for (int x = chunkX; x < chunkX + 16; x += 2) {
            for (int z = chunkZ; z < chunkZ + 16; z += 2) {
                float strength = manager.getRiverJunctionStrength(x, z);
                if (strength > best) {
                    best = strength;
                    bestX = x;
                    bestZ = z;
                }
            }
        }
        if (best < .35f) return null;
        for (int step = 4; step >= 1; step /= 2) {
            boolean moved;
            do {
                moved = false;
                for (int dx = -step; dx <= step; dx += step) {
                    for (int dz = -step; dz <= step; dz += step) {
                        int x = bestX + dx;
                        int z = bestZ + dz;
                        float strength = manager.getRiverJunctionStrength(x, z);
                        if (strength > best) {
                            best = strength;
                            bestX = x;
                            bestZ = z;
                            moved = true;
                        }
                    }
                }
            } while (moved);
        }
        if (best < .82f || !hasMountainChainNearby(manager, bestX, bestZ)
                || manager.getLavaCaveCoordinates(bestX, bestZ) != Long.MIN_VALUE)
            return null;
        if (Math.floorDiv(bestX, 16) * 16 != chunkX || Math.floorDiv(bestZ, 16) * 16 != chunkZ) return null;
        return new int[] { bestX, bestZ };
    }

    /** Rolls the centerpiece, assuming Twilight Forest and LootGames are both installed. */
    public static Centerpiece pickCenterpiece(long seed, int centerX, int centerZ) {
        long key = (long) centerX << 32 ^ centerZ & 0xffffffffL;
        long choice = mix(seed ^ key ^ 0x94D049BB133111EBL);
        if ((choice & 1L) != 0L) return Centerpiece.NONE;
        if (!rollsPortal(choice, centerX, centerZ)) return Centerpiece.PUZZLE;
        boolean unlit = (choice >>> 40 & 0xFFFFFFL)
                < (long) (LandmarkDecorationsConfig.twilightForestPortalUnlitChance * 0x1000000L);
        return unlit ? Centerpiece.UNLIT_PORTAL : Centerpiece.LIT_PORTAL;
    }

    private static boolean rollsPortal(long choice, int centerX, int centerZ) {
        if (LandmarkDecorationsConfig.twilightForestPortalChance <= 0f) return false;
        long minimumDistance = LandmarkDecorationsConfig.twilightForestPortalMinimumSpawnDistance;
        if ((long) centerX * centerX + (long) centerZ * centerZ < minimumDistance * minimumDistance) return false;
        return (choice >>> 1 & 0xFFFFFFL) < (long) (LandmarkDecorationsConfig.twilightForestPortalChance * 0x1000000L);
    }

    private static boolean hasMountainChainNearby(ChunkManagerRealistic manager, int x, int z) {
        for (int dx = -24; dx <= 24; dx += 12) {
            for (int dz = -24; dz <= 24; dz += 12) {
                if (manager.getBiomeDataAt(x + dx, z + dz) instanceof RealisticBiomeMountainChain) return true;
            }
        }
        return false;
    }

    static long mix(long value) {
        value ^= value >>> 30;
        value *= 0xBF58476D1CE4E5B9L;
        value ^= value >>> 27;
        value *= 0x94D049BB133111EBL;
        return value ^ value >>> 31;
    }
}
