package rwg2.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.FMLLog;
import rwg2.util.CommonUtils;
import rwg2.util.NoiseImplementation;

public class RwgWorldSavedData extends WorldSavedData {

    private static RwgWorldSavedData INSTANCE = null;
    public static final String DATA_NAME = "rwg2_data";
    public static final String DATA_NAME_LEGACY = "rwg_data";
    private NoiseImplementation noiseImplementation = rwg2.util.NoiseImplementation.UNKNOWN;

    public RwgWorldSavedData() {
        super(DATA_NAME);
    }

    public RwgWorldSavedData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        String noiseImplementation = nbtTagCompound.getString("noiseImplementation");

        try {
            this.noiseImplementation = NoiseImplementation.valueOf(noiseImplementation);
        } catch (IllegalArgumentException e) {
            System.out.println("RWG ERROR LOADING DATA - NOISE IS: " + this.noiseImplementation);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        String noiseString = noiseImplementation.toString();

        try {
            nbtTagCompound.setString("noiseImplementation", noiseString);
        } catch (Exception e) {
            System.out.println("RWG ERROR SAVING DATA - NOISE IS: " + this.noiseImplementation);
        }
    }

    static void loadInstance() {
        if (INSTANCE == null) {
            WorldServer world = DimensionManager.getWorld(0);
            if (world == null) return;

            MapStorage storage = world.mapStorage;
            if (storage == null) return;

            if (INSTANCE == null) {
                migrateLegacyDataFile(world);
            }

            INSTANCE = (RwgWorldSavedData) storage.loadData(RwgWorldSavedData.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new RwgWorldSavedData();
                storage.setData(DATA_NAME, INSTANCE);
            }
        }
        INSTANCE.markDirty();
    }

    public static void setNoiseImplementation(NoiseImplementation ni) {
        loadInstance();

        INSTANCE.noiseImplementation = ni;
    }

    public static NoiseImplementation getNoiseImplementation() {
        loadInstance();
        if (INSTANCE == null) {
            return NoiseImplementation.UNKNOWN;
        }

        return INSTANCE.noiseImplementation;
    }

    private static void migrateLegacyDataFile(World world) {
        String worldSave = CommonUtils.getSaveLocation(world).getPath();
        Path fileData = Paths.get(worldSave, "data", RwgWorldSavedData.DATA_NAME + ".dat");
        Path fileDataLegacy = Paths.get(worldSave, "data", RwgWorldSavedData.DATA_NAME_LEGACY + ".dat");
        if (Files.notExists(fileData) && Files.exists(fileDataLegacy)) {
            try {
                Files.copy(fileDataLegacy, fileData);
            } catch (IOException ex) {
                FMLLog.warning("Could not copy \"rwg_data.dat\" to \"rwg2_data.dat\".");
            }
        }
    }
}
