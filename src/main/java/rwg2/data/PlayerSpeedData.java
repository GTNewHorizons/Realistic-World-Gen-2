package rwg2.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/** Persistent RWG2 speed settings which are independent of capabilities owned by Minecraft and other mods. */
public final class PlayerSpeedData {

    public static final float DEFAULT_FLIGHT_MULTIPLIER = 1.0f;
    private static final String FLIGHT_MULTIPLIER_KEY = "rwg2FlightSpeedMultiplier";

    private PlayerSpeedData() {}

    public static float getFlightMultiplier(EntityPlayer player) {
        NBTTagCompound persisted = getPersistedData(player, false);
        if (persisted == null || !persisted.hasKey(FLIGHT_MULTIPLIER_KEY)) {
            return DEFAULT_FLIGHT_MULTIPLIER;
        }

        float multiplier = persisted.getFloat(FLIGHT_MULTIPLIER_KEY);
        return isValid(multiplier) ? multiplier : DEFAULT_FLIGHT_MULTIPLIER;
    }

    public static void setFlightMultiplier(EntityPlayer player, float multiplier) {
        getPersistedData(player, true)
                .setFloat(FLIGHT_MULTIPLIER_KEY, isValid(multiplier) ? multiplier : DEFAULT_FLIGHT_MULTIPLIER);
    }

    private static NBTTagCompound getPersistedData(EntityPlayer player, boolean create) {
        NBTTagCompound entityData = player.getEntityData();
        if (entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            return entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }
        if (!create) {
            return null;
        }

        NBTTagCompound persisted = new NBTTagCompound();
        entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persisted);
        return persisted;
    }

    private static boolean isValid(float multiplier) {
        return !Float.isNaN(multiplier) && !Float.isInfinite(multiplier) && multiplier >= 0.0f && multiplier <= 10.0f;
    }
}
