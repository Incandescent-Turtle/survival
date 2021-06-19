package game.thesurvival.main.handlers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import game.thesurvival.main.Game;
import game.thesurvival.main.interfaces.Controllable;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.objects.Monster;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Item;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Items;

//to handle key pressed for the whole game
public class KeyHandler extends KeyAdapter 
{
	private final Game game;
	private final GameObjectHandler objectHandler;
	private final ArrayList<Controllable> controllableObjects = new ArrayList<>();
	
	public KeyHandler(Game game)
	{
		this.game = game;
		this.objectHandler = game.getObjectHandler();
	}
	
	//when a key is pressed
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		switch(key)
		{
			//closes game
			case KeyEvent.VK_ESCAPE: 
				game.terminate();
				break;
			//fullscreen toggle
			case KeyEvent.VK_F11:
				fullscreen();
				break;
				
			case KeyEvent.VK_O:
				for(GameObject obj : game.getObjectHandler().getObjects())
				{
					if(obj instanceof Monster) game.getObjectHandler().removeObject(obj);
				}
				game.getObjectHandler().addObject(new Monster(game, new Point((int) (MouseHandler.mouseX + game.getCamera().getXOffset()), (int) (MouseHandler.mouseY + game.getCamera().getYOffset()))));
				break;
				
			case KeyEvent.VK_J:
				game.getPlayer().changeWaterLevel(-10);
		}
		
		//sends key presses to gameobjects that need them
		for(GameObject obj : objectHandler.getObjects())
		{
			if(obj instanceof Controllable) ((Controllable) obj).keyPressed(key);
		}
		
		for(Controllable obj : controllableObjects)
		{
			obj.keyPressed(key);
		}
		
		for(Item item : Items.ITEM_KEYBINDINGS.keySet())
		{
			if(key == Items.ITEM_KEYBINDINGS.get(item))
			{
				game.getPlayer().getInventory().setCurrentItem(item);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		//sends key releases to gameobjects that need them
		for(GameObject obj : objectHandler.getObjects())
		{
			if(obj instanceof Controllable) ((Controllable) obj).keyReleased(key);
		}
		
		for(Controllable obj : controllableObjects)
		{
			obj.keyReleased(key);
		}
	}
	
	//to go in and out of fullscreen mode
	private void fullscreen()
	{
		JFrame frame = game.getFrame();
		GraphicsDevice fullscreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		frame.dispose();

	    if (frame.isUndecorated()) {
	      fullscreenDevice.setFullScreenWindow(null);
	      frame.setUndecorated(false);
	    } else {
	      frame.setUndecorated(true);
	      fullscreenDevice.setFullScreenWindow(frame);
	    }

	    frame.setVisible(true);
	    frame.repaint();
	    game.requestFocus();
	}
	
	public void add(Controllable obj)
	{
		controllableObjects.add(obj);
	}
}