package game.thesurvival.main.tilestuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import game.thesurvival.main.worldstuff.structures.Structures;

//Game tile
public class Tile 
{	
	//texture of the tile (can be transparent)
	protected Image texture;
	//whether the tile can be walked over, or should have collisions
	private boolean walkable = true;
	//whether other tiles can be placed upon this block (grass dirt - true, water stone - false)
	private boolean canBePlacedOn = false;
	//the "id", used for reference
	private int id;
	//color the tile is in the player png maps
	protected final Color color;
	//whether the texture is just an overlay, and the tile should be drawn normally, or just the graphics
	private boolean textureIsOverlay = false;
	//whether the tile can be mined (removes from world)
	private boolean isMineable = false;
		
	public Tile(Image texture, Color color)
	{
		this(texture, null, color);
	}
	
	public Tile(Image texture, ArrayList<Tile> structureTileArray, Color color)
	{
		this.texture = texture;
		this.color = color;	
		if(structureTileArray != null)
		{
			structureTileArray.add(this);
			Structures.STRUCTURE_ALLOWED_TILES.add(this);
		}
	}
	
	//called every world render
	public final void render(Graphics2D g, boolean highDef, int x, int y)
	{
		//if not in high def renders the tile
		if(!highDef || (textureIsOverlay && highDef)) renderTile(g, x, y);
		//if in high def renders img
		if(highDef || (textureIsOverlay && highDef)) g.drawImage(texture, x, y, Tiles.TILE_SIZE, Tiles.TILE_SIZE, null);
	}
	
	//renders the tile in low def mode
	protected void renderTile(Graphics2D g, int x, int y)
	{
		g.setColor(color);
		g.fillRect(x, y, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
	}
	
	public void tick(){}
	
	//whether collisions take place or not
	public final boolean isWalkable()
	{
		return walkable;
	}
	
	//to set whether collisions take place or not
	public final Tile setWalkable(boolean bool)
	{
		walkable = bool;
		return this;
	}
	
	//whether collisions take place or not
	public final boolean canBePlacedOn()
	{
		return canBePlacedOn;
	}
	
	//to set whether collisions take place or not
	public final Tile setCanBePlacedOn(boolean bool)
	{
		canBePlacedOn = bool;
		return this;
	}
	
	//whether the player can break this block
	public final boolean isMineable()
	{
		return isMineable;
	}
	
	//to set whether the player can break this block
	public final Tile setMineable(boolean bool)
	{
		isMineable = bool;
		return this;
	}
	
	//to set whether the texture should be rendered
	public final Tile setTextureIsOverlay(boolean bool)
	{
		textureIsOverlay = bool;
		return this;
	}
	
	/*
	 * Getters
	 */
	public final Image getTexture()
	{
		return texture;
	}
	
	public final int getId()
	{
		return id;
	}
	
	public final Color getColor()
	{
		return color;
	}
	
	protected final void setId(int id)
	{
		this.id = id;
	}
}