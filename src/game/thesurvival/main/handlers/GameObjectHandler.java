package game.thesurvival.main.handlers;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import game.thesurvival.main.objects.GameObject;

//to hold all the game objects
public class GameObjectHandler 
{
	private final CopyOnWriteArrayList<GameObject> objects = new CopyOnWriteArrayList<>();
	
	//render all game objects
	public void render(Graphics2D g)
	{
		for(GameObject obj : objects)
		{
			obj.render(g);
		}
	}
	
	//tick all game objects
	public void tick()
	{
		for(GameObject obj : objects)
		{
			obj.tick();
		}
	}
	
	public void addObject(GameObject obj)
	{
		objects.add(obj);
	}
	
	public void removeObject(GameObject obj)
	{
		objects.remove(obj);
	}

	public CopyOnWriteArrayList<GameObject> getObjects() 
	{
		return objects;
	}

	public void reset() 
	{
		for(GameObject obj : objects)
		{
			obj.reset();
		}
	}
}