package com.idtech.item;

import com.idtech.SoundHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.SoundEvent;

public class ItemSpeaker extends QuickItem {
	
	int i = 0;
	
	{
		name = "Speaker";
		tab = CreativeTabs.MISC;
		texture = "speaker";
		type = ItemType.HandHeld;
	}

	public void onRightClick(){
		SoundEvent sound = SoundHelper.soundEvents[i];
		String soundName = SoundHelper.soundStrings[i];
		playSound(sound);
		displayMessage(soundName);
		if(!world.isRemote) i = ++i % SoundHelper.soundEvents.length;
	}
	
}
