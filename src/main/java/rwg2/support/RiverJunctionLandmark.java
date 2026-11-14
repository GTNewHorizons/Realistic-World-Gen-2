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

    /**
     * How far, in chunks, to look for other centers of the same river junction. Distinct junctions lie far further
     * apart.
     */
    public static final int RIVAL_CHUNKS = 20;

    private RiverJunctionLandmark() {}

    /**
     * Returns the junction center owned by the chunk at these block coordinates, or null if it has none. A junction can
     * have several such centers; see {@link #hasStrongerCenter}.
     */
    public static int[] findCenter(ChunkManagerRealistic manager, int chunkX, int chunkZ) {
        int[] center = findPeak(manager, chunkX, chunkZ);
        return center != null && canHost(manager, center[0], center[1]) ? center : null;
    }

    /**
     * Where the river warp or a thin Voronoi triangle stretches a junction, its strength stays high along a ridge and
     * the climb stalls in several chunks. Two Voronoi vertices a short edge apart also read as one junction. Only the
     * strongest of those centers that draws a centerpiece hosts the landmark, so the junction keeps the odds of having
     * one it had before.
     */
    public static boolean hasStrongerCenter(ChunkManagerRealistic manager, long seed, int centerX, int centerZ) {
        float strength = manager.getRiverJunctionStrength(centerX, centerZ);
        int chunkX = Math.floorDiv(centerX, 16) * 16;
        int chunkZ = Math.floorDiv(centerZ, 16) * 16;
        int reach = RIVAL_CHUNKS * 16;
        for (int x = chunkX - reach; x <= chunkX + reach; x += 16) {
            for (int z = chunkZ - reach; z <= chunkZ + reach; z += 16) {
                if (x == chunkX && z == chunkZ) continue;
                // Cheap skip: a chunk holding a center is all but always above zero at its middle.
                if (manager.getRiverJunctionStrength(x + 8, z + 8) <= 0f) continue;
                int[] rival = findPeak(manager, x, z);
                if (rival == null) continue;
                float rivalStrength = manager.getRiverJunctionStrength(rival[0], rival[1]);
                if (rivalStrength < strength) continue;
                if (rivalStrength == strength && (rival[0] > centerX || rival[0] == centerX && rival[1] > centerZ))
                    continue;
                if (pickCenterpiece(seed, rival[0], rival[1]) != Centerpiece.NONE
                        && canHost(manager, rival[0], rival[1]))
                    return true;
            }
        }
        return false;
    }

    /** Returns the junction peak the chunk at these block coordinates climbs to, or null unless it lies inside it. */
    public static int[] findPeak(ChunkManagerRealistic manager, int chunkX, int chunkZ) {
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
        if (best < .82f || Math.floorDiv(bestX, 16) * 16 != chunkX || Math.floorDiv(bestZ, 16) * 16 != chunkZ)
            return null;
        return new int[] { bestX, bestZ };
    }

    public static boolean canHost(ChunkManagerRealistic manager, int x, int z) {
        return hasMountainChainNearby(manager, x, z) && manager.getLavaCaveCoordinates(x, z) == Long.MIN_VALUE;
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
