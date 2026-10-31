package rwg2.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import rwg2.data.PlayerSpeedData;

public class FlightSpeedPacket implements IMessage {

    private float multiplier;

    public FlightSpeedPacket() {}

    public FlightSpeedPacket(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        multiplier = buffer.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeFloat(multiplier);
    }

    public static class Handler implements IMessageHandler<FlightSpeedPacket, IMessage> {

        @Override
        public IMessage onMessage(final FlightSpeedPacket message, MessageContext context) {
            Minecraft.getMinecraft().func_152344_a(new Runnable() {

                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                    if (player != null) {
                        PlayerSpeedData.setFlightMultiplier(player, message.multiplier);
                    }
                }
            });
            return null;
        }
    }
}
