package game.thesurvival.main.handlers;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//used to do cool mouse things and access loaction/states of mouse
public class MouseHandler extends MouseAdapter
{

	public static double mouseX, mouseY;
	public static boolean mouseDown = false;
	
	//updates mouse coords
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		mouseX = e.getPoint().getX();
		mouseY = e.getPoint().getY();
	}
	
	//when the mouse is down
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;	
	}
	
	//when the mouse isnt down
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;		
	}
	
	//when the mouse is down and moving
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		mouseX = (int) e.getPoint().getX();
		mouseY = (int) e.getPoint().getY();
	}

	//checks to see whether or not the mouse is within the given rectangle (usually used for buttons). Helper
	public static boolean mouseWithin(Rectangle rectangle)
	{
		return mouseX >= rectangle.getMinX() && mouseX <= rectangle.getMaxX() && mouseY <= rectangle.getMaxY() && mouseY >= rectangle.getMinY() ? true : false;
	}
}