package com.idtech;

import com.idtech.block.QuickBlock;
import com.idtech.item.ItemMod;
import com.idtech.item.QuickItem;
import com.idtech.proxy.CommonProxy;
import com.idtech.recipe.RecipeMod;
import com.idtech.world.WorldMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BaseMod.MODID, version = BaseMod.VERSION)
public class BaseMod
{



	/**
	 * Change MODID to a unique name for your mod.
	 * You can change VERSION to higher numbers as you make new versions.
	 */
	public static final String MODID = "examplemod";
	public static final String VERSION = "1.0";

	/**
	 * ----DO NOT EDIT----
	 * BaseMod.instance will get the currently running copy of your mod.
	 * Used in other mod classes.
	 */
	@Instance(MODID)
	public static BaseMod instance;

	/**
	 * ----DO NOT EDIT----
	 * Sided proxies for your mod. Used in cases where code must be only run on either the client or the server.
	 */
	@SidedProxy(modId=MODID, clientSide="com.idtech.proxy.ClientProxy", serverSide="com.idtech.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		
		// Mod PreInit
		ItemMod.preInit();
		QuickItem.preInit();
		QuickBlock.preInit();

		LanguageLocalization.createLanguageFile();
		JSONManager.buildJSON();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		// Mod Init
		ItemMod.init();
		QuickItem.init();
		QuickBlock.init();
		RecipeMod.init();
		
		WorldMod.init();


	}


}
