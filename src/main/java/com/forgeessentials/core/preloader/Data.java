package com.forgeessentials.core.preloader;

import java.util.HashMap;

/**
 * Kindly do not reference any FE classes outside the coremod package in this
 * class. This is a store room for all String[]s used by the coremod, 99% of
 * stuff is edited here and not in the actual coremod classes.
 */

// Change me if this class is updated for future minecraft versions
// MC version: 1.6.4
public class Data
{

	protected static String[]	transformers	=
												{ "com.forgeessentials.core.preloader.asm.FEAccessTransformer", "com.forgeessentials.core.preloader.asm.FEeventAdder", "com.forgeessentials.core.preloader.asm.FEPacketAnalyzer"};
	
	public static HashMap<String, String>	ISob       = new HashMap<String, String>();
	public static HashMap<String, String>	ISdev      = new HashMap<String, String>();
	
	public static HashMap<String, String>   MCob         = new HashMap<String, String>();
	public static HashMap<String, String>   MCdev        = new HashMap<String, String>();
	
	public static HashMap<String, String>   TCob         = new HashMap<String, String>();
    public static HashMap<String, String>   TCdev        = new HashMap<String, String>();
	
	static
	{
		
		ISob.put("className", "yd");
        ISob.put("javaClassName", "yd");
        ISob.put("targetMethodName", "a");// searge name func_77943_a
        ISob.put("itemstackJavaClassName", "yd");
        ISob.put("entityPlayerJavaClassName", "ue");
        ISob.put("worldJavaClassName", "abv");

        ISdev.put("className", "net.minecraft.item.ItemStack");
        ISdev.put("javaClassName", "net/minecraft/item/ItemStack");
        ISdev.put("targetMethodName", "tryPlaceItemIntoWorld");
        ISdev.put("itemstackJavaClassName", "net/minecraft/item/ItemStack");
        ISdev.put("entityPlayerJavaClassName", "net/minecraft/entity/player/EntityPlayer");
        ISdev.put("worldJavaClassName", "net/minecraft/world/World");
        
        MCob.put("className", "cm");
        MCob.put("targetMethod1", "a");// searge name func_74429_a
        MCob.put("targetMethod2", "b");// searge name func_74436_b
        MCob.put("packetName", "ex");
        
        MCdev.put("className", "net.minecraft.network.MemoryConnection");
        MCdev.put("targetMethod1", "addToSendQueue");
        MCdev.put("targetMethod2", "processOrCachePacket");
        MCdev.put("packetName", "net/minecraft/network/packet/Packet");
        
        TCob.put("className", "cn");
        TCob.put("targetMethod1", "a");// searge namefunc_74429_a
        TCob.put("targetMethod2", "i");// searge name func_74447_i
        TCob.put("packetName", "ex");
        
        TCdev.put("className", "net.minecraft.network.TcpConnection");
        TCdev.put("targetMethod1", "addToSendQueue");
        TCdev.put("targetMethod2", "readPacket");
        TCdev.put("packetName", "net/minecraft/network/packet/Packet");
	}
}
