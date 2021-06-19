package game.thesurvival.main.worldstuff.structures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PopUp;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.tilestuff.Tiles;
//water pool
public class WaterStructure extends MaterialStructure
{	
	public WaterStructure(Game game, ArrayList<Point> poses) 
	{
		super(game, poses);
		materialAmount = poses.size() * 10;
		materialAmount = 1;
	}

	@Override
	public void renderStructure(Graphics2D g) 
	{
		if(disappearing)
		for(Point pos : poses)
		{
			Util.drawTranslucentImage(game, g, Util.imageToBufferedImage(Textures.WATER_TILE).getSubimage(0, 0, Tiles.TILE_SIZE, Tiles.TILE_SIZE), pos, opacity);
		}
	}

	@Override
	public void tickStructure() 
	{
	}

	//when player standing in water
	public void harvest(Player player) 
	{
		if(!disappearing && ticksSinceLastHarvested > 20 && player.getWaterLevel() != 100)
		{
			Point tilePos = player.getTilePosition();
			Point newScreenPos = new Point(tilePos.x * Tiles.TILE_SIZE, tilePos.y * Tiles.TILE_SIZE);
			newScreenPos.translate(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2);
			game.getPopUpManager().addPopUp(new PopUp(game, "+1 Water", newScreenPos, Tiles.WATER.getColor()));
			player.changeWaterLevel(6);
			materialAmount--;
			ticksSinceLastHarvested = 0;
		}		
	}

	@Override
	public String getMaterialDisplay() 
	{
		return materialAmount + " water";
	}
	@Override
	public void resetStructure() 
	{
		
	}
}