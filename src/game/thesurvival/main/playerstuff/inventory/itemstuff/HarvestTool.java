package game.thesurvival.main.playerstuff.inventory.itemstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
//tool the player can harvest things with
public class HarvestTool extends Item 
{
	private int ticksBreakingMineableTile = 0;
	private Point tilePosBeingMined = new Point(-1, -1);
	
	public HarvestTool() 
	{
		//keybind and info
		super(Textures.MULTI_TOOL, new ItemInfo("Multi-Tool", "The last thing"
			+ " your parents gave you...and the only thing now that will keep you alive", "Use this tool to defend yourself while"
					+ " also using it to harvest materials"), KeyEvent.VK_Q);
	}
	
	@Override
	public void tickItem(Game game) 
	{
		ticksBreakingMineableTile++;
	}
	
	@Override
	public boolean useItem(Game game) 
	{
		//tile the mouse is hovering over
		Point mouseTilePos = game.getWorld().getTileSelector().getTileLocation();
		Player player = game.getPlayer();
		World world = game.getWorld();
		//tile player is currently on
		Point playerTilePos = player.getTilePosition();
		//checks to see if the tile is within the players mining AOE and the tile can be mined
		if(Util.posWithinPlayerAOE(game, mouseTilePos, 1) && world.getTile(mouseTilePos).isMineable())
		{
			//for animations
			player.setHarvesting(true);
			//whether the same block is being mined as last tick
			if(mouseTilePos.equals(tilePosBeingMined))
			{
				//player faces the block
				player.setDirection(Math.atan2(mouseTilePos.y - player.getTilePosition().y, mouseTilePos.x - player.getTilePosition().x));
				if(ticksBreakingMineableTile > 60)
				{
					ticksBreakingMineableTile = 0;
					//everything broken turns to dirt
					world.setTile(mouseTilePos, Tiles.DIRT);
				}
				//if its a different tile than last tick
			} else {
				tilePosBeingMined = mouseTilePos;
				ticksBreakingMineableTile = 0;
			}
			return false;
		}
		
		outer:
		//all structures
		for(MaterialStructure structure : world.getStructures())
		{
			//poses included in the structure
			ArrayList<Point> structureParts = structure.getPoses();
			//for every adjeacent block to the player
			for(Point adjPos : Util.getSquareTilePositions(game, playerTilePos, 1))
			{
				//if the adj tile in the the strucutre and the player is harvesting it
				if(structureParts.contains(adjPos) && structure.isMineable() && adjPos.equals(mouseTilePos))
				{
					//calls the harvest function
					structure.mine(adjPos);
					//for animations
					player.setHarvesting(true);
					player.setDirection(Math.atan2(adjPos.y - player.getTilePosition().y, adjPos.x - player.getTilePosition().x));
					//can only harvest from one structure per tick
					break outer;
				}
			}
		}
		return false;
	}
		
	public void render(Game game, Graphics2D g) 
	{
		World world = game.getWorld();
		Point screenPos = world.getTileSelector().getScreenLocation();
		Point tilePos = world.getTileSelector().getTileLocation();
		//either white or red transparent rectangle drawn
		g.setColor(canMine(game, tilePos) ? new Color(255, 255, 255, 40) : new Color(255, 0, 0, 40));
		g.fillRect(screenPos.x, screenPos.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		//knife aoe
		/*for(Point po : Util.getSquareTilePositions(game.getPlayer().getTilePosition(), 1))
		{
			g.fillRect(po.x * Tiles.TILE_SIZE - (int) game.getCamera().getXOffset(), po.y * Tiles.TILE_SIZE - (int) game.getCamera().getYOffset(), Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		}*/
	}
	
	//whether the tile is mineable
	private boolean canMine(Game game, Point tilePos)
	{
		World world = game.getWorld();
		boolean isInStructure = false;

		//all structures in the world
		for(MaterialStructure structure : world.getStructures())
		{			
			//if mouse pos is on a part of the structure
			if(structure.getPoses().contains(tilePos) && structure.isMineable()) 
			{
				isInStructure = true;
				break;
			}
		}
		return Util.posWithinPlayerAOE(game, tilePos, 1) && (isInStructure || world.getTile(tilePos).isMineable());
	}
}