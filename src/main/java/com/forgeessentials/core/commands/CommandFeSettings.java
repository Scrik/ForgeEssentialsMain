package com.forgeessentials.core.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import org.apache.commons.lang3.StringUtils;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.RootZone;
import com.forgeessentials.api.permissions.Zone;
import com.forgeessentials.core.ForgeEssentials;
import com.forgeessentials.core.misc.TranslatedCommandException;
import com.forgeessentials.core.misc.Translator;
import com.forgeessentials.core.moduleLauncher.config.ConfigLoader;
import com.forgeessentials.util.CommandParserArgs;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleServerPostInitEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CommandFeSettings extends ParserCommandBase implements ConfigLoader
{

    public static final String CONFIG_FILE = "Settings";

    public static Map<String, String> aliases = new HashMap<>();

    public static Map<String, String> values = new HashMap<>();

    private Configuration config;

    public CommandFeSettings()
    {
        ForgeEssentials.BUS.register(this);
        ForgeEssentials.getConfigManager().registerLoader(CONFIG_FILE, this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void serverStartedEvent(FEModuleServerPostInitEvent event)
    {
        loadSettings();
        config.save();
    }

    public static void addAlias(String alias, String permission)
    {
        aliases.put(alias, permission);
    }

    @Override
    public String getCommandName()
    {
        return "fesettings";
    }

    @Override
    public String[] getDefaultAliases()
    {
        return new String[] { "feconfig" };
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/fesettings [id] [value]: Change FE settings";
    }

    @Override
    public String getPermissionNode()
    {
        return "fe.settings";
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return true;
    }

    @Override
    public void parse(CommandParserArgs arguments)
    {
        if (arguments.isEmpty())
        {
            arguments.confirm("Available settings: " + StringUtils.join(aliases.keySet(), ", "));
            return;
        }

        arguments.tabComplete(aliases.keySet());
        String key = arguments.remove().toLowerCase();
        String perm = aliases.get(key);
        if (perm == null)
            throw new TranslatedCommandException("Unknown FE setting %s", key);

        if (arguments.isEmpty())
        {
            String rootValue = APIRegistry.perms.getServerZone().getRootZone().getGroupPermission(Zone.GROUP_DEFAULT, perm);
            String globalValue = APIRegistry.perms.getServerZone().getGroupPermission(Zone.GROUP_DEFAULT, perm);
            if (!rootValue.equals(globalValue))
                arguments.confirm(Translator.format("Registered setting of %s is %s, but global perm value is set to %s", key, rootValue, globalValue));
            else
                arguments.confirm(Translator.format("Registered setting of %s is %s", key, rootValue));
            return;
        }

        arguments.tabComplete(Zone.PERMISSION_TRUE, Zone.PERMISSION_FALSE);
        String value = arguments.remove();

        APIRegistry.perms.registerPermissionProperty(perm, value);
        config.get("Settings", key, "").set(value);
        config.save();
    }

    public void loadSettings()
    {
        if (config == null)
            return;
        RootZone root = APIRegistry.perms.getServerZone().getRootZone();
        for (Entry<String, String> setting : aliases.entrySet())
        {
            String defaultValue = root.getGroupPermission(Zone.GROUP_DEFAULT, setting.getValue());
            if (defaultValue == null)
                defaultValue = "";
            String desc = APIRegistry.perms.getPermissionDescription(setting.getValue());
            String help = String.format("%s = %s\n%s", setting.getValue(), defaultValue, desc);
            String value = config.get("Settings", setting.getKey(), "", help).getString();
            if (!value.isEmpty())
                APIRegistry.perms.registerPermissionProperty(setting.getValue(), value);
        }
    }

    @Override
    public void load(Configuration config, boolean isReload)
    {
        this.config = config;
        if (isReload)
            loadSettings();
    }

    @Override
    public void save(Configuration config)
    {
        /* do nothing */
    }

    @Override
    public boolean supportsCanonicalConfig()
    {
        return false;
    }

}
