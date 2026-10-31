package rwg2.handlers;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import rwg2.network.NetworkHandler;

public class LoginHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncFlightSpeed(event);

        // TODO: Figure out how to save&detect a brand new world vs. an upgraded world, and display a message
        // conditional on that:

        /*
         * if (RwgWorldSavedData.getNoiseImplementation() == NoiseImplementation.UNKNOWN) {
         * event.player.addChatMessage(new ChatComponentText(RED +
         * "WARNING: Unable to detect worldgen version for this world. This can cause chunk border mismatch if not corrected. Please run "
         * + GOLD + "/rwg_buginfo" + WHITE + " for info on how to fix.")); }
         */
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncFlightSpeed(event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncFlightSpeed(event);
    }

    private static void syncFlightSpeed(PlayerEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            NetworkHandler.syncFlightSpeed((EntityPlayerMP) event.player);
        }
    }
}
