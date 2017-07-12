package com.idtech.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;
import com.idtech.BaseMod;
import com.idtech.JSONManager;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class QuickTool extends ItemTool {

	protected String name = "Undefined";

	enum ToolType {
		// Taken from ItemPickaxe
		PICKAXE(Sets.newHashSet(new Block[] { Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE,
				Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB,
				Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE,
				Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE,
				Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE,
				Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON,
				Blocks.STONE_PRESSURE_PLATE })), SHOVEL(Sets.newHashSet(new Block[] { Blocks.CLAY, Blocks.DIRT,
						Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW,
						Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH })), AXE(
								Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2,
										Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK,
										Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE })), SWORD(
												Sets.newHashSet(new Block[] {}));

		Set<Block> effectiveOn;

		ToolType(Set<Block> effectiveOn) {
			this.effectiveOn = effectiveOn;
		}
	}

	ToolType toolType;

	public QuickTool(String name, ToolMaterial mat, float attackDamage, float attackSpeed, ToolType toolType) {
		super(attackSpeed, attackSpeed, mat, toolType.effectiveOn);

		this.name = name;
		this.toolType = toolType;

		this.setUnlocalizedName(BaseMod.MODID + "_" + JSONManager.safeString(name));
		System.out.println("preInit: " + name);
		GameRegistry.register(this.setRegistryName(name));

		createJSONFile();
	}

	public void registerRenderers() {
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
		builder.append("\"parent\": \"item/handheld\",");
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

	public boolean canHarvestBlock(IBlockState blockIn) {
		if (toolType == ToolType.PICKAXE) {
			Block block = blockIn.getBlock();

			if (block == Blocks.OBSIDIAN) {
				return this.toolMaterial.getHarvestLevel() == 3;
			} else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
				if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
					if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
						if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
							if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
								if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
									Material material = blockIn.getMaterial();
									return material == Material.ROCK ? true
											: (material == Material.IRON ? true : material == Material.ANVIL);
								} else {
									return this.toolMaterial.getHarvestLevel() >= 2;
								}
							} else {
								return this.toolMaterial.getHarvestLevel() >= 1;
							}
						} else {
							return this.toolMaterial.getHarvestLevel() >= 1;
						}
					} else {
						return this.toolMaterial.getHarvestLevel() >= 2;
					}
				} else {
					return this.toolMaterial.getHarvestLevel() >= 2;
				}
			} else {
				return this.toolMaterial.getHarvestLevel() >= 2;
			}
		} else
			return super.canHarvestBlock(blockIn);
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (toolType == ToolType.PICKAXE) {
			Material material = state.getMaterial();
			return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK
					? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
		}
		if (toolType == ToolType.AXE) {
			Material material = state.getMaterial();
			return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
					? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
		}
		return super.getStrVsBlock(stack, state);
	}

}
