package com.forgeessentials.commands.player;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.UserIdent;
import com.forgeessentials.commands.util.FEcmdModuleCommands;
import com.forgeessentials.core.misc.TranslatedCommandException;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.PlayerUtil;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSmite extends FEcmdModuleCommands
{

    @Override
    public String getCommandName()
    {
        return "smite";
    }

    @Override
    public void processCommandPlayer(EntityPlayerMP sender, String[] args)
    {
        if (args.length == 1)
        {
            if (args[0].toLowerCase().equals("me"))
            {
                sender.worldObj.addWeatherEffect(new EntityLightningBolt(sender.worldObj, sender.posX, sender.posY, sender.posZ));
                OutputHandler.chatConfirmation(sender, "Was that really a good idea?");
            }
            else
            {
                EntityPlayerMP player = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
                if (player != null)
                {
                    player.worldObj.addWeatherEffect(new EntityLightningBolt(player.worldObj, player.posX, player.posY, player.posZ));
                    OutputHandler.chatConfirmation(sender, "You should feel bad about doing that.");
                }
                else
                    throw new TranslatedCommandException("Player %s does not exist, or is not online.", args[0]);
            }
        }
        else if (args.length > 1)
        {
            if (args.length != 3)
            {
                throw new TranslatedCommandException("Need coordinates X, Y, Z.");
            }
            int x = Integer.valueOf(args[0]);
            int y = Integer.valueOf(args[1]);
            int z = Integer.valueOf(args[2]);
            sender.worldObj.addWeatherEffect(new EntityLightningBolt(sender.worldObj, x, y, z));
            OutputHandler.chatConfirmation(sender, "I hope that didn't start a fire.");
        }
        else
        {
            MovingObjectPosition mop = PlayerUtil.getPlayerLookingSpot(sender, 500);
            if (mop == null)
            {
                OutputHandler.chatError(sender, "You must first look at the ground!");
            }
            else
            {
                sender.worldObj.addWeatherEffect(new EntityLightningBolt(sender.worldObj, mop.blockX, mop.blockY, mop.blockZ));
                OutputHandler.chatConfirmation(sender, "I hope that didn't start a fire.");
            }
        }
    }

    @Override
    public void processCommandConsole(ICommandSender sender, String[] args)
    {
        if (args.length >= 1)
        {
            EntityPlayerMP player = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
            if (player != null)
            {
                player.worldObj.addWeatherEffect(new EntityLightningBolt(player.worldObj, player.posX, player.posY, player.posZ));
                OutputHandler.chatConfirmation(sender, "You should feel bad about doing that.");
            }
            else
                throw new TranslatedCommandException("Player %s does not exist, or is not online.", args[0]);
        }
        else
            throw new TranslatedCommandException(getCommandUsage(sender));
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return true;
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
        if (sender instanceof EntityPlayer)
        {
            return "/smite [me|player] Smite yourself, another player, or the spot you are looking at.";
        }
        else
        {
            return "/smite <player> Smite someone.";
        }
    }

}
