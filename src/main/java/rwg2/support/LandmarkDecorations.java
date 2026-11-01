package rwg2.support;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import cpw.mods.fml.common.Loader;
import eu.usrv.legacylootgames.blocks.DungeonBrick;
import ganymedes01.etfuturum.ModBlocks;
import mods.natura.worldgen.GlowshroomGenBlueGreen;
import mods.natura.worldgen.GlowshroomGenPurple;
import ru.timeconqueror.lootgames.registry.LGBlocks;
import rwg2.biomes.realistic.land.RealisticBiomeMountainChain;
import rwg2.config.ConfigRWG;
import rwg2.map.LavaCaveLandmark;
import rwg2.util.ContinentalNoise;
import rwg2.util.NoiseGenerator;
import rwg2.world.ChunkManagerRealistic;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;

/** Optional mod-backed centerpieces and flora for RWG's underground landmarks. */
public final class LandmarkDecorations {

    private static final int LAVA_LEVEL = 25;
    private static final int LAVA_PILLAR_TOP = 37;
    private static final int RIVER_PILLAR_TOP = 64;
    private static final int RIVER_PILLAR_BOTTOM = 40;
    private static final int PORTAL_ISLAND_RADIUS = 7;
    private static final int PORTAL_ISLAND_SPAN = PORTAL_ISLAND_RADIUS * 2 + 1;

    private final Block deepslate;
    private final Block puzzleMaster;
    private final Block twilightPortal;
    private final Block twilightFirefly;
    private final Block[] portalFlora;
    private final int[] portalFloraMetadata;
    private final WorldGenerator[] glowshrooms;

    private LandmarkDecorations(Block deepslate, Block puzzleMaster, Block twilightPortal, Block twilightFirefly,
            Block[] portalFlora, int[] portalFloraMetadata, WorldGenerator[] glowshrooms) {
        this.deepslate = deepslate;
        this.puzzleMaster = puzzleMaster;
        this.twilightPortal = twilightPortal;
        this.twilightFirefly = twilightFirefly;
        this.portalFlora = portalFlora;
        this.portalFloraMetadata = portalFloraMetadata;
        this.glowshrooms = glowshrooms;
    }

    public static LandmarkDecorations create() {
        if (!Loader.isModLoaded("etfuturum")) return null;
        Block lootGame = Loader.isModLoaded("lootgames") ? LGBlocks.PUZZLE_MASTER : null;
        Block twilight = null;
        Block firefly = null;
        Block[] flora = null;
        int[] floraMetadata = null;
        if (Loader.isModLoaded("TwilightForest")) {
            twilight = TFBlocks.portal;
            firefly = TFBlocks.firefly;
            flora = new Block[] { TFBlocks.plant, TFBlocks.plant, TFBlocks.plant, TFBlocks.plant, TFBlocks.plant,
                    Blocks.tallgrass, Blocks.red_flower, Blocks.yellow_flower };
            floraMetadata = new int[] { BlockTFPlant.META_FORESTGRASS, BlockTFPlant.META_FORESTGRASS,
                    BlockTFPlant.META_MAYAPPLE, BlockTFPlant.META_CLOVERPATCH, BlockTFPlant.META_FIDDLEHEAD, 1, 0, 0 };
        }
        WorldGenerator[] naturaGlowshrooms = Loader.isModLoaded("Natura")
                ? new WorldGenerator[] { new GlowshroomGenBlueGreen(false), new GlowshroomGenPurple(false) }
                : null;
        Block deep = ModBlocks.DEEPSLATE.get();
        return new LandmarkDecorations(
                deep == null ? Blocks.stone : deep,
                lootGame,
                twilight,
                firefly,
                flora,
                floraMetadata,
                naturaGlowshrooms);
    }

    public void decorate(World world, ChunkManagerRealistic manager, NoiseGenerator perlin, int chunkX, int chunkZ) {
        activateCeilingLavaSources(world, manager, chunkX, chunkZ);
        decorateLavaShore(world, manager, perlin, chunkX, chunkZ);
        decorateLavaCenter(world, manager, chunkX, chunkZ);
        decorateRiverJunction(world, manager, perlin, chunkX, chunkZ);
    }

    private static void activateCeilingLavaSources(World world, ChunkManagerRealistic manager, int chunkX, int chunkZ) {
        for (int offsetX = 8; offsetX < 24; offsetX++) {
            int x = chunkX + offsetX;
            for (int offsetZ = 8; offsetZ < 24; offsetZ++) {
                int z = chunkZ + offsetZ;
                if (manager.getLavaCaveCoordinates(x, z) == Long.MIN_VALUE) continue;
                for (int y = 26; y < 96; y++) {
                    if (world.getBlock(x, y, z) != Blocks.lava || !world.isAirBlock(x, y - 1, z)) continue;
                    world.setBlock(x, y, z, Blocks.flowing_lava, 0, 2);
                    world.scheduleBlockUpdate(x, y, z, Blocks.flowing_lava, Blocks.flowing_lava.tickRate(world));
                }
            }
        }
    }

    private void decorateLavaCenter(World world, ChunkManagerRealistic manager, int chunkX, int chunkZ) {
        long center = manager.getLavaCaveCenterCoordinates(chunkX + 8, chunkZ + 8);
        if (center == Long.MIN_VALUE) return;
        int centerX = (int) (center >> 32);
        int centerZ = (int) center;
        if (Math.floorDiv(centerX, 16) * 16 != chunkX || Math.floorDiv(centerZ, 16) * 16 != chunkZ
                || manager.getVolcanoVicinityCoordinates(centerX, centerZ) != Long.MIN_VALUE)
            return;

        long choice = mix(world.getSeed() ^ center ^ 0xD1B54A32D192ED03L);
        if ((choice & 3L) == 0L) {
            placeRoundedPillar(world, centerX, centerZ, 9, LAVA_PILLAR_TOP, 6, deepslate, false);
            placeNetherPortal(world, centerX, LAVA_PILLAR_TOP + 1, centerZ, (choice & 4L) != 0L);
        } else if (puzzleMaster != null) {
            placeRoundedPillar(world, centerX, centerZ, 9, LAVA_PILLAR_TOP, 6, deepslate, true);
            world.setBlock(centerX, LAVA_PILLAR_TOP + 2, centerZ, puzzleMaster, 0, 2);
        } else {
            placeRoundedPillar(world, centerX, centerZ, 9, LAVA_PILLAR_TOP, 6, deepslate, false);
        }
    }

    private void decorateRiverJunction(World world, ChunkManagerRealistic manager, NoiseGenerator perlin, int chunkX,
            int chunkZ) {
        int[] center = findJunctionCenter(manager, chunkX, chunkZ);
        if (center == null || Math.floorDiv(center[0], 16) * 16 != chunkX
                || Math.floorDiv(center[1], 16) * 16 != chunkZ)
            return;
        if (world.getBlock(center[0], 62, center[1]).getMaterial() != net.minecraft.block.material.Material.water
                || !world.isAirBlock(center[0], 65, center[1]))
            return;
        long key = (long) center[0] << 32 ^ center[1] & 0xffffffffL;
        long choice = mix(world.getSeed() ^ key ^ 0x94D049BB133111EBL);
        if ((choice & 1L) != 0L) return;

        if (allowsTwilightPortal(choice, center[0], center[1])) {
            Block poolBlock = leavesPortalUnlit(choice) ? Blocks.water : twilightPortal;
            placeTwilightPortalIsland(world, perlin, center[0], center[1], poolBlock);
        } else if (puzzleMaster != null) {
            placeRoundedPillar(world, center[0], center[1], RIVER_PILLAR_BOTTOM, RIVER_PILLAR_TOP, 2, deepslate, true);
            world.setBlock(center[0], RIVER_PILLAR_TOP + 2, center[1], puzzleMaster, 0, 2);
        }
    }

    private void decorateLavaShore(World world, ChunkManagerRealistic manager, NoiseGenerator perlin, int chunkX,
            int chunkZ) {
        if (glowshrooms == null) return;
        for (int offsetX = 8; offsetX < 24; offsetX++) {
            int x = chunkX + offsetX;
            for (int offsetZ = 8; offsetZ < 24; offsetZ++) {
                int z = chunkZ + offsetZ;
                long cave = manager.getLavaCaveCoordinates(x, z);
                if (cave == Long.MIN_VALUE || !LavaCaveLandmark.isMainChamber(
                        perlin,
                        ContinentalNoise.unpackVolcanoX(cave),
                        ContinentalNoise.unpackVolcanoY(cave)))
                    continue;
                float patch = perlin.noise2((x + 719f) / 8f, (z - 283f) / 8f) + perlin.noise2(x / 23f, z / 23f) * .3f;
                if (patch < .62f) continue;
                long placement = mix(world.getSeed() ^ (long) x * 0x9E3779B97F4A7C15L ^ (long) z * 0xC2B2AE3D27D4EB4FL);
                if ((placement & 63L) != 0L) continue;
                int floor = findCaveFloor(world, x, z);
                if (floor >= LAVA_LEVEL && floor <= 34 && world.isAirBlock(x, floor + 1, z)) {
                    Block substrate = world.getBlock(x, floor, z);
                    int substrateMetadata = world.getBlockMetadata(x, floor, z);
                    world.setBlock(x, floor, z, Blocks.netherrack, 0, 2);
                    Random random = new Random(placement);
                    WorldGenerator glowshroom = glowshrooms[(int) (placement >>> 6 & 1L)];
                    if (!glowshroom.generate(world, random, x, floor + 1, z)) {
                        world.setBlock(x, floor, z, substrate, substrateMetadata, 2);
                    }
                }
            }
        }
    }

    private static int findCaveFloor(World world, int x, int z) {
        for (int y = 44; y >= LAVA_LEVEL; y--) {
            if (world.getBlock(x, y, z).getMaterial().isSolid() && world.isAirBlock(x, y + 1, z)) return y;
        }
        return -1;
    }

    private static int[] findJunctionCenter(ChunkManagerRealistic manager, int chunkX, int chunkZ) {
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
        return new int[] { bestX, bestZ };
    }

    private static boolean hasMountainChainNearby(ChunkManagerRealistic manager, int x, int z) {
        for (int dx = -24; dx <= 24; dx += 12) {
            for (int dz = -24; dz <= 24; dz += 12) {
                if (manager.getBiomeDataAt(x + dx, z + dz) instanceof RealisticBiomeMountainChain) return true;
            }
        }
        return false;
    }

    private static void placeRoundedPillar(World world, int centerX, int centerZ, int bottom, int top, int radius,
            Block block, boolean shieldTop) {
        int radiusSquared = radius * radius + radius / 2;
        int shieldMetadata = DungeonBrick.Type.FLOOR_SHIELDED.ordinal();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radiusSquared) continue;
                for (int y = bottom; y <= top; y++) {
                    boolean shield = shieldTop && y == top;
                    world.setBlock(
                            centerX + dx,
                            y,
                            centerZ + dz,
                            shield ? LGBlocks.DUNGEON_WALL : block,
                            shield ? shieldMetadata : 0,
                            2);
                }
            }
        }
    }

    private static void placeNetherPortal(World world, int centerX, int bottom, int centerZ, boolean alongZ) {
        for (int horizontal = -1; horizontal <= 2; horizontal++) {
            for (int y = 0; y < 5; y++) {
                boolean frame = horizontal == -1 || horizontal == 2 || y == 0 || y == 4;
                int x = centerX + (alongZ ? 0 : horizontal);
                int z = centerZ + (alongZ ? horizontal : 0);
                world.setBlock(x, bottom + y, z, frame ? Blocks.obsidian : Blocks.portal, alongZ ? 2 : 1, 2);
            }
        }
    }

    private boolean allowsTwilightPortal(long choice, int centerX, int centerZ) {
        if (twilightPortal == null || ConfigRWG.twilightForestPortalChance <= 0f) return false;
        long minimumDistance = ConfigRWG.twilightForestPortalMinimumSpawnDistance;
        if ((long) centerX * centerX + (long) centerZ * centerZ < minimumDistance * minimumDistance) return false;
        return (choice >>> 1 & 0xFFFFFFL) < (long) (ConfigRWG.twilightForestPortalChance * 0x1000000L);
    }

    private static boolean leavesPortalUnlit(long choice) {
        return (choice >>> 40 & 0xFFFFFFL) < (long) (ConfigRWG.twilightForestPortalUnlitChance * 0x1000000L);
    }

    /**
     * Raises a grass island out of the river junction, centred on the landmark, holding an irregular portal pool. Since
     * TwilightForest#133 a pool only has to be a flood-fillable blob bordered by grass or dirt, so neither the pool nor
     * the island has to be square. A pool filled with water instead of portal blocks stays a valid pool for a player to
     * light later.
     */
    private void placeTwilightPortalIsland(World world, NoiseGenerator perlin, int centerX, int centerZ,
            Block poolBlock) {
        boolean[] pool = new boolean[PORTAL_ISLAND_SPAN * PORTAL_ISLAND_SPAN];
        boolean[] land = new boolean[pool.length];
        for (int dx = -PORTAL_ISLAND_RADIUS; dx <= PORTAL_ISLAND_RADIUS; dx++) {
            for (int dz = -PORTAL_ISLAND_RADIUS; dz <= PORTAL_ISLAND_RADIUS; dz++) {
                int x = centerX + dx;
                int z = centerZ + dz;
                double distance = Math.sqrt(dx * dx + dz * dz);
                int index = islandIndex(dx, dz);
                pool[index] = distance <= Math.min(3.2f, 2.1f + perlin.noise2(x / 3.5f, z / 3.5f));
                land[index] = pool[index] || distance <= PORTAL_ISLAND_RADIUS - 2f + perlin.noise2(x / 6f, z / 6f) * 2f;
            }
        }
        for (int dx = -PORTAL_ISLAND_RADIUS; dx <= PORTAL_ISLAND_RADIUS; dx++) {
            for (int dz = -PORTAL_ISLAND_RADIUS; dz <= PORTAL_ISLAND_RADIUS; dz++) {
                // A pool block that is not bordered by land on every side breaks itself back into water.
                if (hasNeighbor(pool, dx, dz, 2)) land[islandIndex(dx, dz)] = true;
            }
        }
        for (int dx = -PORTAL_ISLAND_RADIUS; dx <= PORTAL_ISLAND_RADIUS; dx++) {
            for (int dz = -PORTAL_ISLAND_RADIUS; dz <= PORTAL_ISLAND_RADIUS; dz++) {
                int index = islandIndex(dx, dz);
                if (!land[index]) continue;
                int x = centerX + dx;
                int z = centerZ + dz;
                boolean portal = pool[index];
                int surface = portal || !isShore(land, dx, dz) ? RIVER_PILLAR_TOP : RIVER_PILLAR_TOP - 1;
                for (int y = RIVER_PILLAR_BOTTOM; y <= surface; y++) {
                    Block block = y == surface ? portal ? poolBlock : Blocks.grass
                            : y > surface - 3 ? Blocks.dirt : deepslate;
                    world.setBlock(x, y, z, block, 0, 2);
                }
                if (portal) continue;
                int poolDistance = 0;
                while (poolDistance < 3 && !hasNeighbor(pool, dx, dz, poolDistance)) poolDistance++;
                decoratePortalIsland(world, x, surface + 1, z, poolDistance);
            }
        }
    }

    /** Plants the clearing around the pool and drops the odd mossy boulder further out. */
    private void decoratePortalIsland(World world, int x, int y, int z, int poolDistance) {
        if (!world.isAirBlock(x, y, z)) return;
        long roll = mix(world.getSeed() ^ (long) x * 0x9E3779B97F4A7C15L ^ (long) z * 0xC2B2AE3D27D4EB4FL);
        int choice = (int) (roll >>> 8 & 31L);
        if (poolDistance <= 1 || choice < 13) {
            // Keeping a nature block over every bordering grass block lets a broken portal be relit.
            int flora = (int) (roll & 7L);
            world.setBlock(x, y, z, portalFlora[flora], portalFloraMetadata[flora], 2);
        } else if (choice == 13 && poolDistance >= 3) {
            world.setBlock(x, y, z, Blocks.mossy_cobblestone, 0, 2);
            if ((roll & 8L) != 0L) world.setBlock(x, y + 1, z, Blocks.cobblestone, 0, 2);
            // Metadata 1 clings to the boulder we just placed one block to the west.
            if (twilightFirefly != null && world.isAirBlock(x + 1, y, z)) {
                world.setBlock(x + 1, y, z, twilightFirefly, 1, 2);
            }
        }
    }

    private static int islandIndex(int dx, int dz) {
        return (dx + PORTAL_ISLAND_RADIUS) * PORTAL_ISLAND_SPAN + dz + PORTAL_ISLAND_RADIUS;
    }

    private static boolean isIsland(boolean[] grid, int dx, int dz) {
        return Math.abs(dx) <= PORTAL_ISLAND_RADIUS && Math.abs(dz) <= PORTAL_ISLAND_RADIUS
                && grid[islandIndex(dx, dz)];
    }

    private static boolean hasNeighbor(boolean[] grid, int dx, int dz, int radius) {
        for (int offsetX = -radius; offsetX <= radius; offsetX++) {
            for (int offsetZ = -radius; offsetZ <= radius; offsetZ++) {
                if (isIsland(grid, dx + offsetX, dz + offsetZ)) return true;
            }
        }
        return false;
    }

    private static boolean isShore(boolean[] land, int dx, int dz) {
        return !isIsland(land, dx - 1, dz) || !isIsland(land, dx + 1, dz)
                || !isIsland(land, dx, dz - 1)
                || !isIsland(land, dx, dz + 1);
    }

    private static long mix(long value) {
        value ^= value >>> 30;
        value *= 0xBF58476D1CE4E5B9L;
        value ^= value >>> 27;
        value *= 0x94D049BB133111EBL;
        return value ^ value >>> 31;
    }
}
