package game.thesurvival.main.objects.fruits;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Item;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.Camera;

//for the different types of friot
public enum FruitType 
{
	//apple
	APPLE(Textures.APPLE_FRUIT, Textures.APPLE_ITEM, new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Apple", "Apples", 5) {
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			//red
			g.setColor(Color.RED);
			//circle
			g.fill(new Ellipse2D.Float(x - camera.getXOffset(), y - camera.getYOffset(), dim.width, dim.height));
		}
	},
	
	//orange
	ORANGE(Textures.ORANGE_FRUIT, Textures.ORANGE_ITEM, new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Orange", "Oranges", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			//orange
			g.setColor(new Color(255, 119, 0));
			//circle
			g.fill(new Ellipse2D.Float(x - camera.getXOffset(), y - camera.getYOffset(), dim.width, dim.height));
		}
	},
	
	//Lemon
	PEACH(Textures.PEACH_FRUIT, Textures.PEACH_ITEM, new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Peach", "Peaches", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			g.setColor(new Color(235, 171, 127));
			//circle
			g.fillOval((int) (x - camera.getXOffset()), (int) (y - camera.getYOffset()), dim.width, dim.height);
		}
	},
	
	//Banana
	BANANA(Textures.BANANA_FRUIT, Textures.BANANA_ITEM, new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Banana", "Bananas", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			drawBanana(g, camera, x, y, dim);
		}
	},
	
	CHERRY(Textures.CHERRY_FRUIT, Textures.CHERRY_ITEM, new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Cherry", "Cherries", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			int xPos = (int) (x - camera.getXOffset());
			int yPos = (int) (y - camera.getYOffset());
			
			Shape cherry = new Ellipse2D.Float(xPos, yPos, dim.width/3*2, dim.height-3);
			g.setColor(Color.RED);
			g.fill(cherry);
			//outline
			g.setColor(Color.BLACK);
			g.draw(cherry);
			
			cherry = new Ellipse2D.Float(xPos + dim.width/3, yPos - 3, dim.width/3*2, dim.height-3);
			g.setColor(Color.RED);
			g.fill(cherry);
			g.setColor(Color.BLACK);
			g.draw(cherry);
		}
	},
	
	PEAR(Textures.PEAR_FRUIT, Textures.PEAR_ITEM,  new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Pear", "Pears", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			g.setColor(Color.GREEN);
			int xPos = (int) (x - camera.getXOffset());
			int yPos = (int) (y - camera.getYOffset());
			
			g.fillOval((int) (xPos + (dim.width - (dim.width/3f + dim.width/2))/2), yPos+1, dim.width/3 + dim.width/2, dim.height/2);
			g.fillOval(xPos, yPos + dim.height/2, dim.width, dim.height/2);
		}
	},
	
	LEMON(Textures.LEMON_FRUIT, Textures.LEMON_ITEM,  new Dimension(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2), "Lemon", "Lemons", 5)
	{
		@Override
		public void render(Camera camera, Graphics2D g, float x, float y) 
		{
			g.setColor(Color.YELLOW);
			//circle
			g.fillOval((int) (x - camera.getXOffset()), (int) (y - camera.getYOffset()), dim.width, dim.height);
		}
	}
	;
	
	//dimensions of the fruit
	protected final Dimension dim;
	//the amount of food the fruit supplies to the player
	protected final int foodAmount;
	//food texture
	protected final Image texture;
	protected final Image itemTexture;

	//name of the fruit (for inventory)
	private final String singularName;
	private final String pluralName;
	//the item the player gets when picked up
	private final Item item;

	
	private FruitType(Image texture, Image itemTexture, Dimension dim, String singularName, String pluralName, int foodAmount)
	{
		//makes sure the fruit isnt too big or too small
		this.foodAmount = foodAmount;
		this.dim = dim;
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.itemTexture = itemTexture;
		
		if(texture != null) this.texture = texture.getScaledInstance(dim.width, dim.height, 0);
		else this.texture = texture;
		
		item = new Item(this) {
			
			@Override
			public boolean useItem(Game game) 
			{
				if(ticksSinceLastUsed > 60)
				{
					ticksSinceLastUsed = 0;
					game.getPlayer().changeHungerLevel(foodAmount);
					return true;
				}
				return false;
			}
		};
	}
	
	//to render the fruit
	public abstract void render(Camera camera, Graphics2D g, float x, float y);
	
	/*
	 * Getters
	 */
	
	public int getFoodAmount()
	{
		return foodAmount;
	}
	
	public Image getTexture()
	{
		return texture;
	}
	
	public Image getItemTexture()
	{
		return itemTexture;
	}
	
	public Dimension getDim() 
	{
		return dim;
	}
	
	public String getName(int amt) 
	{
		return amt > 1 || amt == 0 ? pluralName : singularName;
	}
	
	public String getName() 
	{
		return singularName;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	/*
	 * Static
	 */
	//returns a random type of fruit (used when creating trees)
	public static FruitType getRandomFruitType()
	{
		return FruitType.values()[new Random().nextInt(FruitType.values().length)];
	}
	
	//code from https://www.databaseadm.com/article/11265932/Drawing+a+cresent%3F
	protected static void drawBanana(Graphics2D g, Camera camera, float x, float y, Dimension dim)
	{
		g.setColor(Color.YELLOW);
		//biggest dim
		int radius = Math.max(Util.clamp(dim.width, 0, Tiles.TILE_SIZE), Util.clamp(dim.height, 0, Tiles.TILE_SIZE))/2;
		int[] xPoints = new int[360];
		int[] yPoints = new int[360];
		for(int i=0; i< 180; i++) 
		{   
			double angle = Math.toRadians(i-90);
		    int x1 = (int) (Math.cos(angle) * radius);
		    int y1 = (int) (Math.sin(angle) * radius);
		     
		    int width = x1 * 2;
		    int x2 = 0;
		     
		    x2 = (int) ( width * 0.75);
		     

		    x1 = (int) (x - camera.getXOffset()) + radius + x1;                    
		    x2 = x1 - (width - x2);     
		                              
		    y1 = (int) (y - camera.getYOffset()) + radius + y1;
		     
		    xPoints[i] = x1;
		    yPoints[i] = y1;
		     
		    xPoints[xPoints.length-(i+1)] = x2;
		    yPoints[yPoints.length-(i+1)] = y1;               
		}	
		g.fillPolygon(xPoints, yPoints, xPoints.length);
	}
}