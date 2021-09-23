package game.thesurvival.main.objects;

import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.pathfinding.PathFinder;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.tilestuff.Tiles;

public class Monster extends GameObject 
{
	private double moveX, moveY;
	private final PathFinder pathFinder;
	private final Player player;
	private List<Point> path;
	private int ticksSincePathGenerated = 0;
	private final double speed = 3;
	//whether the player was found last pathfind or not
	private boolean playerFound = true;
	private double angle;
	
	//g = start
	//h - target
	public Monster(Game game, Point pos) 
	{
		super(game, null, new Rectangle(0,0,Tiles.TILE_SIZE-1,Tiles.TILE_SIZE-1), pos);
		this.pathFinder = new PathFinder(game, 300);
		player = game.getPlayer();
		path = pathFinder.getPath(getTilePosition(), game.getPlayer().getTilePosition());
		playerFound = !(path.size() == 0);
	}

	@Override
	public void tick() 
	{
		if(!world.getTile(getTilePosition()).isWalkable() || !world.isWithinWorld(getTilePosition())) objectHandler.removeObject(this);
		ticksSincePathGenerated++;
	
		//when the player isnt in a building
		if(playerFound)
		{
			//if the path is empty and a path is allowed to be generated
			if(path.size() == 0 || (ticksSincePathGenerated > 30))
			{
				ticksSincePathGenerated = 0;
				//gets a new path
				path = pathFinder.getPath(getTilePosition(), game.getPlayer().getTilePosition());
				playerFound = !(path.size() == 0);
			} else {
				Point currentPathWorldPos = Util.tilePosToWorldPos(path.get(0));
				//to snap to tile pos for traveling around corners and such
				if(abs(currentPathWorldPos.x - pos.x) < speed + 1) pos.x = currentPathWorldPos.x;
				if(abs(currentPathWorldPos.y - pos.y) < speed + 1) pos.y = currentPathWorldPos.y;
				
				//changes the angle to face the target block
				angle = atan2(currentPathWorldPos.y - pos.y, currentPathWorldPos.x - pos.x); 
				//removes the current path tile if the monster is on it
				if(path.size() > 0 && pos.x == currentPathWorldPos.x && pos.y == currentPathWorldPos.y) path.remove(0);
			}
		} else {
			//angle towards player
			angle = atan2(player.getPos().y - pos.y, player.getPos().x - pos.x);
			//updates every 5 seconds (to save resources)
			if(ticksSincePathGenerated > 5*60) 
			{
				ticksSincePathGenerated = 0;
				path = pathFinder.getPath(getTilePosition(), game.getPlayer().getTilePosition());
				playerFound = !(path.size() == 0);
			}	
		}
		move(angle);
	}

	@Override
	protected void renderObject(Graphics2D g) 
	{
		//draws image
		Util.drawRotatedImage(Textures.MONSTER, g, angle + toRadians(90), (int)getBoundsWithCamera().x, (int)getBoundsWithCamera().y);
		//path
		/*qfor(Point pos : path)
		{
			Point screenP = Util.tilePosToScreenPos(game, pos);
			g.fillRect(screenP.x, screenP.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE); 
		}*/
	}
	
	//to move the monster
	private void move(double angle)
	{		
		//updates movement variables based on angle facing and speed
		moveX = cos(angle) * speed;
		moveY = sin(angle) * speed;
				
		
		//tile collision logic
		checkTileCollisions();
		
		//monster collisions
		for(GameObject obj : game.getObjectHandler().getObjects())
		{
			if(obj instanceof Monster && obj != this)
			{
				Monster monster = (Monster) obj;
				
				//the bounds of this monster
				Rectangle2D.Float primBounds = (Rectangle2D.Float) getBoundsWithoutCamera().clone();
				primBounds.getBounds().translate((int)moveX, (int)moveY);
				
				//the bounds of the iterated monster
				Rectangle2D.Float secBounds = (Rectangle2D.Float) monster.getBoundsWithoutCamera().clone();
				secBounds.getBounds().translate((int)monster.getMoveX(), (int)monster.getMoveY());
				
				// if the new bounds (with added x and y move) collide
				if(primBounds.intersects(secBounds))
				{
					//angle away from the monster
					double angleAwayFromMonster = Math.toRadians(180) + atan2(secBounds.y - primBounds.y, secBounds.x - primBounds.x);

					//moves away from monster
					moveX = cos(angleAwayFromMonster) * speed/2;
					moveY += sin(angleAwayFromMonster) * speed/2;
					//can only collide with one
					break;
				}
			}
		}
		pos.x += moveX;
		pos.y += moveY;
	}
	
	//tile collision logic
	private void checkTileCollisions()
	{	
		if(moveX > 0) //moving right
		{
			//right tile coords
			int x = (int) (pos.x + bounds.width + moveX) / Tiles.TILE_SIZE;
			//checks top right and bottom right corners for collisions
			if(!(world.canBeWalkedOn(x, (int) ((pos.y) / Tiles.TILE_SIZE)) && world.canBeWalkedOn(x, (int) ((pos.y + bounds.height) / Tiles.TILE_SIZE))))
			{
				//if cant be walked on moves to the edge of the tile
				moveX += x * Tiles.TILE_SIZE - bounds.width - bounds.x - pos.x - 4;
			}
		} else if(moveX < 0) { //moving left
			//left tile coords
			int x = (int) (pos.x + moveX + 1) / Tiles.TILE_SIZE;
			//checks top left and bottom left corners for collisions
			if(!(world.canBeWalkedOn(x, (int) ((pos.y) / Tiles.TILE_SIZE)) && world.canBeWalkedOn(x, (int) ((pos.y + bounds.height) / Tiles.TILE_SIZE))))
			{
				//if cant be walked on moves to the edge of the tile
				moveX += x * Tiles.TILE_SIZE + Tiles.TILE_SIZE - bounds.x - pos.x + 3;
			}
		}
		
		if(moveY > 0) //moving down
		{
			//bottom tile row
			int y = (int) (pos.y + bounds.height + moveY + 1) / Tiles.TILE_SIZE;
			//checks bottom left and right corners for collisions
			if(!(world.canBeWalkedOn((int) ((pos.x) / Tiles.TILE_SIZE), y) && world.canBeWalkedOn((int) (pos.x + bounds.width) / Tiles.TILE_SIZE, y)))
			{
				//if cant be walked on moves to the edge of the tile
				moveY += y * Tiles.TILE_SIZE - bounds.height - bounds.y - pos.y - 4;
			}
		} else if(moveY < 0) { //moving up
			//top tile coords
			int y = (int) (pos.y + moveY) / Tiles.TILE_SIZE;
			//checks top left and right corners for collisions
			if(!(world.canBeWalkedOn((int) (pos.x) / Tiles.TILE_SIZE, y) && world.canBeWalkedOn((int) (pos.x + bounds.width) / Tiles.TILE_SIZE, y)))
			{
				//if cant be walked on moves to the edge of the tile
				moveY += y * Tiles.TILE_SIZE + Tiles.TILE_SIZE - bounds.y - pos.y + 3;
			}
		}
	}
	
	public double getMoveX()
	{
		return moveX;
	}
	
	public double getMoveY()
	{
		return moveY;
	}
}