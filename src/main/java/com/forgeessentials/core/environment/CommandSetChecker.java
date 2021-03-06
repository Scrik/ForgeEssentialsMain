package com.forgeessentials.core.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fe.server.CommandHandlerForge;

import com.forgeessentials.core.ForgeEssentials;
import com.google.common.collect.HashMultimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class CommandSetChecker
{

    public static final String[] FIELDNAME = { "commandSet", "c", "field_71561_b", "z/c" };

    public static void remove()
    {
        ForgeEssentials.log.debug("Running duplicate command removal process!");
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server.getCommandManager() instanceof CommandHandler)
        {
            try
            {
                HashMap<String, ICommand> initials = new HashMap<String, ICommand>();
                HashMultimap<String, ICommand> duplicates = HashMultimap.create();

                Set<ICommand> cmdList = ReflectionHelper.getPrivateValue(CommandHandler.class, (CommandHandler) server.getCommandManager(), FIELDNAME);
                ForgeEssentials.log.debug("commandSet size: " + cmdList.size());

                ICommand keep;
                for (ICommand cmd : cmdList)
                {
                    keep = initials.put(cmd.getCommandName(), cmd);
                    if (keep != null)
                    {
                        ForgeEssentials.log.debug("Duplicate command found! Name:" + keep.getCommandName());
                        duplicates.put(cmd.getCommandName(), cmd);
                        duplicates.put(cmd.getCommandName(), keep);
                        continue;
                    }
                    CommandHandlerForge.doPermissionReg(cmd);
                }

                Set<ICommand> toRemove = new HashSet<ICommand>();
                keep = null;
                Class<? extends ICommand> cmdClass;
                int kept = -1, other = -1;
                for (String name : duplicates.keySet())
                {
                    keep = null;
                    kept = -1;
                    other = -1;
                    cmdClass = null;

                    for (ICommand cmd : duplicates.get(name))
                    {
                        other = getCommandPriority(cmd);

                        if (keep == null)
                        {
                            kept = other;

                            if (kept == -1)
                            {
                                keep = null;
                                duplicates.remove(name, cmd);
                            }
                            else
                            {
                                keep = cmd;
                            }

                            continue;
                        }

                        if (kept > other)
                        {
                            toRemove.add(cmd);
                            cmdClass = cmd.getClass();
                            ForgeEssentials.log.debug("Removing command '" + cmd.getCommandName() + "' from class: " + cmdClass.getName());
                        }
                        else
                        {
                            toRemove.add(keep);
                            cmdClass = keep.getClass();
                            ForgeEssentials.log.debug("Removing command '" + keep.getCommandName() + "' from class: " + cmdClass.getName());

                            keep = cmd;
                            kept = other;
                        }

                    }
                }

                cmdList.removeAll(toRemove);
                ForgeEssentials.log.debug("commandSet size: " + cmdList.size());
                ReflectionHelper.setPrivateValue(CommandHandler.class, (CommandHandler) server.getCommandManager(), cmdList, FIELDNAME);
            }
            catch (Exception e)
            {
                ForgeEssentials.log.debug("Something broke: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    // 0 = vanilla. 1 = fe. 2 = other mods
    private static int getCommandPriority(ICommand cmd)
    {
        try
        {
            Class<?> cmdClass = cmd.getClass();
            Package pkg = cmdClass.getPackage();
            if (pkg == null)
                return 0;
            if (pkg.getName().contains("net.minecraft"))
                return 0;
            if (pkg.getName().contains("forgeessentials"))
                return 1;
            return 2;
        }
        catch (Exception e)
        {
            ForgeEssentials.log.debug("Can't remove " + cmd.getCommandName());
            ForgeEssentials.log.debug("" + e.getLocalizedMessage());
            e.printStackTrace();
            return -1;
        }
    }

}
