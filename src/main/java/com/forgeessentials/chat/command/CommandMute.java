package com.forgeessentials.chat.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.UserIdent;
import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.core.misc.TranslatedCommandException;
import com.forgeessentials.core.misc.Translator;
import com.forgeessentials.util.OutputHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandMute extends ForgeEssentialsCommandBase
{

    @Override
    public String getCommandName()
    {
        return "mute";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/mute <player>: Mutes the specified player.";
    }

    @Override
    public String getPermissionNode()
    {
        return "fe.chat.mute";
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return false;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            EntityPlayerMP receiver = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
            if (receiver == null)
                throw new TranslatedCommandException("Player %s does not exist, or is not online.", args[0]);

            receiver.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean("mute", true);
            OutputHandler.chatError(sender, Translator.format("You muted %s.", args[0]));
            OutputHandler.chatError(receiver, Translator.format("You were muted by %s.", sender.getCommandSenderName()));
        }
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

}
