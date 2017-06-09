package com.idtech.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * 
 * @author Blam
 * 
 * This class is for server-side code in your mod.
 *
 */
public class CommonProxy {

	public void registerEntityRenderers() {
		// TODO Auto-generated method stub
		// Don't do anything because this will run server side - there's no rendering on server side.
	}
	
	/** 
	 * Registers an item with RenderItem so the texture shows up in game.
	 * Will look for the texture in your mod's /textures/item folder.
	 * 
	 * @param item the item to register
	 * @param itemName the name of the item - should be the same as the texture you want to use.
	 */
	public void registerItemInventoryRender(Item item, String itemName) {
		// Don't do anything, runs server-side. Client implementation is in ClientProxy
	}
	
	/** 
	 * Registers a Block with RenderItem so the texture shows up in your inventory.
	 * Will look for the texture in your mod's /textures/block folder.
	 * 
	 * @param block the block to register
	 * @param blockName the name of the block - should be the same as the texture you want to use.
	 */
	public void registerBlockInventoryRender(Block block, String blockName) {
		// Don't do anything, runs server-side. Client implementation is in ClientProxy
	}

}
