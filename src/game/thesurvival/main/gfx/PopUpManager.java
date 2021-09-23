package game.thesurvival.main.gfx;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class PopUpManager 
{		
	private CopyOnWriteArrayList<PopUp> popUps = new CopyOnWriteArrayList<>();
	
	public void tick()
	{
		for(PopUp popUp : popUps)
		{
			popUp.tick();
		}
		//popUps.forEach(p -> p.tick());
	}
	
	public void render(Graphics2D g)
	{
		for(PopUp popUp : popUps)
		{
			popUp.render(g);
		}
		//popUps.forEach(p -> p.render(g));
	}
	
	public void addPopUp(PopUp popUp)
	{
		popUps.add(popUp.setManager(this));
	}
	
	public void removePopUp(PopUp popUp)
	{
		popUps.remove(popUp);
	}
}