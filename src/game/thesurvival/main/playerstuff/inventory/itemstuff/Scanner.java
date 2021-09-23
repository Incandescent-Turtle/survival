package game.thesurvival.main.playerstuff.inventory.itemstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.playerstuff.inventory.Inventory;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
//used for checking stats
public class Scanner extends Item 
{
	private final int scannerScreenWidth = 146;
	private final int scannerScreenHeight = 243;
	private MaterialStructure currentStructure;
	private int y;
	
	public Scanner() 
	{
		super(Textures.SCANNER_ITEM, new ItemInfo("Scanner", "The last piece of modern technology you have. It's powered by the sun, so it'll "
				+ "never die", "Used to interect with structures to check the resource amount. Water pools, trees, and"
						+ " rocks are all able to scanned"), KeyEvent.VK_H);
		y = Game.height;
	}

	@Override
	public boolean useItem(Game game){return false;}
	
	@Override
	public void tickItem(Game game) 
	{
		final World world = game.getWorld();
		Point mouseTilePos = game.getWorld().getTileSelector().getTileLocation();

		for(MaterialStructure struc : world.getStructures())
		{
			if(struc.getPoses().contains(mouseTilePos) && Util.posWithinPlayerAOE(game, mouseTilePos, 2))
			{
				//when its hovering over a structure
				currentStructure = struc;
				break;
			} else {
				currentStructure = null;
			}
		}
		
		//for "animation"
		if(currentStructure == null && y <= Game.height) y+=4;
		if(currentStructure != null && y >= Game.height - scannerScreenHeight) y-=4;

		y = Util.clamp(y, Game.height - scannerScreenHeight, Game.height);
	}
	
	@Override
	public void render(Game game, Graphics2D g) 
	{
		World world = game.getWorld();
		Point screenPos = world.getTileSelector().getScreenLocation();
		//whether red or white depending on if hovering over a close structure
		g.setColor(currentStructure != null ? new Color(255, 255, 255, 40) : new Color(255, 0, 0, 40));
		//draws sqaure over tile
		g.fillRect(screenPos.x, screenPos.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		
		g.setFont(new Font("ariel", 1, 10));
		g.setColor(Color.WHITE);
		int x = Game.width - Inventory.ITEM_DISPLAY_AREA_SIZE - Inventory.SCROLL_BUTTON_SIZE - 10 - scannerScreenWidth;
		//draws scanner image
		g.drawImage(Textures.SCANNER_SCREEN, x, y, null);
		
		if(currentStructure != null)
		{
			int sectionNum = 0;
			//prints strucutre information
			for(String str : currentStructure.getMaterialDisplay().split("\n"))
			{
				g.drawString(str, x + 26, y + 120 + Util.getStringHeight(g) + (10*sectionNum));
				sectionNum++;
			}
		}
	}
}