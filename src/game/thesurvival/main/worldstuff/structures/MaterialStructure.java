package game.thesurvival.main.worldstuff.structures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;
//Like rocks, water, trees
public abstract class MaterialStructure 
{
	//positions of the "components" or the individual tiles
	protected final ArrayList<Point> poses;
	protected final Game game;
	protected boolean useable = true;
	protected int ticksSinceLastHarvested = 0;
	protected int materialAmount;
	protected boolean disappearing = false;
	protected int opacity = 100;
	protected final World world;
	private int ticksSinceOpacityChanged = 0;
	protected Player player;
	
	public MaterialStructure(Game game, ArrayList<Point> poses)
	{
		this.poses = poses;
		this.game = game;
		world = game.getWorld();
		player = game.getPlayer();
	}
	
	public abstract void renderStructure(Graphics2D g);
	
	//renders the strucutre
	public final void render(Graphics2D g)
	{
		if(disappearing)
		{
			for(Point tilePos : poses)
			{
				Point screenPos = Util.tilePosToScreenPos(game, tilePos);
				Tiles.DIRT.render(g, game.isInHighDetial(), screenPos.x, screenPos.y);
			}
		}
		renderStructure(g);
		if(disappearing) player.render(g);

	}
	//ticks the strucutre
	public abstract void tickStructure();
	//resets the strucutre
	public abstract void resetStructure();
	//returns whetehr animation is played. Tilepos that its being mined from
	public final void mine(Point tilePos)
	{
		if(!disappearing) mineStructure(tilePos);
	}
	
	public void mineStructure(Point tilePos){}
	
	//called when the object has been mined and opacity is at 0
	protected final void vanish()
	{
		world.getStructures().remove(this);
		poses.forEach(pos -> world.setTile(pos, Tiles.DIRT));
		vanishStructure();
	}
	
	protected void vanishStructure() {}

	
	//for displaying information with the scanner
	public abstract String getMaterialDisplay();
	
	public boolean isMineable()
	{
		return false;
	}
	
	public void tick()
	{
		ticksSinceOpacityChanged++;
		ticksSinceLastHarvested++;
		if(materialAmount <= 0 && !disappearing) disappearing = true;
		
		if(ticksSinceOpacityChanged > 1 && disappearing) 
		{
			ticksSinceOpacityChanged = 0;
			opacity--;
		}
		
		if(opacity <= 0)
		{
			vanish();
			world.getStructures().remove(this);
			poses.forEach(pos -> world.setTile(pos, Tiles.DIRT));
		}
		
		tickStructure();
	}
	
	public ArrayList<Point> getPoses()
	{
		return poses;
	}
	
	public void reset()
	{
		useable = true;
		resetStructure();
	}
}