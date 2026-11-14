package rwg2.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import rwg2.support.RiverJunctionLandmark;
import rwg2.support.RiverJunctionLandmark.Centerpiece;

/**
 * Headless count of river junction landmarks under several deduplication rules, without rendering the map. Peaks are
 * found with the same per-chunk scan as world generation; the rival rules are then replayed in memory.
 */
final class JunctionAudit {

    private static final int REACH = RiverJunctionLandmark.RIVAL_CHUNKS;

    private static final class Peak {

        final int x;
        final int z;
        final float strength;
        boolean hostable;
        Centerpiece kind = Centerpiece.NONE;
        boolean water;
        boolean inside;

        Peak(int x, int z, float strength) {
            this.x = x;
            this.z = z;
            this.strength = strength;
        }

        boolean candidate() {
            return inside && hostable && kind != Centerpiece.NONE && water;
        }

        boolean portal() {
            return kind == Centerpiece.LIT_PORTAL || kind == Centerpiece.UNLIT_PORTAL;
        }

        /** Same order as {@link RiverJunctionLandmark#hasStrongerCenter}. */
        boolean beats(Peak other) {
            if (strength != other.strength) return strength > other.strength;
            return x < other.x || x == other.x && z < other.z;
        }
    }

    private JunctionAudit() {}

    static void run(long seed, int size) {
        int half = size / 2;
        int margin = REACH * 16;
        int minChunk = Math.floorDiv(-half - margin, 16);
        int maxChunk = Math.floorDiv(half + margin - 1, 16);
        int threads = Math.max(1, Runtime.getRuntime().availableProcessors());
        List<Peak> peaks = new ArrayList<Peak>();
        AtomicInteger nextRow = new AtomicInteger(minChunk);
        long start = System.nanoTime();
        System.out.printf("Junction audit: seed %d, area %d x %d blocks, %d threads%n", seed, size, size, threads);

        Thread[] workers = new Thread[threads];
        for (int t = 0; t < threads; t++) {
            workers[t] = new Thread(() -> {
                ChunkManagerRealistic manager = new ChunkManagerRealistic(seed, true);
                ChunkGeneratorRealistic generator = new ChunkGeneratorRealistic(manager, seed, true);
                List<Peak> found = new ArrayList<Peak>();
                for (int cz; (cz = nextRow.getAndIncrement()) <= maxChunk;) {
                    for (int cx = minChunk; cx <= maxChunk; cx++) {
                        int chunkX = cx * 16;
                        int chunkZ = cz * 16;
                        if (manager.getRiverJunctionStrength(chunkX + 8, chunkZ + 8) <= 0f) continue;
                        int[] p = RiverJunctionLandmark.findPeak(manager, chunkX, chunkZ);
                        if (p == null) continue;
                        Peak peak = new Peak(p[0], p[1], manager.getRiverJunctionStrength(p[0], p[1]));
                        peak.inside = p[0] >= -half && p[0] < half && p[1] >= -half && p[1] < half;
                        peak.hostable = RiverJunctionLandmark.canHost(manager, p[0], p[1]);
                        if (peak.hostable) {
                            peak.kind = RiverJunctionLandmark.pickCenterpiece(seed, p[0], p[1]);
                            if (peak.inside && peak.kind != Centerpiece.NONE)
                                peak.water = generator.hasOpenJunctionWater(p[0], p[1]);
                        }
                        found.add(peak);
                    }
                }
                synchronized (peaks) {
                    peaks.addAll(found);
                }
            });
            workers[t].start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        Map<Long, Peak> byChunk = new HashMap<Long, Peak>();
        List<Peak> candidates = new ArrayList<Peak>();
        int hostable = 0;
        for (Peak peak : peaks) {
            byChunk.put(chunkKey(Math.floorDiv(peak.x, 16), Math.floorDiv(peak.z, 16)), peak);
            if (peak.inside && peak.hostable) hostable++;
            if (peak.candidate()) candidates.add(peak);
        }
        System.out.printf(
                "Scan took %.1fs: %d peaks (with margin), %d hostable inside, %d pass coin flip and water%n%n",
                (System.nanoTime() - start) / 1e9,
                peaks.size(),
                hostable,
                candidates.size());

        List<Peak> master = new ArrayList<Peak>(candidates);
        List<Peak> strongest = filter(candidates, c -> !hasRival(byChunk, c, false));
        List<Peak> current = filter(candidates, c -> !hasRival(byChunk, c, true));

        crossCheck(seed, candidates, current);

        System.out.printf(
                "%-34s %9s %7s %10s %10s %10s %12s%n",
                "variant",
                "landmarks",
                "portals",
                "pairs<320",
                "pairs<640",
                "pairs<1250",
                "areas lost");
        report("no dedup (before)", master, master);
        report("strongest center only", strongest, master);
        report("strongest with a centerpiece", current, master);

        System.out.println();
        System.out.println("Close pairs (< 1250 blocks) left by the current rule:");
        for (String line : closePairs(current, 1250)) System.out.println("  " + line);
    }

    private static boolean hasRival(Map<Long, Peak> byChunk, Peak center, boolean rivalCoin) {
        int chunkX = Math.floorDiv(center.x, 16);
        int chunkZ = Math.floorDiv(center.z, 16);
        for (int dx = -REACH; dx <= REACH; dx++) {
            for (int dz = -REACH; dz <= REACH; dz++) {
                if (dx == 0 && dz == 0) continue;
                Peak rival = byChunk.get(chunkKey(chunkX + dx, chunkZ + dz));
                if (rival == null || !rival.hostable || !rival.beats(center)) continue;
                if (rivalCoin && rival.kind == Centerpiece.NONE) continue;
                return true;
            }
        }
        return false;
    }

    /** Confirms the in-memory replay of the current rule against the real check. */
    private static void crossCheck(long seed, List<Peak> candidates, List<Peak> current) {
        ChunkManagerRealistic manager = new ChunkManagerRealistic(seed, true);
        Set<Peak> kept = new HashSet<Peak>(current);
        int mismatches = 0;
        for (Peak candidate : candidates) {
            boolean real = !RiverJunctionLandmark.hasStrongerCenter(manager, seed, candidate.x, candidate.z);
            if (real != kept.contains(candidate)) mismatches++;
        }
        System.out.printf("Cross-check against hasStrongerCenter: %d mismatches%n%n", mismatches);
    }

    private static void report(String name, List<Peak> placed, List<Peak> master) {
        int portals = 0;
        for (Peak peak : placed) if (peak.portal()) portals++;
        // Groups master landmarks less than 320 blocks apart; a group is lost when none of it is placed.
        int[] group = new int[master.size()];
        for (int i = 0; i < group.length; i++) group[i] = i;
        for (int i = 0; i < group.length; i++) {
            for (int j = i + 1; j < group.length; j++) {
                if (distanceSquared(master.get(i), master.get(j)) < 320L * 320L) group[root(group, i)] = root(group, j);
            }
        }
        Set<Peak> kept = new HashSet<Peak>(placed);
        Set<Integer> covered = new HashSet<Integer>();
        Set<Integer> groups = new HashSet<Integer>();
        for (int i = 0; i < group.length; i++) {
            groups.add(root(group, i));
            if (kept.contains(master.get(i))) covered.add(root(group, i));
        }
        int lost = groups.size() - covered.size();
        System.out.printf(
                "%-34s %9d %7d %10d %10d %10d %12d%n",
                name,
                placed.size(),
                portals,
                pairsWithin(placed, 320),
                pairsWithin(placed, 640),
                pairsWithin(placed, 1250),
                lost);
    }

    private static int root(int[] group, int index) {
        while (group[index] != index) index = group[index] = group[group[index]];
        return index;
    }

    private static int pairsWithin(List<Peak> placed, int distance) {
        long limit = (long) distance * distance;
        int pairs = 0;
        for (int i = 0; i < placed.size(); i++) {
            for (int j = i + 1; j < placed.size(); j++) {
                if (distanceSquared(placed.get(i), placed.get(j)) < limit) pairs++;
            }
        }
        return pairs;
    }

    private static List<String> closePairs(List<Peak> placed, int distance) {
        long limit = (long) distance * distance;
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < placed.size(); i++) {
            for (int j = i + 1; j < placed.size(); j++) {
                Peak a = placed.get(i);
                Peak b = placed.get(j);
                long squared = distanceSquared(a, b);
                if (squared >= limit) continue;
                lines.add(
                        String.format(
                                "%d,%d (%.3f %s) <-> %d,%d (%.3f %s)  %.0f blocks",
                                a.x,
                                a.z,
                                a.strength,
                                a.kind,
                                b.x,
                                b.z,
                                b.strength,
                                b.kind,
                                Math.sqrt(squared)));
            }
        }
        if (lines.isEmpty()) lines.add("(none)");
        return lines;
    }

    private static long distanceSquared(Peak a, Peak b) {
        long dx = a.x - b.x;
        long dz = a.z - b.z;
        return dx * dx + dz * dz;
    }

    private static List<Peak> filter(List<Peak> peaks, Predicate<Peak> keep) {
        List<Peak> kept = new ArrayList<Peak>();
        for (Peak peak : peaks) if (keep.test(peak)) kept.add(peak);
        return kept;
    }

    private static long chunkKey(int chunkX, int chunkZ) {
        return (long) chunkX << 32 | chunkZ & 0xffffffffL;
    }
}
