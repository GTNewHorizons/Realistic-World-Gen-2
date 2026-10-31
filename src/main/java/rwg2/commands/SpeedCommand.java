package rwg2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import rwg2.data.PlayerSpeedData;
import rwg2.network.NetworkHandler;

/** Changes the executing player's flight speed multiplier. */
public class SpeedCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "speed";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/speed <multiplier 0-10>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        double multiplier = parseDoubleBounded(sender, args[0], 0.0, 10.0);
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        PlayerSpeedData.setFlightMultiplier(player, (float) multiplier);
        NetworkHandler.syncFlightSpeed(player);

        sender.addChatMessage(new ChatComponentText("Flight speed set to " + multiplier + "x."));
    }
}
