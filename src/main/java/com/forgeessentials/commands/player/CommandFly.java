package com.forgeessentials.commands.player;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.commands.util.FEcmdModuleCommands;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.WorldUtil;

public class CommandFly extends FEcmdModuleCommands
{
    @Override
    public boolean canConsoleUseCommand()
    {
        return false;
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public String getCommandName()
    {
        return "fly";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/fly [true|false] Toggle flight mode.";
    }

    @Override
    public void processCommandPlayer(EntityPlayerMP player, String[] args)
    {
        if (args.length == 0)
        {
            if (!player.capabilities.allowFlying)
                player.capabilities.allowFlying = true;
            else
                player.capabilities.allowFlying = false;
        }
        else
        {
            player.capabilities.allowFlying = Boolean.parseBoolean(args[0]);
        }
        if (!player.onGround)
            player.capabilities.isFlying = player.capabilities.allowFlying;
        if (!player.capabilities.allowFlying)
            WorldUtil.placeInWorld(player);
        player.sendPlayerAbilities();
        OutputHandler.chatNotification(player, "Flying " + (player.capabilities.allowFlying ? "enabled" : "disabled"));
    }

}
