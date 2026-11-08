package rwg2.world;

import rwg2.util.ClimateBands;

/** Fails if climate band placement breaks. */
public final class ClimateBandsCheck {

    private static final ClimateBands BANDS = new ClimateBands(6000D, 6000D, 12000D, 6000D);

    public static void main(String[] args) {
        // West is dry, east is wet, within the hot band south of the origin.
        expect(3, -1000D, 1000D);
        expect(4, 1000D, 1000D);
        // Humidity alternates.
        expect(3, 7000D, 1000D);
        // North gets colder.
        expect(2, 1000D, -1000D);
        expect(2, 1000D, -5999D);
        expect(1, 1000D, -6001D);
        expect(1, 1000D, -11999D);
        // Snow mirrors back into cold, with the configured total width.
        expect(2, 1000D, -12001D);
        expect(2, 1000D, -17999D);
        // South mirrors after the full hot width.
        expect(4, 1000D, 11999D);
        expect(2, 1000D, 12001D);
        expect(2, 1000D, 17999D);
        expect(1, 1000D, 18001D);
        // Bands repeat with a 30000 block period.
        for (double z = -40000D; z < 40000D; z += 137D) {
            if (BANDS.climateAt(1000D, z) != BANDS.climateAt(1000D, z + 30000D))
                throw new AssertionError("Period " + z);
        }
        // Neighbors cross temperature and humidity boundaries only when in range.
        expectNeighbor(2, 1000D, 100D, 288D);
        expectNeighbor(3, 100D, 3000D, 288D);
        expectNeighbor(4, 1000D, 3000D, 288D);
        System.out.println("Climate bands OK");
    }

    private static void expect(int climate, double x, double z) {
        int actual = BANDS.climateAt(x, z);
        if (actual != climate) throw new AssertionError(x + "," + z + " expected " + climate + " got " + actual);
    }

    private static void expectNeighbor(int climate, double x, double z, double distance) {
        int actual = BANDS.neighborClimateAt(x, z, distance);
        if (actual != climate) throw new AssertionError(x + "," + z + " neighbor " + climate + " got " + actual);
    }
}
