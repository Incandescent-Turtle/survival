package game.thesurvival.main.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;

public class PopUp
{
	private final Point2D.Float pos;
	private final String message;
	private double size;
	private final Color color;
	private PopUpManager manager;
	private final Game game;
		
	public PopUp(Game game, String message, Point2D.Float pos, Color color) 
	{
		this.message = message;
		this.pos = pos;
		this.color = color;
		this.game = game;
		size = 20;
	}
	
	public PopUp(Game game, String message, Point pos, Color color) 
	{
		this(game, message, new Point2D.Float(pos.x, pos.y), color);
	}
	
	public PopUp setManager(PopUpManager manager)
	{
		this.manager = manager;
		return this;
	}
	
	public void tick()
	{
		size -= .1;
		pos.y -= 1;
		if(size <= 14) manager.removePopUp(this);
	}
	
	public void render(Graphics2D g)
	{
		g.setColor(color);
		g.setFont(new Font("ariel", 0, (int) size));
		Util.drawXCenteredString(message, pos.x - game.getCamera().getXOffset(), pos.y - game.getCamera().getYOffset(), g);
	}
}