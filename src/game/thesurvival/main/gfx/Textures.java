package game.thesurvival.main.gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;

import game.thesurvival.main.playerstuff.inventory.Inventory;
import game.thesurvival.main.tilestuff.Tiles;

//general game textures
public class Textures 
{
	private Textures() {}
	//image loader to load the images
	private static final ImageLoader loader;
	//tiles
	public static final Image DIRT_TILE, GRASS_TILE, STONE_TILE, ROCK_TILE, STUMP_TILE, LEAF_TILE, PLANKS_TILE, PLACED_ROCK_TILE,
	OPEN_DOOR_TILE, CLOSED_DOOR_TILE;
	//fruit
	public static final Image ORANGE_FRUIT, APPLE_FRUIT, PEACH_FRUIT, BANANA_FRUIT, CHERRY_FRUIT, PEAR_FRUIT, LEMON_FRUIT;
	//the world image
	public static final BufferedImage WORLD;
	//animated tile
	public static final Image WATER_TILE;
	//items
	public static final Image ROCK_ITEM, WOOD_ITEM, APPLE_ITEM, ORANGE_ITEM, PEACH_ITEM, BANANA_ITEM, CHERRY_ITEM, PEAR_ITEM, 
	LEMON_ITEM, MULTI_TOOL, HANDS, DOOR_ITEM, SCANNER_ITEM;
	
	public static final Image SCANNER_SCREEN;
	
	public static final Image MONSTER;
	//loading all images
	static 
	{
		loader = new ImageLoader();
		WORLD = loader.loadImage("world");
		MONSTER = loadTileSizedImage("monster");

		/*
		 * Tiles
		 */
		DIRT_TILE = loadTileImage("dirt");
		GRASS_TILE = loadTileImage("grass");
		STONE_TILE = loadTileImage("stone");
		ROCK_TILE = loadTileImage("rock");
		STUMP_TILE = loadTileImage("stump");
		LEAF_TILE = loadTileImage("leaf");
		PLANKS_TILE = loadTileImage("planks");
		PLACED_ROCK_TILE = loadTileImage("placed rock");
		OPEN_DOOR_TILE = loadTileImage("open door");
		CLOSED_DOOR_TILE = loadTileImage("closed door");
		
		//water is an animated tile
		WATER_TILE = loadTileImage("water").getScaledInstance(Tiles.TILE_SIZE*2, Tiles.TILE_SIZE, 0);
				
		/*
		 * Fruit
		 */
		ORANGE_FRUIT = loadFoodImage("orange");
		APPLE_FRUIT = loadFoodImage("apple");
		PEACH_FRUIT = loadFoodImage("peach");
		BANANA_FRUIT = loadFoodImage("banana");
		CHERRY_FRUIT = loadFoodImage("cherry");
		PEAR_FRUIT = loadFoodImage("pear");
		LEMON_FRUIT = loadFoodImage("lemon");

		/*
		 * Items
		 */
		ROCK_ITEM = loadItemImage("rock");
		WOOD_ITEM = loadItemImage("wood");
		PEACH_ITEM = loadFoodItemImage("peach");
		ORANGE_ITEM = loadFoodItemImage("orange");
		APPLE_ITEM = loadFoodItemImage("apple");
		BANANA_ITEM = loadFoodItemImage("banana");		
		CHERRY_ITEM = loadFoodItemImage("cherry");	
		PEAR_ITEM = loadFoodItemImage("pear");	
		LEMON_ITEM = loadFoodItemImage("lemon");
		MULTI_TOOL = loadItemImage("multi tool");
		HANDS = loadItemImage("hands");
		SCANNER_ITEM = loadItemImage("scanner");
		DOOR_ITEM = loader.loadImage("tiles/closed_door").getScaledInstance(Inventory.ITEM_SIZE, Inventory.ITEM_SIZE, 0);
	
		SCANNER_SCREEN = loader.loadImage("scanner screen");
	}
	
	/*
	 * Helpers
	 */
	
	private static Image loadTileImage(String path)
	{
		return loadTileSizedImage("tiles/" + path);
	}	
	
	private static Image loadFoodImage(String path)
	{
		return loadTileSizedImage("food/" + path);
	}	
	
	private static Image loadFoodItemImage(String path)
	{
		return loader.loadImage("food/" + path).getScaledInstance(Inventory.ITEM_SIZE, Inventory.ITEM_SIZE, 0);
	}
	
	private static Image loadItemImage(String path)
	{
		return loader.loadImage("items/" + path).getScaledInstance(Inventory.ITEM_SIZE, Inventory.ITEM_SIZE, 0);
	}
	
	public static Image loadTileSizedImage(String path)
	{
		try {
			return loader.loadImage(path).getScaledInstance(Tiles.TILE_SIZE, Tiles.TILE_SIZE, 0);
		} catch (Exception e) {
			System.out.println("Error! Couldn't find texture at location " + "res/" + path + ".png" + " Ensure that the png file is named correctly and is placed within the correct folder.");
		}
		return null;
	}	
}