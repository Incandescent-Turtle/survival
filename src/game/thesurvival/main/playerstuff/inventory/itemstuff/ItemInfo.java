package game.thesurvival.main.playerstuff.inventory.itemstuff;

public class ItemInfo 
{
	private final String singularName, pluralName, lore, uses, harvestingInfo;
	private boolean stackable;
	
	public ItemInfo(String singularName, String pluralName, String lore, String uses, String harvestingInfo) 
	{
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.lore = lore;
		this.uses = uses;
		this.harvestingInfo = harvestingInfo;
		stackable = true;
	}
	
	//non-stackable
	public ItemInfo(String singularName, String lore, String uses) 
	{
		this(singularName, singularName, lore, uses, "");
		stackable = false;
	}
	
	public String getName()
	{
		return singularName;
	}
	
	public String getName(int amt)
	{
		return amt > 1 || amt == 0 ? pluralName : singularName;
	}
	
	public String getLore()
	{
		return lore;
	}
	
	public String getUses()
	{
		return uses;
	}
	
	public String getHarvestInfo()
	{
		return harvestingInfo;
	}
	
	public boolean isStackable()
	{
		return stackable;
	}
}