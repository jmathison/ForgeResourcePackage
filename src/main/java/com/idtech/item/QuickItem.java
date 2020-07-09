package com.idtech.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.idtech.BaseMod;
import com.idtech.JSONManager;
import com.idtech.PackageManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class QuickItem extends Item {

	public static enum ItemType {
		Generated, HandHeld;
	}

	private static Set<QuickItem> registry = new HashSet<QuickItem>();
	private static Set<Item> itemRegistry = new HashSet<Item>();
	private static Map<String, QuickItem> lookup = new HashMap<String, QuickItem>();
	protected String name = "Undefined";
	protected CreativeTabs tab = CreativeTabs.MISC;
	protected String texture = "undefined";
	protected ItemType type = ItemType.Generated;

	protected World world;
	protected EntityPlayer player;
	protected EnumHand hand;

	// When QuickItem is loaded, it will scan the item directory for all sub
	// classes of QuickItem
	// and create a newInstance of them. This allows us to simply use the
	// initialization block to specify each parameter
	// This invokes the default super constructor of QuickItem and registers
	// it. Therefore, you should almost never
	// need to call the constructor for QuickItem manually.
	static {
		// Loads all of the QuickItems that are in this package
		Set<Class> classes = PackageManager.loadClassesInPackage("com.idtech.item");
		for (Class klass : classes) {
			if (QuickItem.class.isAssignableFrom(klass) && QuickItem.class != klass) {
				try {
					QuickItem item = (QuickItem)klass.newInstance();
					lookup.put(item.name, item);
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		}
	}

	
	
	/**
	 * Returns a QuickItem by its name or {@code null} if no such QuickItem has been created.
	 * @param name the name of the Quick Item
	 * @return the specified QuickItem or {@code null} if no such QuickItem has been created.
	 */
	public static QuickItem getItem(String name){
		return lookup.get(name);
	}

	/**
	 * Creates a new QuickItem and adds it to the registry. (Note: You probably
	 * shouldn't be calling this directly)
	 */
	protected QuickItem() {
		this.registry.add(this);
	}

	public String getName() {
		return this.name;
	}

	/**
	 * This method should be called once prior to the initialization phase.
	 */
	public static void preInit() {
		System.out.println("Quick Item Registry Size: " + registry.size());
		for (QuickItem item : registry) {
			item.setUnlocalizedName(BaseMod.MODID + "_" + JSONManager.safeString(item.name));
			item.setCreativeTab(item.tab);
			System.out.println("preInit: " + item.name);
			//GameRegistry.register(item.setRegistryName(item.name));
		}
	}
	
	

	/**
	 * This method should be called during the initialization phase
	 */
	public static void init() {
		for (QuickItem item : registry) {
			System.out.println("init: " + item.name);
			BaseMod.proxy.registerItemInventoryRender(item, item.name);
		}
	}

	/**
	 * Returns an immutable view of the QuickItem register
	 * 
	 * @return an immutable view of the QuickItem register
	 */
	public static Set<QuickItem> getRegistry() {
		return Collections.unmodifiableSet(registry);
	}

	/**
	 * This method is called each time the item is right clicked. Prior to being
	 * called, player, world, and hand are all updated.
	 */
	protected void onRightClick() {
	}

	/**
	 * Returns the {@link BlockPos} of the block at the players cursor within
	 * the specified range or {@code null} if no block is at the cursor or
	 * within range.
	 * 
	 * @param range
	 *            the distance to check
	 * @return The BlockPos of the block at the cursor or {@code null} if there
	 *         is no block in range.
	 */
	protected BlockPos findBlockAtCursor(float range) {
		Vec3d posVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d lookVec = player.getLookVec();

		// Draw a line from the player to where the player is aiming, save it if
		// we hit a block.
		// TODO: 1.12 Change - lookVec uses x, y, z
		RayTraceResult blockHit = world.rayTraceBlocks(posVec,
				posVec.addVector(lookVec.x * range, lookVec.y * range, lookVec.z * range));
		if (blockHit == null)
			return null;
		BlockPos block = blockHit.getBlockPos().offset(blockHit.sideHit);
		return block;
	}

	/**
	 * Moves the player to the specified block
	 * 
	 * @param block
	 *            the block where the playe will be moved.
	 */
	protected void moveToBlock(BlockPos block) {
		if (block == null)
			return;
		player.setPosition(block.getX(), block.getY(), block.getZ());
	}

	/**
	 * Creates a Lightning Bolt at the specified block.
	 * 
	 * @param block
	 *            The block where lightning will strike.
	 */
	protected void createLightningBolt(BlockPos block) {
		if (block == null)
			return;
		world.addWeatherEffect(new EntityLightningBolt(world, block.getX(), block.getY(), block.getZ(), false));
	}

	/**
	 * Creates an Explosion at the specified block.
	 * 
	 * @param block
	 *            the block where the explosion should originate
	 * @param size
	 *            the size of the explosion
	 * @param destroysBlocks
	 *            {@code true} if you want the explosioni to destroy blocks and
	 *            {@false otherwise}
	 */
	protected void createExplosion(BlockPos block, float size, boolean destroysBlocks) {
		if (block == null)
			return;
		if (!world.isRemote)
			world.createExplosion(player, block.getX(), block.getY(), block.getZ(), size, destroysBlocks);
	}

	/**
	 * Generates a sound at the players current position. Each call to this
	 * method generates a random pitch applied to the audio.
	 * 
	 * @param sound
	 *            The sound to generate
	 */
	protected void playSound(SoundEvent sound) {
		world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, sound, SoundCategory.NEUTRAL, 0.5f,
				0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	}

	/**
	 * Displays a message on the players screen using the default font
	 * 
	 * @param message
	 *            The message to display
	 */
	protected void displayMessage(String message) {
		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(message));
		}
	}

	/**
	 * Spawns particles at the specified target.
	 * 
	 * @param target
	 *            the Target where particles should spawn
	 * @param type
	 *            the type of particle which will be spawned
	 * @param intensity
	 *            the intensity of the particle effect, a value of 0 results in
	 *            no particles.
	 */
	protected void spawnParticles(Entity target, EnumParticleTypes type, int intensity) {
		for (int i = 0; i < intensity; i++) {
			double x = target.posX + (Math.random() - 0.5) * target.width;
			double y = target.posY + Math.random() * target.height - target.getYOffset();
			double z = target.posZ + (Math.random() - 0.5) * target.width;
			world.spawnParticle(type, x, y, z, 0, 0, 0);
		}

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// Store all of the incoming arguments of this method into local member
		// variables and then calls onRightClick()
		// This allows us to ignore return types and method parameters in our
		// onRightClick method.
		world = worldIn;
		player = playerIn;
		hand = handIn;
		onRightClick();
		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	/**
	 * This method creates the JSON Files necessary for the Minecraft mod. If
	 * you want to use your own custom JSON files @Override this method.
	 */
	public void createJSONFile() {
		File f = Paths.get(".").resolve(JSONManager.assetsDir + "/models/item/" + JSONManager.jsonName(name) + ".json")
				.toFile();

		if (f.exists()) {
			f.delete();
		}

		StringBuilder builder = new StringBuilder();

		builder.append("{");
		builder.append("\"parent\": \"item/" + type.toString().toLowerCase() + "\",");
		builder.append("\"textures\": {");
		builder.append("   \"layer0\": \"" + BaseMod.MODID + ":items/" + texture + "\"");
		builder.append("}");
		builder.append("}");

		try {
			FileUtils.writeStringToFile(f, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





}
