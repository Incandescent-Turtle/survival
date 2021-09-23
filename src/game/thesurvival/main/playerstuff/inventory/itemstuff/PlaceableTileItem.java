package game.thesurvival.main.playerstuff.inventory.itemstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import game.thesurvival.main.Game;
import game.thesurvival.main.TileSelector;
import game.thesurvival.main.Util;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.tilestuff.Tile;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;
//an item that when used becomes a tile
public class PlaceableTileItem extends Item
{
	//the tile it places downn
	private final Tile tile;
	
	public PlaceableTileItem(Image itemTexture, Tile tile, ItemInfo info, int keyBinding) 
	{
		super(itemTexture, info, keyBinding);
		this.tile = tile;
	}

	@Override
	public boolean useItem(Game game) 
	{			
		World world = game.getWorld();
		TileSelector selector = world.getTileSelector();
		//if can be placed, places the tile
		if(canBePlacedOn(game, selector.getTileLocation()))
		{
			world.setTile(selector.getTileLocation(), tile);
			return true;
		}
		return false;
	}
	
	@Override
	public void render(Game game, Graphics2D g)
	{
		Point tilePos = game.getWorld().getTileSelector().getTileLocation();
		//draws the tile image at an opacity
		Util.drawTranslucentImage(game, g, tile.getTexture(), tilePos, 50);
		//if the tile you're trying to place a new tile on isnt allowed to be placed on
		if(!canBePlacedOn(game, tilePos))
		{
			Point screenPos = game.getWorld().getTileSelector().getScreenLocation();
			//overlays the tile img with red
			g.setColor(new Color(255, 0, 0, 25));
			g.fillRect(screenPos.x, screenPos.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		}
	}
	
	//whether the tile is allowed to have tiles palced on itself
	private boolean canBePlacedOn(Game game, Point tilePos)
	{
		//making sure no objects are on the tiles you're trying to place
		boolean objectOccupyingTile = false;
		for(GameObject obj : game.getObjectHandler().getObjects())
		{
			if(Util.objectTouchingTilePos(game, obj, tilePos)) 
			{
				objectOccupyingTile = true;
				break;
			}
		}
		return !objectOccupyingTile && Util.posWithinPlayerAOE(game, tilePos, 3) && game.getWorld().getTile(tilePos).canBePlacedOn();
	}
}