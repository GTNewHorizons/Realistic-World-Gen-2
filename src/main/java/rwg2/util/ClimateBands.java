package rwg2.util;

/**
 * Maps coordinates to climates using mirrored temperature bands along Z and alternating humidity bands along X.
 * Climates: 1 snow, 2 cold, 3 hot, 4 wet. Z = 0 is the cold/hot boundary, with hot to the south.
 */
public final class ClimateBands {

    private final double snowLimit;
    private final double coldLimit;
    private final double foldLength;
    private final double humidityWidth;

    public ClimateBands(double snowWidth, double coldWidth, double hotWidth, double humidityWidth) {
        // Outer bands are folded, so each half-width mirrors into the configured full width.
        snowLimit = snowWidth / 2D;
        coldLimit = snowLimit + coldWidth;
        foldLength = coldLimit + hotWidth / 2D;
        this.humidityWidth = humidityWidth;
    }

    public int climateAt(double x, double z) {
        double position = floorMod(z + coldLimit, foldLength * 2D);
        if (position > foldLength) position = foldLength * 2D - position;
        if (position < snowLimit) return 1;
        if (position < coldLimit) return 2;
        return floorMod(x, humidityWidth * 2D) < humidityWidth ? 4 : 3;
    }

    /** Returns a differing climate within the given block distance, or the local climate if there is none. */
    public int neighborClimateAt(double x, double z, double distance) {
        int climate = climateAt(x, z);
        int neighbor = climateAt(x, z - distance);
        if (neighbor != climate) return neighbor;
        neighbor = climateAt(x, z + distance);
        if (neighbor != climate) return neighbor;
        neighbor = climateAt(x - distance, z);
        if (neighbor != climate) return neighbor;
        return climateAt(x + distance, z);
    }

    private static double floorMod(double value, double modulus) {
        return value - Math.floor(value / modulus) * modulus;
    }
}
