package com.forgeessentials.client.auth;

import com.forgeessentials.client.ForgeEssentialsClient;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import java.io.File;
import java.io.IOException;

public class AuthAutoLogin
{
    private static File KEYSTORE_DIR = new File(FMLClientHandler.instance().getSavesDirectory(), "FEAuthStore/");

    private static File KEYSTORE_FILE;

    public static NBTTagCompound KEYSTORE = new NBTTagCompound();

    /**
     * Load the keystore from its NBT save file.
     */
    public static void load()
    {
        try
        {
            KEYSTORE_FILE = new File (KEYSTORE_DIR, FMLClientHandler.instance().getClient().thePlayer.getCommandSenderName() + ".dat");
            KEYSTORE = CompressedStreamTools.read(KEYSTORE_FILE);
        }
        catch (IOException ex)
        {
            ForgeEssentialsClient.feclientlog.error("Unable to load AuthLogin keystore file - will ignore keystore.");
        }
    }

    /**
     * Set the key for the current player on a server.
     * @param serverIP IP of the server that we received the key from
     * @param key The key to persist
     */
    public static void setKey(String serverIP, String key)
    {
        KEYSTORE.setTag(serverIP, new NBTTagString(key));
        try
        {
            KEYSTORE_FILE = new File (KEYSTORE_DIR, FMLClientHandler.instance().getClient().thePlayer.getCommandSenderName() + ".dat");
            CompressedStreamTools.safeWrite(KEYSTORE, KEYSTORE_FILE);
        }
        catch (IOException e)
        {
            ForgeEssentialsClient.feclientlog.error("Unable to save AuthLogin keystore file - any keys received in this session will be discarded..");
        }
    }

    /**
     * Get the key for the current player on a server.
     * @param serverIP IP of the server requesting the key
     * @return
     */
    public static String getKey(String serverIP)
    {
        return KEYSTORE.getString(serverIP);
    }
}
