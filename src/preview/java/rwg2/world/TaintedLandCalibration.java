package rwg2.world;

import java.io.File;

import rwg2.config.ConfigRWG;
import rwg2.config.ContinentalConfig;
import rwg2.util.ContinentalNoise;
import rwg2.util.PoissonPointNoise;

/**
 * Headless sampling utility for checking how much Tainted Land each placement mode produces.
 *
 * <p>
 * Per-continent placement shares the landmark probability model behind Average Landmarks Per Type And Continent, which
 * overshoots on the default continent geometry: a budget of 1.0 measures about 2 patches per continent, against roughly
 * 25 small-biome patches per continent for the field it replaces.
 *
 * <p>
 * Both figures count the raw placement field. Biome selection then thins it, dropping a patch wherever the littoral or
 * coast band wins instead, so fewer survive in world. Both modes are measured the same way, so the ratio holds, but
 * only runPreview shows the density a player actually gets.
 */
public final class TaintedLandCalibration {

    private static final long SEED = 927692613931855800L;
    private static final double SMALL_PATCH_AREA = area(ChunkManagerRealistic.SMALL_BIOME_RADIUS);
    private static final double TAINT_PATCH_AREA = area(ContinentalNoise.TAINTED_LAND_RADIUS);
    /** Land components smaller than this are islands rather than continents. */
    private static final double CONTINENT_AREA_LIMIT = 4.0e6;

    private TaintedLandCalibration() {}

    public static void main(String[] args) {
        File config = new File(System.getProperty("rwg2.previewConfig", "run/client/config/rwg2.cfg"));
        int size = Integer.getInteger("rwg2.calibrationSize", 32768);
        int step = Integer.getInteger("rwg2.calibrationStep", 32);
        String budgets = System.getProperty("rwg2.taintBudgets", "0.5,1.0,2.0,0.75,0.70");
        ConfigRWG.init(config);
        System.out.println("RWG tainted land calibration config: " + config.getAbsolutePath());
        System.out.printf("area %d x %d blocks, step %d%n%n", size, size, step);

        int width = size / step;
        double cellArea = (double) step * step;
        int half = size / 2;

        ContinentalConfig.perContinentTaintedLand = false;
        ContinentalNoise landNoise = new ContinentalNoise(SEED ^ ChunkManagerRealistic.CONTINENT_SEED_SALT);
        boolean[] land = new boolean[width * width];
        long landCells = 0L;
        for (int zIndex = 0; zIndex < width; zIndex++) {
            for (int xIndex = 0; xIndex < width; xIndex++) {
                boolean isLand = landNoise.getValue(-half + xIndex * step, -half + zIndex * step) >= 0f;
                land[zIndex * width + xIndex] = isLand;
                if (isLand) landCells++;
            }
        }
        int[] continent = new int[width * width];
        int continents = label(land, continent, width, cellArea);
        System.out.printf(
                "land %.3f of sampled area, %d continents fully inside the sample%n%n",
                landCells / (double) (width * width),
                continents);

        PoissonPointNoise smallField = new PoissonPointNoise(
                SEED ^ ChunkManagerRealistic.SMALL_BIOME_SEED_SALT,
                ChunkManagerRealistic.SMALL_BIOME_SPACING,
                4);
        double[] point = new double[5];
        long smallCells = 0L;
        long[] smallPerContinent = new long[continents + 1];
        for (int zIndex = 0; zIndex < width; zIndex++) {
            for (int xIndex = 0; xIndex < width; xIndex++) {
                int index = zIndex * width + xIndex;
                if (!land[index]) continue;
                smallField.sample(-half + xIndex * step, -half + zIndex * step, point);
                if (point[0] >= ChunkManagerRealistic.SMALL_BIOME_RADIUS) continue;
                smallCells++;
                if (continent[index] > 0) smallPerContinent[continent[index]]++;
            }
        }
        report(
                "legacy field, all small biomes",
                smallCells,
                landCells,
                cellArea,
                SMALL_PATCH_AREA,
                smallPerContinent,
                continents);

        for (String text : budgets.split(",")) {
            float budget = Float.parseFloat(text.trim());
            ContinentalConfig.perContinentTaintedLand = true;
            ContinentalConfig.averageTaintedLandPerContinent = budget;
            ContinentalNoise noise = new ContinentalNoise(SEED ^ ChunkManagerRealistic.CONTINENT_SEED_SALT);
            long taintCells = 0L;
            long[] perContinent = new long[continents + 1];
            for (int zIndex = 0; zIndex < width; zIndex++) {
                for (int xIndex = 0; xIndex < width; xIndex++) {
                    int index = zIndex * width + xIndex;
                    if (!land[index]) continue;
                    if (!noise.isTaintedLandAt(-half + xIndex * step, -half + zIndex * step)) continue;
                    taintCells++;
                    if (continent[index] > 0) perContinent[continent[index]]++;
                }
            }
            report(
                    "per continent budget " + budget,
                    taintCells,
                    landCells,
                    cellArea,
                    TAINT_PATCH_AREA,
                    perContinent,
                    continents);
        }
    }

    private static void report(String label, long cells, long landCells, double cellArea, double patchArea,
            long[] perContinent, int continents) {
        System.out.printf(
                "%-34s %.4f of land, %.2f patches per Mblocks2 of land%n",
                label,
                cells / (double) landCells,
                cells / patchArea * 1.0e6 / landCells);
        if (continents == 0) {
            System.out.printf("    no continent fits inside the sample; raise rwg2.calibrationSize%n%n");
            return;
        }
        double total = 0D;
        long attributed = 0L;
        int occupied = 0;
        for (int index = 1; index <= continents; index++) {
            total += perContinent[index] * cellArea / patchArea;
            attributed += perContinent[index];
            if (perContinent[index] > 0) occupied++;
        }
        System.out.printf(
                "%-34s mean %.2f patches per continent, %d of %d continents affected%n",
                "",
                total / continents,
                occupied,
                continents);
        // Cells on islands or on landmasses the sample clips are deliberately left out of the per-continent mean.
        if (cells > attributed) System.out.printf(
                "%-34s %.0f%% of patch area sits outside those continents and is excluded above%n",
                "",
                (cells - attributed) * 100D / cells);
        System.out.println();
    }

    private static double area(double radius) {
        return Math.PI * radius * radius;
    }

    /** Labels land components, keeping only continent-sized ones that do not touch the sampled border. */
    private static int label(boolean[] land, int[] continent, int width, double cellArea) {
        int[] scratch = new int[land.length];
        int[] stack = new int[land.length];
        int[] sizes = new int[land.length / 8 + 2];
        boolean[] clipped = new boolean[sizes.length];
        int labels = 0;
        for (int start = 0; start < land.length; start++) {
            if (!land[start] || scratch[start] != 0) continue;
            labels++;
            if (labels == sizes.length) {
                int[] grownSizes = new int[sizes.length * 2];
                boolean[] grownClipped = new boolean[sizes.length * 2];
                System.arraycopy(sizes, 0, grownSizes, 0, sizes.length);
                System.arraycopy(clipped, 0, grownClipped, 0, clipped.length);
                sizes = grownSizes;
                clipped = grownClipped;
            }
            int top = 0;
            stack[top++] = start;
            scratch[start] = labels;
            while (top > 0) {
                int index = stack[--top];
                sizes[labels]++;
                int x = index % width;
                int z = index / width;
                if (x == 0 || z == 0 || x == width - 1 || z == width - 1) clipped[labels] = true;
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int nx = x + dx;
                        int nz = z + dz;
                        if (nx < 0 || nz < 0 || nx >= width || nz >= width) continue;
                        int neighbour = nz * width + nx;
                        if (!land[neighbour] || scratch[neighbour] != 0) continue;
                        scratch[neighbour] = labels;
                        stack[top++] = neighbour;
                    }
                }
            }
        }
        int[] renumbered = new int[labels + 1];
        int kept = 0;
        for (int label = 1; label <= labels; label++) {
            if (clipped[label] || sizes[label] * cellArea < CONTINENT_AREA_LIMIT) continue;
            renumbered[label] = ++kept;
        }
        for (int index = 0; index < land.length; index++) continent[index] = renumbered[scratch[index]];
        return kept;
    }
}
