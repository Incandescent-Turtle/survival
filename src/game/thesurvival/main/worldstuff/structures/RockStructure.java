package game.thesurvival.main.worldstuff.structures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PopUp;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Items;
import game.thesurvival.main.tilestuff.Tiles;

public class RockStructure extends MaterialStructure
{		
	public RockStructure(Game game, ArrayList<Point> poses) 
	{
		super(game, poses);
		materialAmount = poses.size() + poses.size()/2;
	}
	
	@Override
	public void tickStructure() 
	{
		
	}

	@Override
	public void renderStructure(Graphics2D g) 
	{
		if(disappearing) poses.forEach(pos -> Util.drawTranslucentImage(game, g, Textures.ROCK_TILE, pos, opacity));
	}

	@Override
	public void mineStructure(Point tilePos) 
	{
		if(ticksSinceLastHarvested > 100) 
		{
			ticksSinceLastHarvested = 0;
			//adds rock to the player inventory
			player.getInventory().addItem(Items.ROCK);
			Point newScreenPos = new Point(tilePos.x * Tiles.TILE_SIZE, tilePos.y * Tiles.TILE_SIZE);
			newScreenPos.translate(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2);
			game.getPopUpManager().addPopUp(new PopUp(game, "+1 Rock", newScreenPos, Tiles.ROCK.getColor()));
			materialAmount--;
		}
	}
	
	@Override
	public String getMaterialDisplay() 
	{
		return materialAmount + " rock" + (materialAmount != 1 ? "s" : "");
	}
	
	@Override
	public boolean isMineable() 
	{
		return true;
	}

	@Override
	public void resetStructure() 
	{
		
	}
}