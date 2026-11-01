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
    private static final int ISLAND_RADIUS = 7;
    private static final int ISLAND_SPAN = ISLAND_RADIUS * 2 + 1;
    /** Keeps the puzzle floor the size the rounded pillar used to give it. */
    private static final int ISLAND_PLATFORM_RADIUS_SQUARED = 5;
    /**
     * LootGames lays its board out over a 21x21 area around the floor, which is wider than the island, so the floor
     * sits on a dais above the grass and everything the island grows stays below it.
     */
    private static final int PUZZLE_FLOOR_TOP = RIVER_PILLAR_TOP + 2;

    private final Block deepslate;
    private final Block puzzleMaster;
    private final Block twilightPortal;
    private final Block twilightFirefly;
    private final Block[] islandFlora;
    private final int[] islandFloraMetadata;
    private final WorldGenerator[] glowshrooms;

    private LandmarkDecorations(Block deepslate, Block puzzleMaster, Block twilightPortal, Block twilightFirefly,
            Block[] islandFlora, int[] islandFloraMetadata, WorldGenerator[] glowshrooms) {
        this.deepslate = deepslate;
        this.puzzleMaster = puzzleMaster;
        this.twilightPortal = twilightPortal;
        this.twilightFirefly = twilightFirefly;
        this.islandFlora = islandFlora;
        this.islandFloraMetadata = islandFloraMetadata;
        this.glowshrooms = glowshrooms;
    }

    public static LandmarkDecorations create() {
        if (!Loader.isModLoaded("etfuturum")) return null;
        Block lootGame = Loader.isModLoaded("lootgames") ? LGBlocks.PUZZLE_MASTER : null;
        Block twilight = null;
        Block firefly = null;
        Block[] flora = { Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass,
                Blocks.tallgrass, Blocks.red_flower, Blocks.yellow_flower };
        int[] floraMetadata = { 1, 1, 1, 1, 2, 1, 0, 0 };
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
            placeRiverJunctionIsland(
                    world,
                    perlin,
                    center[0],
                    center[1],
                    leavesPortalUnlit(choice) ? Blocks.water : twilightPortal);
        } else if (puzzleMaster != null) {
            placeRiverJunctionIsland(world, perlin, center[0], center[1], null);
            world.setBlock(center[0], PUZZLE_FLOOR_TOP + 2, center[1], puzzleMaster, 0, 2);
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
     * Raises a grass island out of the river junction, centred on the landmark, around whichever centerpiece the
     * junction drew. A portal pool is noise-shaped: since TwilightForest#133 a pool only has to be a flood-fillable
     * blob bordered by grass or dirt, and filling it with water instead of portal blocks leaves a valid pool for a
     * player to light later. A null pool block raises the rounded puzzle dais instead.
     */
    private void placeRiverJunctionIsland(World world, NoiseGenerator perlin, int centerX, int centerZ,
            Block poolBlock) {
        boolean poolCore = poolBlock != null;
        boolean[] core = new boolean[ISLAND_SPAN * ISLAND_SPAN];
        boolean[] land = new boolean[core.length];
        for (int dx = -ISLAND_RADIUS; dx <= ISLAND_RADIUS; dx++) {
            for (int dz = -ISLAND_RADIUS; dz <= ISLAND_RADIUS; dz++) {
                int x = centerX + dx;
                int z = centerZ + dz;
                double distance = Math.sqrt(dx * dx + dz * dz);
                int index = islandIndex(dx, dz);
                core[index] = poolCore ? distance <= Math.min(3.2f, 2.1f + perlin.noise2(x / 3.5f, z / 3.5f))
                        : dx * dx + dz * dz <= ISLAND_PLATFORM_RADIUS_SQUARED;
                land[index] = core[index] || distance <= ISLAND_RADIUS - 2f + perlin.noise2(x / 6f, z / 6f) * 2f;
            }
        }
        for (int dx = -ISLAND_RADIUS; dx <= ISLAND_RADIUS; dx++) {
            for (int dz = -ISLAND_RADIUS; dz <= ISLAND_RADIUS; dz++) {
                // A pool block that is not bordered by land on every side breaks itself back into water, and the
                // puzzle floor reads better with the same collar around it.
                if (hasNeighbor(core, dx, dz, 2)) land[islandIndex(dx, dz)] = true;
            }
        }
        for (int dx = -ISLAND_RADIUS; dx <= ISLAND_RADIUS; dx++) {
            for (int dz = -ISLAND_RADIUS; dz <= ISLAND_RADIUS; dz++) {
                int index = islandIndex(dx, dz);
                if (!land[index]) continue;
                int x = centerX + dx;
                int z = centerZ + dz;
                boolean centerpiece = core[index];
                if (centerpiece && !poolCore) {
                    placePuzzleDais(world, x, z);
                    continue;
                }
                int surface = centerpiece || !isShore(land, dx, dz) ? RIVER_PILLAR_TOP : RIVER_PILLAR_TOP - 1;
                for (int y = RIVER_PILLAR_BOTTOM; y <= surface; y++) {
                    boolean top = y == surface;
                    Block block = top ? centerpiece ? poolBlock : Blocks.grass
                            : y > surface - 3 ? Blocks.dirt : deepslate;
                    world.setBlock(x, y, z, block, 0, 2);
                }
                if (centerpiece) continue;
                int coreDistance = 0;
                while (coreDistance < 3 && !hasNeighbor(core, dx, dz, coreDistance)) coreDistance++;
                if (!poolCore && coreDistance == 1) {
                    world.setBlock(
                            x,
                            RIVER_PILLAR_TOP + 1,
                            z,
                            LGBlocks.DUNGEON_WALL,
                            DungeonBrick.Type.WALL.ordinal(),
                            2);
                    continue;
                }
                decorateIslandSurface(world, x, surface + 1, z, coreDistance, poolCore);
            }
        }
    }

    /** Raises one column of the dais the minigame floor is laid out on, out of the island's grass. */
    private void placePuzzleDais(World world, int x, int z) {
        for (int y = RIVER_PILLAR_BOTTOM; y < RIVER_PILLAR_TOP; y++) {
            world.setBlock(x, y, z, y > RIVER_PILLAR_TOP - 3 ? Blocks.dirt : deepslate, 0, 2);
        }
        for (int y = RIVER_PILLAR_TOP; y <= PUZZLE_FLOOR_TOP; y++) {
            DungeonBrick.Type type = y == PUZZLE_FLOOR_TOP ? DungeonBrick.Type.FLOOR_SHIELDED : DungeonBrick.Type.WALL;
            world.setBlock(x, y, z, LGBlocks.DUNGEON_WALL, type.ordinal(), 2);
        }
    }

    /** Plants the clearing around the centerpiece and drops the odd mossy boulder further out. */
    private void decorateIslandSurface(World world, int x, int y, int z, int coreDistance, boolean poolCore) {
        if (!world.isAirBlock(x, y, z)) return;
        long roll = mix(world.getSeed() ^ (long) x * 0x9E3779B97F4A7C15L ^ (long) z * 0xC2B2AE3D27D4EB4FL);
        int choice = (int) (roll >>> 8 & 31L);
        if (coreDistance <= 1 || choice < 13) {
            // Keeping a nature block over every bordering grass block lets a broken portal be relit.
            int flora = (int) (roll & 7L);
            world.setBlock(x, y, z, islandFlora[flora], islandFloraMetadata[flora], 2);
        } else if (choice == 13 && coreDistance >= 3) {
            world.setBlock(x, y, z, Blocks.mossy_cobblestone, 0, 2);
            // A second block would reach the puzzle floor and leave no room for the board.
            if (poolCore && (roll & 8L) != 0L) world.setBlock(x, y + 1, z, Blocks.cobblestone, 0, 2);
            // Metadata 1 clings to the boulder we just placed one block to the west.
            if (twilightFirefly != null && world.isAirBlock(x + 1, y, z)) {
                world.setBlock(x + 1, y, z, twilightFirefly, 1, 2);
            }
        }
    }

    private static int islandIndex(int dx, int dz) {
        return (dx + ISLAND_RADIUS) * ISLAND_SPAN + dz + ISLAND_RADIUS;
    }

    private static boolean isIsland(boolean[] grid, int dx, int dz) {
        return Math.abs(dx) <= ISLAND_RADIUS && Math.abs(dz) <= ISLAND_RADIUS && grid[islandIndex(dx, dz)];
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
