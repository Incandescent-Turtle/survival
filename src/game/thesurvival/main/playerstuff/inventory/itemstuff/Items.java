package game.thesurvival.main.playerstuff.inventory.itemstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.tilestuff.Tile;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;

// "static" class
//Fruit items are created in their FruitType
public class Items 
{
	public static ArrayList<Item> ITEM_LIST = new ArrayList<>();
	public static HashMap<Item, Integer> ITEM_KEYBINDINGS = new HashMap<>();
	//wood
	public static final Item WOOD = new PlaceableTileItem(Textures.WOOD_ITEM, Tiles.PLANKS, new ItemInfo("Wood", "Wood", "As monsters come from all "
			+ "angles, you must seek shelter fast. Use wood to to build a base to keep you safe! \nWarning: The more you "
			+ "build the faster the monsters come to get you", "Used to build", "Can be harvested from trees "
					+ "that spawn throughout the map"), KeyEvent.VK_P);
	
	//rock
	public static final Item ROCK = new PlaceableTileItem(Textures.ROCK_ITEM, Tiles.PLACED_ROCK, new ItemInfo("Rock", "Rocks", "As monsters come from all "
			+ "angles, you must seek shelter fast. Use rocks to to build a base to keep you safe! \nWarning: The more you "
			+ "build the faster the monsters come to get you", "Used to build", "Can be mined from the rock structures "
					+ "that spawn throughout the map"), KeyEvent.VK_T);
	
	//rock
	public static final Item DOOR = new PlaceableTileItem(Textures.DOOR_ITEM, Tiles.CLOSED_DOOR, new ItemInfo("Door", "Doors", 
			"As monsters come from all angles, you must seek shelter fast. Use doors to help you get in and out of your base quickly",
			"Used to build. Can be interacted with using the player's hands", "Can be crafted in the inventory"), KeyEvent.VK_C);
		
	//for harvesting
	public static final Item MULTI_TOOL = new HarvestTool();

	public static final Item HANDS = new Item(Textures.HANDS, new ItemInfo("Hands", "You were born with these...", "Used to interect with "
			+ "doors"), KeyEvent.VK_R) {
		@Override
		public boolean useItem(Game game) 
		{
			if(ticksSinceLastUsed > 60)
			{
				World world = game.getWorld();
				Point mouseTilePos = game.getWorld().getTileSelector().getTileLocation();
				Tile selectedTile = world.getTile(mouseTilePos);
				if(canInteract(game, mouseTilePos))
				{
					ticksSinceLastUsed = 0;
					world.setTile(mouseTilePos, selectedTile == Tiles.OPEN_DOOR ? Tiles.CLOSED_DOOR : Tiles.OPEN_DOOR);
				}
			}
			return false;
		}
		
		@Override
		public void render(Game game, Graphics2D g)
		{

			World world = game.getWorld();
			Point screenPos = world.getTileSelector().getScreenLocation();
			
			g.setColor(canInteract(game,  world.getTileSelector().getTileLocation()) ? new Color(255, 255, 255, 40) : new Color(255, 0, 0, 40));
			g.fillRect(screenPos.x, screenPos.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		}
		
		private boolean canInteract(Game game, Point mouseTilePos)
		{
			Tile selectedTile = game.getWorld().getTile(mouseTilePos);
			boolean objectsClear = true;
			for(GameObject obj : game.getObjectHandler().getObjects())
			{
				if(Util.objectTouchingTilePos(game, obj, mouseTilePos)) 
				{
					objectsClear = false;
					break;
				}
			}
			return objectsClear && Util.getSquareTilePositions(game, game.getPlayer().getTilePosition(), 3).contains(mouseTilePos) && (selectedTile == Tiles.OPEN_DOOR || selectedTile == Tiles.CLOSED_DOOR);
		}
	};
	
	public static final Item SCANNER = new Scanner();
}