package com.forgeessentials.commands.player;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.UserIdent;
import com.forgeessentials.commands.util.FEcmdModuleCommands;
import com.forgeessentials.commands.util.PlayerInvChest;
import com.forgeessentials.core.misc.TranslatedCommandException;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Opens other player inventory.
 *
 * @author Dries007
 */
public class CommandInventorySee extends FEcmdModuleCommands
{

    public CommandInventorySee()
    {
    }

    @Override
    public String getCommandName()
    {
        return "invsee";
    }

    @Override
    public void processCommandPlayer(EntityPlayerMP sender, String[] args)
    {
        if (args[0] == null)
            throw new TranslatedCommandException("You need to specify a player!");

        if (!FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            return;
        }
        EntityPlayerMP player = sender;
        EntityPlayerMP victim = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
        if (victim == null)
            throw new TranslatedCommandException("Player %s not found.", args[0]);

        if (player.openContainer != player.inventoryContainer)
        {
            player.closeScreen();
        }
        player.getNextWindowId();

        PlayerInvChest chest = new PlayerInvChest(victim, sender);
        player.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(player.currentWindowId, 0, chest.getInventoryName(), chest.getSizeInventory(), true));
        player.openContainer = new ContainerChest(player.inventory, chest);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addCraftingToCrafters(player);
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return false;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
        }
        else
        {
            return null;
        }
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/invsee See a player's inventory.";
    }

}
