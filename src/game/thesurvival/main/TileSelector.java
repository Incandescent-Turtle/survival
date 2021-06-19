package game.thesurvival.main;

import java.awt.Point;

import game.thesurvival.main.handlers.MouseHandler;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.Camera;

public class TileSelector 
{
	private final Camera camera;
	private int tileX, tileY;
	
	public TileSelector(Game game)
	{
		camera = game.getCamera();
		tileX = (int) ((MouseHandler.mouseX + camera.getXOffset()) / Tiles.TILE_SIZE);
		tileY = (int) ((MouseHandler.mouseY + camera.getYOffset()) / Tiles.TILE_SIZE);
	}
	
	public void tick()
	{
		tileX = (int) ((MouseHandler.mouseX + camera.getXOffset()) / Tiles.TILE_SIZE);
		tileY = (int) ((MouseHandler.mouseY + camera.getYOffset()) / Tiles.TILE_SIZE);
	}
	
	public Point getTileLocation()
	{
		return new Point(tileX, tileY);
	}
	
	public Point getScreenLocation()
	{
		return new Point(tileX * Tiles.TILE_SIZE - (int) camera.getXOffset(), tileY * Tiles.TILE_SIZE - (int) camera.getYOffset());
	}
}