package game.thesurvival.main.playerstuff.inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PlayerTextures;
import game.thesurvival.main.handlers.MouseHandler;
import game.thesurvival.main.interfaces.Controllable;
import game.thesurvival.main.objects.fruits.FruitType;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Item;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Items;
//player inventory
public class Inventory implements Controllable
{
	/*
	 * Static
	 */
	public static int ITEM_DESC_LINE_LIMIT = 54;
	public static final int ITEM_SIZE = 98;
	public static final int ITEM_DISPLAY_BEZEL_SIZE = 9;
	public static final int ITEM_DISPLAY_AREA_SIZE = 116;
	public static final int SCROLL_BUTTON_SIZE = PlayerTextures.INVENTORY_TRIANGLE.getWidth(null);

	//map of all the items and their count
	private final HashMap<Item, Integer> itemMap = new HashMap<>();	
	//all items
	private final ArrayList<Item> items = new ArrayList<>();
	//current inv item
	private Item currentItem = null;
		
	private int ticksSinceItemSwitched = 0;
		
	//top corner of inventory background
	private int invX, invY;
	
	//location with bezels
	private int itemDisplayX, itemDisplayY;
	//location without bezels
	private int displayItemX, displayItemY;
	
	private int scrollButtonX;
	
	private final int bezelSize = 8;
	
	private final Font itemNameFont, itemInfoFont, subtitleFont;
	//dims of the main inventory background
	private final int width, height;
	
	private int ticksSinceCraftedDoor = 0;
			
	public Inventory(Game game)
	{
		//sets stuff
		width = PlayerTextures.INVENTORY_BACKGROUND.getWidth(null);
		height = PlayerTextures.INVENTORY_BACKGROUND.getHeight(null);
  		itemNameFont = new Font("ariel", 1, 20);
		itemInfoFont = new Font("ariel", 0, 12);
		subtitleFont = new Font("ariel", 1, 18);
		
		setPositionValues();
		//adds hands
		addItem(currentItem = Items.HANDS);
		//adds multi tool
		addItem(Items.MULTI_TOOL);
		addItem(Items.SCANNER);
		
		//for testing
		FruitType.getRandomFruitType();
		for(Item item : Items.ITEM_LIST) for(int i = 0; i < 1000; i++) addItem(item);
	}
		
	//adds an item to the inventory
	public void addItem(Item item)
	{
		//if already existing increases item count by 1
		if(items.contains(item))
		{
			int count = itemMap.get(item);
			itemMap.remove(item);
			//increases item count
			itemMap.put(item, count + 1);
		} else {
			items.add(item);
			itemMap.put(item, 1);
		}
	}
	
	public void tick()
	{
		ticksSinceCraftedDoor++;
		//removes empty stacks
		ticksSinceItemSwitched++;
		if(currentItem != null && itemMap.get(currentItem) == 0) 
		{
			int index = items.indexOf(currentItem);
			items.remove(currentItem);
			itemMap.remove(currentItem);
			if(items.size() > 0) currentItem = items.get(index - 1 < 0 ? items.size()-1: index - 1);
		}
		//hands are default item
		if(currentItem == null) currentItem = Items.HANDS;
	}
	
	//to display the inventory
	public void render(Graphics2D g)
	{	
		//inventory background
		g.drawImage(PlayerTextures.INVENTORY_BACKGROUND, invX, invY, null);
		//draws square for items to be displayed in
		g.drawImage(PlayerTextures.INVENTORY_ITEM_AREA, itemDisplayX, itemDisplayY, null);
		//draws the item if it exists
		if(currentItem != null)
		{
			g.drawImage(currentItem.getTexture(), displayItemX, displayItemY, null);
		}
		//to cycle through items
		drawScrollButton(g, 0, 1, scrollButtonX, invY + height/2 - SCROLL_BUTTON_SIZE - 2);
		drawScrollButton(g, 180, -1, scrollButtonX, invY + height/2 + 2);
		//draws item name and count
		if(currentItem != null) 
		{
			//display item name and count
			g.setColor(Color.WHITE);
			g.setFont(itemNameFont);
			int count = itemMap.get(currentItem);
			//if not stackable, doesnt display count
			Util.drawXCenteredString((currentItem.getInfo().isStackable() ? count : "") + " " + currentItem.getInfo().getName(count), displayItemX + ITEM_SIZE/2, ITEM_DISPLAY_AREA_SIZE + itemDisplayY + 20, g);
			drawItemInfo(g);
		}
		//drawing the crafting window
		drawCraftingWindow(g);
		setPositionValues();
	}
	
	//draws and handles everything for the crafting window
	private void drawCraftingWindow(Graphics2D g)
	{
		//crafting window size
		final int windowSize = 128;
		//window xPos
		final int xPos = invX - windowSize - bezelSize;
		//draws the crafting window background
		g.drawImage(PlayerTextures.INVENTORY_CRAFTING_WINDOW, xPos, invY, null);
		g.setFont(subtitleFont);
		g.setColor(Color.WHITE);
		//draws the title
		Util.drawXCenteredString("Crafting", xPos + windowSize/2, invY + Util.getStringHeight(g), g);
		int titleHeight = Util.getStringHeight(g);
		g.setFont(itemInfoFont);
		//x pos of the recipe
		int xRecipe = xPos + bezelSize + 23;
		//the highest yPos of the recipe
		int yRecipeBase = invY + bezelSize + titleHeight + 15;
		g.setFont(new Font("ariel", 0, 16));
		//line spacing of recipe
		int ySpace = Util.getStringHeight(g) + 3;
		g.setColor(Color.black);
		//rectangle representing the crafting area
		Rectangle rect = new Rectangle(xRecipe, yRecipeBase - Util.getStringHeight(g) + 5, Util.getStringWidth("= 1 Door", g), ySpace*3 -5);
		//if mouse hovering over crafting area
		if(MouseHandler.mouseWithin(rect))
		{
			//whether the player has the needed resources
			boolean hasMaterials = inventoryHas(Items.WOOD, 2) && inventoryHas(Items.ROCK, 2);
			//if player cant craft draws red rectangle, if else draws white
			g.setColor(hasMaterials && ticksSinceCraftedDoor > 60 ? new Color(255, 255, 255, 40) : new Color(255, 0, 0, 40));
			g.fill(rect);
			//mouse clicked
			if(MouseHandler.mouseDown)
			{
				if(ticksSinceCraftedDoor > 60 && hasMaterials)
				{
					ticksSinceCraftedDoor = 0;
					//reduces item count for wood and rock
					for(Item item : new Item[] {Items.WOOD, Items.ROCK})
					{
						int newCount =  itemMap.get(item) - 2;
						itemMap.remove(item);
						itemMap.put(item, newCount);
					}
					//adds new door item
					addItem(Items.DOOR);
				}
				//to make button pushing smooth
			} else ticksSinceCraftedDoor = 61;
		} else ticksSinceCraftedDoor = 61;
		//drawing recipe
		g.setColor(Color.WHITE);
		g.drawString("2 Wood", xRecipe, yRecipeBase);
		g.drawString("2 Rocks", xRecipe, yRecipeBase + ySpace);
		g.drawString("= 1 Door", xRecipe, yRecipeBase + ySpace * 2);
	}
	
	private void drawItemInfo(Graphics2D g)
	{
		int yPos = displayItemY + 5, itemInfoX = displayItemX + ITEM_SIZE + 26;
		final int lineSpacing = 4;
		final int subtitleSpacing = 3;
		
		//display item lore
		g.setFont(subtitleFont);
		//subtitle
		g.drawString("Background:", itemInfoX, yPos);
		g.setFont(itemInfoFont);
		//prints the item's "lore" in the invntory screen
		String lore = currentItem.getInfo().getLore();
		Util.drawMultiLineString(g, lore, itemInfoX, yPos + Util.getStringHeight(g) + subtitleSpacing, lineSpacing, ITEM_DESC_LINE_LIMIT);
		//to start the next section below
		final int loreHeight = Util.getHeightOfMultiLineString(g, lore, lineSpacing, ITEM_DESC_LINE_LIMIT);
		
		//display item uses
		g.setFont(subtitleFont);
		//so it is draw under the lore section
		yPos = Util.getStringHeight(g) + yPos + loreHeight + 20;
		//subtitle
		g.drawString("Uses:", itemInfoX, yPos);
		g.setFont(itemInfoFont);
		//prints the uses to the inventory
		String uses = currentItem.getInfo().getUses();
		Util.drawMultiLineString(g, uses, itemInfoX, yPos + Util.getStringHeight(g) + subtitleSpacing, lineSpacing, ITEM_DESC_LINE_LIMIT);
		//so the next section knows where to start
		final int usesHeight = Util.getHeightOfMultiLineString(g, uses, lineSpacing, ITEM_DESC_LINE_LIMIT);
		
		if(currentItem.getInfo().isStackable())
		{
			//display item harvest info
			g.setFont(subtitleFont);
			//so it starts under the uses section
			yPos = Util.getStringHeight(g) + yPos + usesHeight + 20;
			//subtitle
			g.drawString("Harvest Info:", itemInfoX, yPos);
			//draws the 
			g.setFont(itemInfoFont);
			String harvestInfo = currentItem.getInfo().getHarvestInfo();
			Util.drawMultiLineString(g, harvestInfo, itemInfoX, yPos + Util.getStringHeight(g) + subtitleSpacing, lineSpacing, ITEM_DESC_LINE_LIMIT);
		}
	}
	
	private void setPositionValues()
	{
		invX = Game.width/2 - width/2;
		invY = Game.height/2 - height/2;
		itemDisplayX = invX+20;
		itemDisplayY = invY+20;
		displayItemX = itemDisplayX + ITEM_DISPLAY_BEZEL_SIZE;
		displayItemY = itemDisplayY + ITEM_DISPLAY_BEZEL_SIZE;
		scrollButtonX = invX + width - SCROLL_BUTTON_SIZE - bezelSize;
	}
	
	public void drawScrollButton(Graphics2D g, double rotation, int increment, int x, int y)
	{
		Image img = PlayerTextures.INVENTORY_TRIANGLE;
		//hover
		if(MouseHandler.mouseWithin(new Rectangle(x, y, img.getWidth(null), img.getHeight(null)))) 
		{
			img = PlayerTextures.INVENTORY_SELECTED_TRIANGLE;
			//onclick
			if(MouseHandler.mouseDown) switchItem(increment);
			else ticksSinceItemSwitched = 61;
		}
		Util.drawRotatedImage(img, g, Math.toRadians(rotation), x, y);
	}
	
	public void switchItem(int increment)
	{
		if(ticksSinceItemSwitched > 60)
		{
			ticksSinceItemSwitched = 0;
			int newValue = items.indexOf(currentItem) + increment;
			if(items.size() > 1) currentItem =  increment < 0 ? (items.get(newValue < 0 ? items.size()-1: newValue)) : items.get(newValue > items.size() -1 ? 0 : newValue);
		}
	}

	//resets the inventory
	public void reset()
	{
		itemMap.clear();
	}

	public Item getCurrentItem()
	{
		return currentItem;
	}
	
	public void setCurrentItem(Item item)
	{
		currentItem = item;
	}
	
	public HashMap<Item, Integer> getItemMap()
	{
		return itemMap;
	}
	
	public void keyPressed(int key) 
	{
		if(key == KeyEvent.VK_UP) switchItem(1);
		if(key == KeyEvent.VK_DOWN) switchItem(-1);
	}
	
	public void keyReleased(int key)
	{
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) ticksSinceItemSwitched = 61;
	}
	
	private boolean inventoryHas(Item item, int amt)
	{
		if(itemMap.containsKey(item)) return itemMap.get(item) >= amt;
		return false;
	}
}