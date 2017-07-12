package com.idtech.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.idtech.BaseMod;
import com.idtech.JSONManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class QuickFood extends ItemFood {

	public final String name;

	public OnEatEvent onEat;

	public static abstract class OnEatEvent {
		ItemStack itemStack = null;
		Item item = null;
		World world = null;
		EntityPlayer player = null;
		
		public abstract void onEat();
	}

	public QuickFood(String name, int amount, float saturation, boolean isAlwaysEdible, boolean isWolfFood) {
		super(amount, isWolfFood);

		this.name = name;

		this.setUnlocalizedName(BaseMod.MODID + "_" + name);
		this.setCreativeTab(CreativeTabs.FOOD);

		if (isAlwaysEdible)
			this.setAlwaysEdible();

		createJSONFile();

		GameRegistry.register(this.setRegistryName(name));
	}
	
	public void registerRenderers()
	{
		System.out.println("init: " + name);
		BaseMod.proxy.registerItemInventoryRender(this, name);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);

		if (onEat != null) {

			onEat.item = stack.getItem();
			onEat.itemStack = stack;
			onEat.world = worldIn;
			onEat.player = player;

			onEat.onEat();
		}
	}

	public void createJSONFile() {
		File f = Paths.get(".").resolve(JSONManager.assetsDir + "/models/item/" + JSONManager.jsonName(name) + ".json")
				.toFile();

		if (f.exists()) {
			f.delete();
		}

		StringBuilder builder = new StringBuilder();

		builder.append("{");
		builder.append("\"parent\": \"item/generated\",");
		builder.append("\"textures\": {");
		builder.append("   \"layer0\": \"" + BaseMod.MODID + ":items/" + JSONManager.jsonName(name) + "\"");
		builder.append("}");
		builder.append("}");

		try {
			FileUtils.writeStringToFile(f, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
