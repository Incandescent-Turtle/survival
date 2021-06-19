package game.thesurvival.main.tilestuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
import game.thesurvival.main.worldstuff.structures.RockStructure;
import game.thesurvival.main.worldstuff.structures.Structures;
import game.thesurvival.main.worldstuff.structures.TreeStructure;

//"static" class to hold stuff
public class Tiles 
{
	private Tiles() {}
	//tile types
	public static final Tile[] TILES; 	
	/*
	 * Tiles
	 */
	public static final Tile DIRT, GRASS, STONE;
	
	//Tree
	public static final Tile STUMP, LEAF;
	
	//Rock
	public static final Tile ROCK;

	//Water
	public static final Tile WATER;
	
	//the size of all tiles
	public static final int TILE_SIZE;
	
	//Mineable
	public static final Tile PLANKS, PLACED_ROCK, OPEN_DOOR, CLOSED_DOOR;

	
	//static
	static
	{
		//setting tile size
		TILE_SIZE = 32;
		
		//setting array size
		TILES = new Tile[] {
			
			STONE = new Tile(Textures.STONE_TILE, new Color(169, 169, 169)).setWalkable(false),
			
			GRASS = new Tile(Textures.GRASS_TILE, new Color(0, 255, 0)).setCanBePlacedOn(true),
		
			DIRT = new Tile(Textures.DIRT_TILE, new Color(111, 42, 42))
			{
				@Override
				public void renderTile(Graphics2D g, int x, int y) 
				{
					g.setColor(Util.modifyColour(color, -20, -20, -20));
					g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
					g.setColor(color);
					g.fillRect(x+1, y+1, TILE_SIZE-1, TILE_SIZE-1);
				}
			}.setCanBePlacedOn(true),
			
			ROCK = new StructureSpawnTile(Textures.ROCK_TILE, Structures.ROCK_STRUCTURE_ALLOWED_TILES, new Color(112, 112, 112)) {
				
				@Override
				public MaterialStructure newStructure(Game game, ArrayList<Point> poses) 
				{
					return new RockStructure(game, poses);
				}
			}.setWalkable(false),
			
			STUMP = new StructureSpawnTile(Textures.STUMP_TILE, Structures.TREE_STRUCTURE_ALLOWED_TILES, new Color(75, 0, 0))
			{
				@Override
				public MaterialStructure newStructure(Game game, ArrayList<Point> poses) 
				{
					return new TreeStructure(game, poses);
				}
			}.setWalkable(false),
			
			LEAF = new Tile(Textures.LEAF_TILE, Structures.TREE_STRUCTURE_ALLOWED_TILES, new Color(0, 119, 0)),
			
			WATER = new WaterTile(),

			PLANKS = new Tile(Textures.PLANKS_TILE, new Color(200, 160, 0)).setWalkable(false).setMineable(true),
			
			PLACED_ROCK = new Tile(Textures.PLACED_ROCK_TILE, new Color(64, 64, 64)).setWalkable(false).setMineable(true),
			
			OPEN_DOOR = new Tile(Textures.OPEN_DOOR_TILE, new Color(255, 100, 0)).setMineable(true),
			
			CLOSED_DOOR = new Tile(Textures.CLOSED_DOOR_TILE, new Color(255, 0, 0)).setMineable(true).setWalkable(false)
		};
		
		for(int i = 0; i < TILES.length; i++)
		{
			TILES[i].setId(i);
		}
	}
	
	//get tile from the color (used for map construction)
	public static Tile getTile(int rgb)
	{
		Tile tileToBeReturned = null;
		for(Tile tile : TILES)
		{
			if((new Color(rgb).equals(tile.getColor()))) tileToBeReturned = tile;
		}
		return tileToBeReturned == null ? Tiles.DIRT : tileToBeReturned;
	}
}