package game.thesurvival.main.objects;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import game.thesurvival.main.Game;

//to render and implement the logic for items you can pick up on the ground
//pickups are detected from the CollisionHandler
public abstract class GroundItem extends GameObject 
{		
	public GroundItem(Game game, Image texture, Rectangle bounds, Point pos)
	{
		super(game, texture, bounds, pos);
	}
	
	//to specify what happens when the item is picked up
	protected abstract void pickUpAction();

	//unused. Only rendered. Collisions handled in collsionhandler
	@Override
	public void tick()
	{
	}
		
	//called from handlers and such to run the code for the pickup. Removes object when picked up
	public void pickUp() 
	{
		pickUpAction();
		objectHandler.removeObject(this);
	}
}