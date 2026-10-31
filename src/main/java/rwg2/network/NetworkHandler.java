package rwg2.network;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import rwg2.data.PlayerSpeedData;

public final class NetworkHandler {

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("rwg2_speed");

    private NetworkHandler() {}

    public static void init() {
        CHANNEL.registerMessage(FlightSpeedPacket.Handler.class, FlightSpeedPacket.class, 0, Side.CLIENT);
    }

    public static void syncFlightSpeed(EntityPlayerMP player) {
        CHANNEL.sendTo(new FlightSpeedPacket(PlayerSpeedData.getFlightMultiplier(player)), player);
    }
}
