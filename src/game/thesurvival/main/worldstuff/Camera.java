package game.thesurvival.main.worldstuff;

import java.awt.Point;
import java.awt.geom.Point2D;

import game.thesurvival.main.Game;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.tilestuff.Tiles;

//Game Camera. Used for scrolling effect
public class Camera 
{
	//used for scroll effect. scrollX, scrollY
	private float xOffset, yOffset;
	private Game game;
	
	public Camera(Game game)
	{
		this.game = game;
	}
	
	//center the game screen around one object (player or drone usually)
	public void centerObject(GameObject obj)
	{
		xOffset = obj.getPos().x - game.getWidth()/2;
		yOffset = obj.getPos().y - game.getHeight()/2;
		//checks to see whether or not the game is at the edge of the world (stops scrolling)
		checkMapBounds();
	}
	
	//checks to see whether or not the game is at the edge of the world (stops scrolling)
	private void checkMapBounds()
	{
		//at the top of the map
		if(yOffset < 0) yOffset = 0;
		//left side of the map
		if(xOffset < 0 ) xOffset = 0;
		//the offset value needed to reach the bound
		int xMax = game.getWorld().getMapWidth()*Tiles.TILE_SIZE - game.getWidth();
		//right side of the map
		if(xOffset > xMax) xOffset = xMax;
		//the offset value needed to reach the bound
		int yMax = game.getWorld().getMapHeight()*Tiles.TILE_SIZE - game.getHeight();
		//left side of the map
		if(yOffset > yMax) yOffset = yMax;
	}
	
	//move camera in the x direction
	public void moveX(int amount)
	{
		xOffset += amount;
		//checks to see whether or not the game is at the edge of the world (stops scrolling)
		checkMapBounds();
	}
	
	//move camera in the y direction
	public void moveY(int amount)
	{
		yOffset += amount;
		//checks to see whether or not the game is at the edge of the world (stops scrolling)
		checkMapBounds();
	}
	
	//whether the point is currently on the screen
	public boolean isOnScreen(Point pos)
	{
		Point tilePos = new Point((int) (pos.x/Tiles.TILE_SIZE), (int) (pos.y/Tiles.TILE_SIZE));
		//closest tile to the left
		World world = game.getWorld();
		int width = world.getMapWidth();
		int height = world.getMapHeight();
		
		//closest tile to the left
		int minX = Math.max(0, (int) (xOffset / Tiles.TILE_SIZE) - 1);
		//closest tile to the right
		int maxX = Math.min(width, (int) ((xOffset + game.getWidth()) / Tiles.TILE_SIZE) + 1);
		//closest tile to the top
		int minY = Math.max(0, (int) (yOffset / Tiles.TILE_SIZE) - 1);
		//closest tile to the bottom
		int maxY = Math.min(height, (int) ((yOffset+ game.getHeight()) / Tiles.TILE_SIZE) + 1);
		
		return tilePos.x > minX && tilePos.x < maxX && tilePos.y > minY && tilePos.y < maxY;
	}
	
	public boolean isOnScreen(Point2D.Float pos)
	{
		return isOnScreen(new Point((int) pos.x, (int) pos.y));
	}
	
	/*
	 * Getters
	 */
	
	public float getXOffset() 
	{
		return xOffset;
	}

	public float getYOffset() 
	{
		return yOffset;
	}
}