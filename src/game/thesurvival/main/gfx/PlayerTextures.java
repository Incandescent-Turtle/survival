package game.thesurvival.main.gfx;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import game.thesurvival.main.Util;
import game.thesurvival.main.tilestuff.Tiles;

//for all the player textures and animations
public class PlayerTextures
{
	private static final ImageLoader loader;
	
	/*
	 * Animations
	 */
	public static final BufferedImage[] KNIFE_MELEE_ANIMATION = new BufferedImage[15];
	public static final BufferedImage[] FLASHLIGHT_MELEE_ANIMATION = new BufferedImage[14];
	public static final BufferedImage[] FISTS_ANIMATION = new BufferedImage[19];
	public static final BufferedImage[] FEET_ANIMATION = new BufferedImage[20];
	public static final Image INVENTORY_BACKGROUND;
	public static final Image INVENTORY_ITEM_AREA;
	public static final Image INVENTORY_TRIANGLE;
	public static final Image INVENTORY_SELECTED_TRIANGLE;
	public static final Image INVENTORY_CRAFTING_WINDOW;
	

	//storing all the correct images in the animation arrays
	static
	{
		loader = new ImageLoader();
		INVENTORY_BACKGROUND = loader.loadImage("player/inventory/inventory background");
		INVENTORY_TRIANGLE = loader.loadImage("player/inventory/triangle").getScaledInstance(32, 32, 0);
		INVENTORY_SELECTED_TRIANGLE = loader.loadImage("player/inventory/highlighted_triangle").getScaledInstance(32, 32, 0);
		INVENTORY_ITEM_AREA = loader.loadImage("player/inventory/item area");
		INVENTORY_CRAFTING_WINDOW = loader.loadImage("player/inventory/crafting window");

		
		for(int i = 0; i < 15; i++)
			KNIFE_MELEE_ANIMATION[i] = resizeImage("player/melee/knife/survivor-meleeattack_knife_" + i, new Dimension(Tiles.TILE_SIZE + 20, Tiles.TILE_SIZE + 20));
	
		for(int i = 0; i < 14; i++)
			FLASHLIGHT_MELEE_ANIMATION[i] = resizeImage("player/melee/flashlight/survivor-meleeattack_flashlight_" + i, new Dimension(Tiles.TILE_SIZE + 20, Tiles.TILE_SIZE + 20));
		
		for(int i = 0; i < 19; i++)
			FISTS_ANIMATION[i] = resizeImage("player/movement/fists/survivor-move_fists_" + i, new Dimension(Tiles.TILE_SIZE + 10, Tiles.TILE_SIZE + 10));

		for(int i = 0; i < 20; i++)
			FEET_ANIMATION[i] = resizeImage("player/movement/feet/survivor-run_" + i, new Dimension(Tiles.TILE_SIZE, Tiles.TILE_SIZE));
		}
	
	//helper to resize the images
	private static BufferedImage resizeImage(String path, Dimension dim)
	{
		return Util.imageToBufferedImage(loader.loadImage(path).getScaledInstance(dim.width, dim.height, 0));
	}
}