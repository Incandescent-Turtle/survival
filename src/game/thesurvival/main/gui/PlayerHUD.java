package game.thesurvival.main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PlayerTextures;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.playerstuff.inventory.Inventory;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Item;

//Player stats
public class PlayerHUD 
{
	private final Player player;
	private final Game game;
	private final Inventory playerInventory;
	
	public PlayerHUD(Game game)
	{
		this.game = game;
		this.player = game.getPlayer();
		this.playerInventory = player.getInventory();
	}
	
	public void render(Graphics2D g)
	{
		if(player.isOverlayShown())
		{
			//renders hunger bar
			renderVerticleBar(g, new Color(240, 135, 29), 1, player.getHungerLevel());
			//renders water bar
			renderVerticleBar(g, new Color(58, 163, 255), 2, player.getWaterLevel());
			//renders health bar
			renderVerticleBar(g, Color.RED, 3, player.getHealth());
		}
		if(player.isInventoryShown()) playerInventory.render(g);
		//draws the held item in the bottom right of screen
		renderHeldItem(g);
	}
	
	//helper to render the verticle stat bars
	private void renderVerticleBar(Graphics2D g, Color color, int multiplier, int stat)
	{
		//set dimensions of health bar
		int height = 100;
		int width = 40;
		//outline colour
		g.setColor(Color.BLACK);
		//x coordinate
		int x = game.getWidth() - (width * multiplier) - (10 * multiplier);
		//draws the outline
		g.drawRect(x, 10, 40, height);
		//filling colour
		g.setColor(color);
		//how much the bar should be filled based on the stat given
		int filledHeight = (int) ((stat/100f) * (height-1));
		//filling the health bar
		g.fillRect(x+1, 10 + height-filledHeight, width-1, filledHeight);
	}
	
	private void renderHeldItem(Graphics2D g)
	{
		//setting values
		final int displaySize = Inventory.ITEM_DISPLAY_AREA_SIZE;
		final int x = Game.width - displaySize - 10;
		final int y = Game.height - 10 - displaySize;
		final int scrollButtonWidth = PlayerTextures.INVENTORY_TRIANGLE.getWidth(null);
		final int scrollButtonY = y + displaySize/2;
		//bezel size of the item display square
		final int bezelSize = Inventory.ITEM_DISPLAY_BEZEL_SIZE;
		final Item currentItem = playerInventory.getCurrentItem();
		//drawing the box for the item to be placed in
		g.drawImage(PlayerTextures.INVENTORY_ITEM_AREA, x, y, null);
		//draws the upwards facing scroll button
		playerInventory.drawScrollButton(g, 0, 1, x - scrollButtonWidth - 2, scrollButtonY - scrollButtonWidth - 2);
		//draws the downwarsd facing scroll button
		playerInventory.drawScrollButton(g, 180, -1, x - scrollButtonWidth - 2, scrollButtonY + 2);
		//when an item exists
		if(currentItem != null)
		{	
			//draws item
			g.drawImage(currentItem.getTexture(), x + bezelSize, y + bezelSize, null);
			//draws item count
			if(currentItem.getInfo().isStackable())
			{
				//item cuont
				String itemCount = "" + playerInventory.getItemMap().get(currentItem);
				g.setColor(Color.WHITE);
				g.setFont(new Font("ariel", 1, 40));
				//draws in bottom right of box
				g.drawString(itemCount, x + displaySize - bezelSize - Util.getStringWidth(itemCount, g), y + displaySize - bezelSize);
			}
		}
	}
}
