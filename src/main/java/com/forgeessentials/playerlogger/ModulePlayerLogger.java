package com.forgeessentials.playerlogger;

import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.IPermissionsHelper;
import com.forgeessentials.core.ForgeEssentials;
import com.forgeessentials.core.misc.FECommandManager;
import com.forgeessentials.core.moduleLauncher.FEModule;
import com.forgeessentials.playerlogger.command.CommandPlayerlogger;
import com.forgeessentials.playerlogger.command.CommandRollback;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleInitEvent;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleServerPreInitEvent;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleServerStoppedEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@FEModule(name = "PlayerLogger", parentMod = ForgeEssentials.class)
public class ModulePlayerLogger
{

    public static final String PERM = "fe.pl";
    public static final String PERM_WAND = PERM + ".wand";
    public static final String PERM_COMMAND = PERM + ".cmd";

    private static PlayerLogger logger;

    @SuppressWarnings("unused")
    private PlayerLoggerEventHandler eventHandler;

    @SubscribeEvent
    public void load(FEModuleInitEvent e)
    {
        logger = new PlayerLogger();
        eventHandler = new PlayerLoggerEventHandler();
        ForgeEssentials.getConfigManager().registerLoader("PlayerLogger", new PlayerLoggerConfig());

        FECommandManager.registerCommand(new CommandRollback());
        FECommandManager.registerCommand(new CommandPlayerlogger());
        // FECommandManager.registerCommand(new CommandTestPlayerlogger());
    }

    @SubscribeEvent
    public void serverPreInit(FEModuleServerPreInitEvent e)
    {
        registerPermissions(APIRegistry.perms);
        logger.loadDatabase();
    }

    private void registerPermissions(IPermissionsHelper p)
    {
        p.registerPermission(PERM, RegisteredPermValue.OP, "Player logger permisssions");
        p.registerPermission(PERM_WAND, RegisteredPermValue.OP, "Allow usage of player loggger wand (clock)");
    }

    @SubscribeEvent
    public void serverStopped(FEModuleServerStoppedEvent e)
    {
        if (logger != null)
            logger.close();
    }

    public static PlayerLogger getLogger()
    {
        return logger;
    }

}
