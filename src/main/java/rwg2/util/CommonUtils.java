package rwg2.util;

import java.io.File;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommonUtils {

    public static File getSaveLocation(World world) {
        File base = DimensionManager.getCurrentSaveRootDirectory();
        return world.provider.dimensionId == 0 ? base : new File(base, world.provider.getSaveFolder());
    }
}
