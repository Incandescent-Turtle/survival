package game.thesurvival.main.playerstuff.inventory.itemstuff;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.objects.fruits.FruitType;

//item class
public abstract class Item 
{
	private final ItemInfo info;
	
	private final Image texture;
		
	protected int ticksSinceLastUsed = 0;
	
	public Item(Image texture, ItemInfo info, int keyBinding)
	{
		this.info = info;
		this.texture = texture;
		//adds to the item list (used for debugging and stuff)
		Items.ITEM_LIST.add(this);
		//adds key binding
		Items.ITEM_KEYBINDINGS.put(this, keyBinding);
	}
	
	public Item(FruitType fruitType)
	{
		//all fruit have the keybind of F for fruit or food
		this(fruitType.getItemTexture(), new ItemInfo(fruitType.getName(), fruitType.getName(0), "The only food left in "
				+ "the world is fruit...you're doomed. \nHere, take " + Util.getStringWithIndefiniteAcrticle(fruitType.getName().toLowerCase()), 
				"Eat " + Util.getStringWithIndefiniteAcrticle(fruitType.getName().toLowerCase()) + " to get " 
				+ fruitType.getFoodAmount() + " hunger points", "Harvested from trees around the map. "
				+ "The bigger the trunks of the tree are, the more frequently " + fruitType.getName(0).toLowerCase() + " will spawn!")
		, KeyEvent.VK_F);
	}
	
	//when the item is used
	public final void use(Game game)
	{ 
		//uses the item and reduces stack size if instructed
		if(useItem(game) && info.isStackable())
		{
			final HashMap<Item, Integer> itemMap = game.getPlayer().getInventory().getItemMap();
			int newCount =  itemMap.get(this) - 1;
			itemMap.remove(this);
			itemMap.put(this, newCount);
		}	
	}
	
	//returns whether or not it should be removed
	public abstract boolean useItem(Game game);
	public void render(Game game, Graphics2D g) {}
	public void tickItem(Game game) {}
	
	public final void tick(Game game) 
	{
		if(ticksSinceLastUsed != Integer.MAX_VALUE) ticksSinceLastUsed++;
		tickItem(game);
	}
	
	public Image getTexture()
	{
		return texture;
	}

	public ItemInfo getInfo()
	{
		return info;
	}
	
	public void allowItemUse()
	{
		ticksSinceLastUsed = Integer.MAX_VALUE;
	}
}