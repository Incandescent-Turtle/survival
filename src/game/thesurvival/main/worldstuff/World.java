package game.thesurvival.main.worldstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.TileSelector;
import game.thesurvival.main.Util;
import game.thesurvival.main.handlers.GameObjectHandler;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Item;
import game.thesurvival.main.tilestuff.StructureSpawnTile;
import game.thesurvival.main.tilestuff.Tile;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
import game.thesurvival.main.worldstuff.structures.Structures;

public class World 
{
	//world array. Tile index
	private int[][] tiles;
	//world size
	public static int width, height;
	private final Game game;
	//all the structures in the game. Water, rocks, trees
	private final CopyOnWriteArrayList<MaterialStructure> structures = new CopyOnWriteArrayList<>();
	//all the origin tiles for structures found when constructing world
	private final ArrayList<Point> foundStructureSpawnPoints = new ArrayList<>();
	//each array is for a certain strucure. The array contains the tiles that the structure can be made up of
	public static final ArrayList<ArrayList<Tile>> ALLOWED_STRUCTURE_TILES = new ArrayList<>();
	//all the leaves in the world - to render ontop of the player
	private final ArrayList<Point> leaves = new ArrayList<>();
	//handler
	private final GameObjectHandler handler;
	private final Camera camera;
	//to select tiles for interacting
	private final TileSelector tileSelector;

	public World(Game game) 
	{
		this.game = game;
		this.handler = game.getObjectHandler();
		this.camera = game.getCamera();
		tileSelector = new TileSelector(game);
	}

	//gets the color of a given pixel
	public Color getColor(int pixel) 
	{
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    return new Color(red, green, blue);
	}

	//sets up the world array according to world file passed in
	public void loadWorld(BufferedImage image)
	{
		//world dimensions
		// + 2 for borders
		width = image.getWidth() + 2;
		height = image.getHeight() + 2;
		//initilizing array 
		tiles = new int[width][height];
		//adding tiles to the world array based on their colour
		//makes borders rocks and the rest corropond to the world png file
		for(int x = 0; x < width; x++) 
			for(int y = 0; y < height; y++)
			{
				//position of the current tile
				Point tilePos = new Point(x, y);
				//if on edge sets it to stone, if else it creates something based on the image file, 
				tiles[x][y] = isOnEdge(tilePos) ? Tiles.STONE.getId() : Tiles.getTile(image.getRGB(x-1, y-1)).getId();
				Tile tile = getTile(tilePos);
				if(tile == Tiles.LEAF) leaves.add(tilePos);
				if(!isOnEdge(tilePos) &&  Structures.STRUCTURE_ALLOWED_TILES.contains(tile) && tile instanceof StructureSpawnTile) foundStructureSpawnPoints.add(tilePos);
			}
		
		//generating structures
		generateStructures();
	}
	
	private void generateStructures()
	{
		for(Point pos : foundStructureSpawnPoints)
		{
			boolean exists = false;
			for(MaterialStructure stucture : structures)
			{
				if(stucture.getPoses().contains(pos)) exists = true;
			}
			
			if(!exists) 
			{
				Tile tile = getTile(pos);
				for(ArrayList<Tile> acceptableTiles : ALLOWED_STRUCTURE_TILES)
				{
					if(acceptableTiles.contains(tile))
					{ 
						structures.add(((StructureSpawnTile) tile).newStructure(game, getConnectedTiles(new ArrayList<Point>(), pos, acceptableTiles)));
						break;
					}		
				}
			}
		}	
	}
	
	private ArrayList<Point> getConnectedTiles(ArrayList<Point> connectedTiles, Point origin, ArrayList<Tile> acceptableTiles)
	{
		if(!connectedTiles.contains(origin))
		{
			connectedTiles.add(origin);
			for(Point pos : Util.getAdjacentTilePoses(origin))
			{
				if(!connectedTiles.contains(pos) && !isOnEdge(pos) && acceptableTiles.contains(Tiles.TILES[this.tiles[pos.x][pos.y]]))
					getConnectedTiles(connectedTiles, pos, acceptableTiles);	
			}				
		}
		return connectedTiles;
	}
	
	public void tick()
	{
		tileSelector.tick();
		for(MaterialStructure structure : structures)
		{
			structure.tick();
		}
		handler.tick();

		//ticks tiles for animation purposes
		for(Tile tile : Tiles.TILES) tile.tick();
		Item currentItem = game.getPlayer().getInventory().getCurrentItem();
		if(currentItem != null) currentItem.tick(game);
	}
	
	//renders all tiles
	public void render(Graphics2D g)
	{ 
		//for ease
		float xOffset = game.getCamera().getXOffset();
		float yOffset = game.getCamera().getYOffset();
		
		//closest tile to the left
		int minX = Math.max(0, (int) (xOffset / Tiles.TILE_SIZE));
		//closest tile to the right
		int maxX = Math.min(width, (int) ((xOffset + game.getWidth()) / Tiles.TILE_SIZE) + 1);
		//closest tile to the top
		int minY = Math.max(0, (int) (yOffset / Tiles.TILE_SIZE));
		//closest tile to the bottom
		int maxY = Math.min(height, (int) ((yOffset+ game.getHeight()) / Tiles.TILE_SIZE) + 1);

		//uses above variables to only render what is on the screen
		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
				if(getTile(x, y) != Tiles.LEAF) getTile(x, y).render(g, game.isInHighDetial(), (int) (x*Tiles.TILE_SIZE - xOffset), (int) (y*Tiles.TILE_SIZE - yOffset));
		
		handler.render(g);

		//renders all the leaves on the map over the player
		for(Point leaf : leaves)
		{
			if(camera.isOnScreen(leaf));
				Tiles.LEAF.render(g, game.isInHighDetial(), (int) (leaf.x * Tiles.TILE_SIZE - xOffset), (int) (leaf.y * Tiles.TILE_SIZE - yOffset));
		}
				
		//rendering the structures
		for(MaterialStructure structure : structures)
		{
			structure.render(g);
		}
		Item currentItem = game.getPlayer().getInventory().getCurrentItem();
		if(currentItem != null) currentItem.render(game, g);
	}
		
	//to get the tile at specified pos
	public Tile getTile(Point pos)
	{
		return getTile(pos.x, pos.y);
	}
	
	//to get the tile at specified pos
	public Tile getTile(int x, int y)
	{
		return Tiles.TILES[tiles[x][y]];
	}
	
	//whether the given tile pos is the rock on the edge of the map
	private boolean isOnEdge(Point tilePos)
	{
		return tilePos.x == 0 || tilePos.x == width-1 || tilePos.y == 0 || tilePos.y == height-1;
	}
	
	//tile and pos is walkable or not
	public boolean canBeWalkedOn(int x, int y)
	{
		if(!isWithinWorld(new Point(x, y))) return false;
		return Tiles.TILES[tiles[x][y]].isWalkable();
	}
	
	//tile and pos is walkable or not
	public boolean canBeWalkedOn(Point tilePos)
	{
		return canBeWalkedOn(tilePos.x, tilePos.y);
	}
	
	public void reset()
	{
		for(MaterialStructure structure : structures)
		{
			structure.reset();
		}
	}
	
	public boolean isWithinWorld(Point tilePos)
	{
		return tilePos.x >= 0 && tilePos.x < width && tilePos.y >= 0 && tilePos.y < height;
	}
	
	//in tile pos
	public void setTile(Point pos, Tile tile)
	{
		tiles[pos.x][pos.y] = tile.getId();
	}
	
	/*
	 * Getters
	 */
	
	public CopyOnWriteArrayList<MaterialStructure> getStructures()
	{
		return structures;
	}
	
	public int getMapHeight()
	{
		return height;
	}
	
	public int getMapWidth()
	{
		return width;
	}
	
	public ArrayList<Point> getLeaves()
	{
		return leaves;
	}
	
	public TileSelector getTileSelector()
	{
		return tileSelector;
	}
}