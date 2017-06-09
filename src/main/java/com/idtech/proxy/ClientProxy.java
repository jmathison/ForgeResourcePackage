package com.idtech.proxy;

import com.idtech.BaseMod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * 
 * @author Blam
 * 
 * This Class is for client-side only code in your mod.
 *
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerEntityRenderers(){
		// Add Client Rendering Code Here		
	}
	
	
	@Override
	public void registerItemInventoryRender(Item item, String itemName) {
		// Registers the item using the renderitem
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item ,0, new ModelResourceLocation(BaseMod.instance.MODID +":" + itemName, "inventory"));
	}
	
	/** 
	 * Registers a Block with RenderItem so the texture shows up in your inventory.
	 * Will look for the texture in your assets/<mod id>/textures/block folder.
	 * 
	 * @param block the block to register
	 * @param blockName the name of the block - should be the same as the texture you want to use.
	 */
	@Override
	public void registerBlockInventoryRender(Block block, String blockName) {
		// Registers the block using the renderitem
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(block),0, new ModelResourceLocation(BaseMod.instance.MODID +":" + blockName, "inventory"));
	}

}
