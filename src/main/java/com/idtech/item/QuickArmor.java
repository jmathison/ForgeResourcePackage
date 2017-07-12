package com.idtech.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.idtech.BaseMod;
import com.idtech.JSONManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class QuickArmor extends ItemArmor {

	public final String name;
	public final String textureName;
	
	public QuickArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String itemTextureName, String armorTextureName) {
		super(materialIn, 0, equipmentSlotIn);

		this.name = itemTextureName;
		this.textureName = armorTextureName;
		this.setUnlocalizedName(BaseMod.MODID + "_" + itemTextureName);
		this.setCreativeTab(CreativeTabs.COMBAT);
		
		createJSONFile();
		
		GameRegistry.register(this.setRegistryName(itemTextureName));
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot,
            String type) {
 
        if(slot == EntityEquipmentSlot.LEGS){
            return "examplemod:textures/models/armor/" + textureName + "_layer_2.png";
        }
        return "examplemod:textures/models/armor/" + textureName + "_layer_1.png";
    }

	public void registerRenderers()
	{
		System.out.println("init: " + name);
		BaseMod.proxy.registerItemInventoryRender(this, name);
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
