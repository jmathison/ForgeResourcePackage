package com.idtech.block;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.idtech.BaseMod;
import com.idtech.JSONManager;
import com.idtech.PackageManager;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class QuickBlock extends Block {
	

	public static enum BlockType {
		CUBE_ALL, HALF_SLAB;
	}
	
	protected static final String PICKAXE = "pickaxe";
	protected static final String SHOVEL = "shovel";
	
	protected static final int WOOD = 0;
	protected static final int STONE = 0;
	protected static final int IRON = 0;
	protected static final int DIAMOND = 0;



	private static final Material defaultMaterial = Material.GROUND;
	private static Set<QuickBlock> registry = new HashSet<QuickBlock>();
	private static Map<String, QuickBlock> lookup = new HashMap<String, QuickBlock>();

	protected String name = "Undefined";
	protected BlockType type = BlockType.CUBE_ALL;
	protected CreativeTabs tab = CreativeTabs.MISC;
	protected String texture = "undefined";
	protected Material material = defaultMaterial;
	protected Item itemDropped = null;
	
	protected World world;
	protected BlockPos pos;
	protected IBlockState state;
	protected Entity entity;
	protected Random random;
	protected double tickSpeed;

	// When QuickBlock is loaded, it will scan the block directory for all sub
	// classes of QuickBlock
	// and create a newInstance of them. This allows us to simply use the
	// initialization block to specify each parameter
	// This invokes the default super constructor of QuickBlock and registers
	// it. Therefore, you should almost never
	// need to call the constructor for QuickBlock manually.
	static {
		// Loads all of the QuickBlocks that are in this package
		Set<Class> classes = PackageManager.loadClassesInPackage("com.idtech.block");
		for (Class klass : classes) {
			if (QuickBlock.class.isAssignableFrom(klass) && QuickBlock.class != klass) {
				try {
					QuickBlock block = (QuickBlock)klass.newInstance();
					lookup.put(block.name, block);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static QuickBlock getBlock(String name){
		return lookup.get(name);
	}

	/**
	 * Returns an immutable view of the Registry
	 * 
	 * @return
	 */
	public static Set<QuickBlock> getRegistry() {
		return Collections.unmodifiableSet(registry);
	}

	/**
	 * Creates a new QuickBlock and adds it to the registry. (Note: You probably
	 * shouldn't be calling this directly)
	 */
	public QuickBlock() {
		super(defaultMaterial);
		this.registry.add(this);
		
	}
	

	/**
	 * This method should be called once prior to the init method.
	 */
	public static void preInit() {
		System.out.println("Quick Block Registry Size: " + registry.size());
		// Loops through all registered QuickBlocks and initializes them
		for (QuickBlock block : registry) {
			block.setUnlocalizedName(BaseMod.MODID + "_" + JSONManager.safeString(block.name));
			block.setCreativeTab(block.tab);
			System.out.println("preInit: " + block.name);

			// Assigns the block material using reflection since we are avoiding
			// using constructors
			setBlockField(block, block.material, "blockMaterial");
			setBlockField(block, block.material.getMaterialMapColor(), "blockMapColor");
			setBlockField(block, !block.material.blocksLight(), "translucent");

			// Register the block and an item for the block.
			//GameRegistry.register(block.setRegistryName(block.name));
			//GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));

		}
	}

	/**
	 * Private helper method which allows the change of private member variables
	 * in the parent class
	 * 
	 * @param block
	 *            the QuickBlock we want to change
	 * @param value
	 *            the value we want to be assigned
	 * @param fieldName
	 *            the field we want to assign
	 */
	private static void setBlockField(QuickBlock block, Object value, String fieldName) {
		try {
			Field f = Block.class.getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(block, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method should be run once before the mod launches
	 */
	public static void init() {
		for (QuickBlock block : registry) {
			System.out.println("init: " + block.name);
			BaseMod.proxy.registerBlockInventoryRender(block, block.name);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return itemDropped;
	}
	
	protected void onRandomTick(){}
	
	
	
	/**
	 * Returns a random neighboring block position.
	 * @return a random neighboring block position.
	 */
	protected BlockPos findNeighborBlock(){
		int spreadX = random.nextInt(3) - 1;
		int spreadY = spreadX == 0 ? random.nextInt(3) - 1 : 0 ;
		int spreadZ = spreadX == 0  && spreadY == 0 ? random.nextInt(3) - 1 : 0;
		BlockPos spreadPos = pos.add(spreadX, spreadY, spreadZ);
		return spreadPos;
	}
	
	protected void cloneAt(BlockPos pos){
		world.setBlockState(pos, this.getDefaultState());
	}
	
	
	@Override
	public void randomTick(World worldIn, BlockPos posIn, IBlockState stateIn, Random randomIn) {
		world = worldIn;
		pos = posIn;
		state = stateIn;
		random = randomIn;
		onRandomTick();
	}
	
	protected void onEntityWalk(){}
	
	
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos posIn, Entity entityIn) {
		world = worldIn;
		pos = posIn;
		entity = entityIn;
		onEntityWalk();
		super.onEntityWalk(worldIn, pos, entityIn);
	}
	 
	
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB box = super.getCollisionBoundingBox(blockState, worldIn, pos);
		AxisAlignedBB slightlySmaller = new AxisAlignedBB(box.minX + .1, box.minY + .1, box.minZ + .1, box.maxX - .1, box.maxY - .1, box.maxZ - .1);
		return slightlySmaller;
	}
	
	

	/**
	 * Generates a sound at the entities current position. Each call to this
	 * method generates a random pitch applied to the audio.
	 * 
	 * @param sound
	 *            The sound to generate
	 */
	protected void playSound(SoundEvent sound) {
		if(entity == null) return;
		world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, sound, SoundCategory.NEUTRAL, 0.5f,
				0.4F / ((float)Math.random() * 0.4F + 0.8F));
	}
	
	protected void onEntityCollidedWithBlock(){
		
	}
	
	public QuickBlock getInstance(){
		return this;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		this.world = worldIn;
		this.pos = pos;
		this.state = state;
		this.entity = entityIn;
		onEntityCollidedWithBlock();
	}

	
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.world = worldIn;
		int nextTick = tickSpeed > 0 ? (int)(tickSpeed*20) : 20;
		world.scheduleBlockUpdate(pos, this, nextTick, 1);
	}

	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		this.world = worldIn;
		this.state = state;
		this.random = rand;
		this.pos = pos;		
		tick();
		int nextTick = tickSpeed > 0 ? (int)(tickSpeed*20) : 20;
		world.scheduleBlockUpdate(pos, this, nextTick, 1);		
	}
	
	protected void tick(){
		
	}
	
	protected void spawnItem(Item toSpawn){
		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY()+1, pos.getZ(), new ItemStack(toSpawn)));
		
	}
	
	protected void spawnParticles(EnumParticleTypes type, int intensity){
		for (int i = 0; i < intensity; i++) {
			double x = pos.getX();
			double y = pos.getY();
			double z = pos.getZ();
			world.setBlockState(new BlockPos(x, y, z), state);
			System.out.println(x + " , " + y + ", " + z);
			world.spawnParticle(type, x, y, z, 0, 0, 0);
		}
	}

	/**
	 * This method creates the JSON Files necessary for the Minecraft mod. If
	 * you want to use your own custom JSON files @Override this method.
	 */
	public void createJSONFiles() {
		createModelJSON();
		createBlockStatesJSON();
		createItemJSON();
	}

	// Code below this point is used to generate JSON

	private final void createModelJSON() {
		File f = Paths.get(".").resolve(JSONManager.assetsDir + "/models/block/" + JSONManager.jsonName(name) + ".json")
				.toFile();

		if (f.exists()) {
			f.delete();
		}

		StringBuilder builder = new StringBuilder();

		builder.append("{");
		builder.append("\"parent\": \"block/" + type + "\",");
		
		builder.append("\"textures\": {");
		// TODO: Need to figure out how to apply textures properly for various
		// types
		builder.append(" \"all\": \"" + BaseMod.MODID + ":blocks/" + texture + "\"");
		builder.append("}");
		builder.append("}");

		try {
			FileUtils.writeStringToFile(f, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final void createBlockStatesJSON() {
		File f = Paths.get(".").resolve(JSONManager.assetsDir + "/blockStates/" + JSONManager.jsonName(name) + ".json")
				.toFile();

		if (f.exists()) {
			f.delete();
		}

		StringBuilder builder = new StringBuilder();

		builder.append("{");
		builder.append("\"variants\": {");
		builder.append("\"normal\": {");
		builder.append("\"model\": \"" + BaseMod.MODID + ":" + name + "\"");
		builder.append("}");
		builder.append("}");
		builder.append("}");

		try {
			FileUtils.writeStringToFile(f, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final void createItemJSON() {
		File f = Paths.get(".").resolve(JSONManager.assetsDir + "/models/item/" + JSONManager.jsonName(name) + ".json")
				.toFile();

		if (f.exists()) {
			f.delete();
		}

		StringBuilder builder = new StringBuilder();

		builder.append("{");
		builder.append("\"parent\": \"" + BaseMod.MODID + ":block/" + name + "\"");
		builder.append("}");

		try {
			FileUtils.writeStringToFile(f, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
}
