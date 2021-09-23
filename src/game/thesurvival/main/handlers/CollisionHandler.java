package game.thesurvival.main.handlers;

import game.thesurvival.main.Game;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.objects.Monster;
import game.thesurvival.main.objects.fruits.Fruit;
import game.thesurvival.main.playerstuff.Player;

//handles collisions with GameObjects
public class CollisionHandler 
{
	private GameObjectHandler objectHandler;
	
	public CollisionHandler(Game game)
	{
		this.objectHandler = game.getObjectHandler();	
	}

	//called from the handler every game tick
	public void checkCollisions() 
	{
		//iterates through all objects
		for(GameObject firstObject : objectHandler.getObjects())
		{
			//for each object it iterates again
			for(GameObject secondObject : objectHandler.getObjects())
			{
				if(areTouching(firstObject, Player.class, secondObject, Fruit.class))
				{
					((Fruit)secondObject).pickUp();
				}
				
				if(areTouching(firstObject, Player.class, secondObject, Monster.class))
				{
					objectHandler.removeObject(secondObject);
					((Player) firstObject).changeHealth(-10);
				}
			}
		}
	}
	
	//to help
	private boolean areTouching(GameObject firstObject, Class<?> class1, GameObject secondObject, Class<?> class2)
	{
		return class1.isInstance(firstObject) && class2.isInstance(secondObject) && firstObject.getBoundsWithoutCamera().intersects(secondObject.getBoundsWithoutCamera());
	}
}