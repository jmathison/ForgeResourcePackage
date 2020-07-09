package com.idtech;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.idtech.block.QuickBlock;
import com.idtech.item.QuickItem;

public class LanguageLocalization {

	public static void createLanguageFile(){
		Set<QuickItem> items = QuickItem.getRegistry();
		Set<QuickBlock> blocks = QuickBlock.getRegistry();
		StringBuilder builder = new StringBuilder();
		
		// Line break
		builder.append('\n');
		
		for(QuickItem item : items){
			builder.append(item.getUnlocalizedName() + ".name=" + item.getName() + "\n");
		}
		
		for(QuickBlock block : blocks){
			builder.append(block.getUnlocalizedName() + ".name=" + block.getName() + "\n");
		}
		
		File custom = Paths.get(".").resolve("../src/main/resources/assets/" + BaseMod.MODID + "/lang/en_us_custom.lang").toFile();
		File f = Paths.get(".").resolve("../src/main/resources/assets/" + BaseMod.MODID + "/lang/en_us.lang").toFile();
		if(f.exists()){
			f.delete();
		}
			
		try {
			FileUtils.copyFile(custom, f);
			FileUtils.writeStringToFile(f, builder.toString(), true);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
