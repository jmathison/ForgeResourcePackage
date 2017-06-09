package com.idtech;

import java.util.Set;

import com.idtech.block.QuickBlock;
import com.idtech.item.QuickItem;

public class JSONManager {
	
	public static final String assetsDir = "../src/main/resources/assets/" + BaseMod.MODID;

	public static void buildJSON() {
		buildItems();
		buildBlocks();
	}
	
	private static void buildBlocks() {
		Set<QuickBlock> blocks = QuickBlock.getRegistry();
		for(QuickBlock block : blocks)
			block.createJSONFiles();			
	}

	private static void buildItems() {
		Set<QuickItem> items = QuickItem.getRegistry();
		for (QuickItem item : items)
			item.createJSONFile();
		
	}

	public static String jsonName(String s) {
		StringBuilder b = new StringBuilder(s.length());
		for (char c : s.toCharArray())
			b.append(Character.isAlphabetic(c) ? Character.toLowerCase(c) : c);
		return b.toString();
	}
	
	public static String safeString(String s){
		StringBuilder b = new StringBuilder(s.length());
		for(char c : s.toCharArray())
			b.append(Character.isAlphabetic(c) ? c : '_');
		return b.toString();
	}

}
