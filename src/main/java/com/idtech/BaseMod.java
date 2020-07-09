package com.idtech;

import com.idtech.block.QuickBlock;
import com.idtech.item.ItemMod;
import com.idtech.item.QuickItem;
import com.idtech.proxy.CommonProxy;
import com.idtech.world.WorldMod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = BaseMod.MODID, version = BaseMod.VERSION)
@Mod.EventBusSubscriber
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
		
		WorldMod.init();


	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		for(QuickBlock b : QuickBlock.getRegistry()){
			event.getRegistry().register(b.setRegistryName(b.getName()));
		}
		
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for(QuickItem i : QuickItem.getRegistry()) {
			event.getRegistry().register(i.setRegistryName(i.getName()));
		}
		
		for(Block b : QuickBlock.getRegistry()){
			event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()) );
		}
		
	}


}
