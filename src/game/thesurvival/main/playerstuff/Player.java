package game.thesurvival.main.playerstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import game.thesurvival.main.Game;
import game.thesurvival.main.TileSelector;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PlayerTextures;
import game.thesurvival.main.handlers.MouseHandler;
import game.thesurvival.main.interfaces.Controllable;
import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.objects.Monster;
import game.thesurvival.main.playerstuff.inventory.Inventory;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
import game.thesurvival.main.worldstuff.structures.WaterStructure;

//gameobject - recieves key presses
public class Player extends GameObject implements Controllable
{
	//key bindings
	boolean upPressed, downPressed, rightPressed, leftPressed, spacePressed;
	//player speed
	private float speed;
	//stats
	private int hungerLevel, waterLevel, health;
	//whether or not the hudoverlay is shown
	private boolean HUDOverlayShown;
	//for toggling HUD
	private int ticksSinceHUDToggled;
	//ticks since water level has decreased, ticks since water level has been at 0 and the player has taken damage because of that
	private int ticksSinceWaterDeducted, ticksSinceWaterDamage;
	//ticks that the player has been moving since hunger level decreased, ticks since hunger level has been at 0 and the player has taken damage because of that
	private int movementTicksSinceHungerDeducted, ticksSinceHungerDamage = 0;
	//ticks since player has switched item with arrow keys
	private int ticksSinceItemSwitched = 0;
	//player inventory
	private Inventory inventory;
	//whether the player is mining a harvestable structure
	private boolean isHarvesting = false;
	//whether the player is standing on a water tile
	private boolean isInWater = false;
	//in radians
	private double direction = 0;
	
	private boolean inventoryShown = false;
	private int ticksSinceInventoryToggled = 0;
	
	/*
	 * For animations
	 */
	private BufferedImage[] textures;
	private int currentTextureIndex = 0;
	private int textureTicks = 0;
	private boolean isMoving = false;

	public Player(Game game) 
	{
		super(game, null, new Rectangle((Tiles.TILE_SIZE/2) - 10, (Tiles.TILE_SIZE/2) - 10, 20, 20), new Point.Float(Tiles.TILE_SIZE, Tiles.TILE_SIZE));
		game.getKeyHandler().add(inventory = new Inventory(game));
		//coudlnt put this in the constructor for some reason
		textures = PlayerTextures.FISTS_ANIMATION; 
		currentTextureIndex = 0;
	}

	//called from the objectHandler every game tick
	@Override
	public void tick() 
	{	
		if(MouseHandler.mouseDown && game.getWorld().canBeWalkedOn(new TileSelector(game).getTileLocation())) game.getObjectHandler().addObject(new Monster(game, new Point((int) (MouseHandler.mouseX + game.getCamera().getXOffset()), (int) (MouseHandler.mouseY + game.getCamera().getYOffset()))));
		tryHarvestWater();
		if(isMoving) movementTicksSinceHungerDeducted++;
		ticksSinceItemSwitched++;
		ticksSinceInventoryToggled++;
		//if the player is harvesting it switches costumes
		if(isHarvesting) textures = PlayerTextures.KNIFE_MELEE_ANIMATION; 
		else textures = PlayerTextures.FISTS_ANIMATION;
		isHarvesting = false;
		//can use items every second
		if(!inventoryShown && spacePressed && inventory.getCurrentItem() != null) inventory.getCurrentItem().use(game);
		
		//every 4 ticks the animation changes
		textureTicks++;
		if(textureTicks == 4)
		{	
			currentTextureIndex++;
			textureTicks = 0;
		}
		
		//every 10 seconds water is deducted
		ticksSinceWaterDeducted++;
		if(ticksSinceWaterDeducted == 60 * 10)
		{
			ticksSinceWaterDeducted = 0;
			waterLevel -= 1;
		}

		//when water is empty player starts to lose health
		if(waterLevel == 0)
		{
			ticksSinceWaterDamage++;
			if(ticksSinceWaterDamage == 60 * 10)
			{
				ticksSinceWaterDamage = 0;
				health -= 1;
			}
		}
		
		//when hunger level is 0 the player starts to lose health
		if(hungerLevel == 0)
		{
			ticksSinceHungerDamage++;
			if(ticksSinceHungerDamage == 60 * 10)
			{
				ticksSinceHungerDamage = 0;
				health -= 1;
			}
		}
		
		//every 10 movement ticks hunger is deducted
		if(movementTicksSinceHungerDeducted == 60 * 10)
		{
			movementTicksSinceHungerDeducted = 0;
			--hungerLevel;
		}
		
		//moves player according to which keys are pressed
		if(!inventoryShown) move();
		//centers the game around the player
		camera.centerObject(this);
		ticksSinceHUDToggled++;
		clampValues();
		if(health <= 0) game.gameOver();
		inventory.tick();
	}
	
	//called from the objectHandler every game tick. Will always be called because the texture field is null
	@Override
	public void renderObject(Graphics2D g) 
	{	
		//pos on the screen
		int xPos = (int) (pos.x - camera.getXOffset());
		int yPos = (int) (pos.y - camera.getYOffset());
		
		//if textures should be rendered
		if(game.isInHighDetial())
		{
			//draws the rotated player
			currentTextureIndex = currentTextureIndex > textures.length - 1 ? 0 : currentTextureIndex;
			Util.drawRotatedImage(PlayerTextures.FEET_ANIMATION[isMoving ? currentTextureIndex : 5], g, direction, xPos, yPos, xPos + (107 / (279/Tiles.TILE_SIZE)), yPos + (113 / (219/Tiles.TILE_SIZE)));		
			Util.drawRotatedImage(textures[currentTextureIndex], g, direction, xPos, yPos, xPos + (107 / (279/Tiles.TILE_SIZE)), yPos + (113 / (219/Tiles.TILE_SIZE)));		
		} else {
			//draws oval
			g.setColor(Color.BLUE);
			g.fill(new Ellipse2D.Float(xPos, yPos, Tiles.TILE_SIZE, Tiles.TILE_SIZE));
		}
	}
	
	private void tryHarvestWater()
	{
		isInWater = world.getTile(getTilePosition()) == Tiles.WATER;
		for(MaterialStructure struc : world.getStructures())
		{
			if(struc instanceof WaterStructure && struc.getPoses().contains(getTilePosition())) ((WaterStructure)struc).harvest(this);
		}
	}
	
	//called every time a key is pressed (KeyHandler)
	@Override
	public void keyPressed(int key) 
	{
		inventory.keyPressed(key);
		//helper to update keybindings
		updateKeys(key, true);
	}

	//called every time a key is released (KeyHandler)
	@Override
	public void keyReleased(int key) 
	{		
		//helper to update keybindings
		updateKeys(key, false);
	}
	
	//helper to update keybindings
	private void updateKeys(int key, boolean pressed)
	{
		//checks key presses and sets the booleans
		switch(key)
		{
			//up
			case KeyEvent.VK_W:
				upPressed = pressed;
				break;
				
			//down
			case KeyEvent.VK_S:
				downPressed = pressed;
				break;
				
			//left
			case KeyEvent.VK_A:
				leftPressed = pressed;
				break;
				
			//right
			case KeyEvent.VK_D:
				rightPressed = pressed;
				break;
				
			//for inventory
			case KeyEvent.VK_UP:
				if(!pressed) ticksSinceItemSwitched = 21;
				else if(ticksSinceItemSwitched > 20) {
					inventory.switchItem(1);
					ticksSinceItemSwitched = 0;
				}

				break;
				
				//for inventory
			case KeyEvent.VK_DOWN:
				if(!pressed) ticksSinceItemSwitched = 21;
				else if(ticksSinceItemSwitched > 20) {
					inventory.switchItem(-1);
					ticksSinceItemSwitched = 0;
				}
				break;
				
			//H - toggles HUD overlay
			case KeyEvent.VK_H:
				if(!pressed) ticksSinceHUDToggled = 20;
				if(ticksSinceHUDToggled > 20) 
				{
					ticksSinceHUDToggled = 0;
					HUDOverlayShown = !HUDOverlayShown;
				}
				break;
			
				//space
			case KeyEvent.VK_SPACE:
				spacePressed = pressed;
				if(!pressed) inventory.getCurrentItem().allowItemUse();
				break;
				
			case KeyEvent.VK_E:
				if(ticksSinceInventoryToggled > 60)
				{
					ticksSinceInventoryToggled = 0;
					inventoryShown = !inventoryShown;
				}
				if(!pressed) ticksSinceInventoryToggled = 61;
				break;
		}
	}
	
	//to clamp values to a range
	private void clampValues()
	{
		waterLevel = Util.clamp(waterLevel, 0, 100);
		hungerLevel = Util.clamp(hungerLevel, 0, 100);
		health = Util.clamp(health, 0, 100);
	}

	//called on reset and when first starting
	@Override
	public void setUp() 
	{
		speed = 3f;
		upPressed = downPressed = rightPressed = leftPressed = false;
		waterLevel = hungerLevel = health = 100;
		HUDOverlayShown = true;
		ticksSinceHUDToggled = 0;
	}
	
	@Override
	protected void resetObject() 
	{
		inventory.reset();
	}
	
	//to move player when keys are pressed. A total mess + headache
	private void move()
	{
		/*
		 * For animations
		 */
		if(!isHarvesting)
		{
			double degDirection = Math.toDegrees(direction);
			if(leftPressed) degDirection = 180;
			if(rightPressed) degDirection = 0;

			//setting direction
			if(upPressed)
			{
				degDirection = -90;
				if(rightPressed) degDirection = -45;
				if(leftPressed) degDirection = -135;
			}
			
			if(downPressed)
			{
				degDirection = 90;
				if(rightPressed) degDirection = 45;
				if(leftPressed) degDirection = 135;
			}
			direction = Math.toRadians(degDirection);
		}
		
		
		/*
		 * For collisions
		 */
		//if player is in water they move slower
		float speed = isInWater ? this.speed/4: this.speed;
		//amounts to move by
		float moveX = 0, moveY = 0;
		//setting amounts based on which keys are pressed
		if(upPressed) moveY-=speed;
		if(downPressed) moveY+=speed;
		if(leftPressed) moveX-=speed;
		if(rightPressed) moveX+=speed;
		//default
		isMoving = false;
		final Point pos = getBoundsWithoutCamera().getBounds().getLocation();
		
		//when right arrow is down
		if(moveX > 0)
		{
			//this is the x coordinate of the tile column the right side of the player is in ( tile coords)
			int x = (int) (pos.x + bounds.width + moveX + 1) / Tiles.TILE_SIZE;
			//checks top right and bottom right corners for collisions
			if(world.canBeWalkedOn(x, (int) ((pos.y) / Tiles.TILE_SIZE)) && world.canBeWalkedOn(x, (int) ((pos.y + bounds.height) / Tiles.TILE_SIZE)))
			{
				//if the tiles the player's right side are in are walkable, the player moves
				this.pos.x += moveX;
				isMoving = true;
			} else {
				//if the tiles the player's right aren't walkable, the player moves to the edge of the tiles
				this.pos.x = x * Tiles.TILE_SIZE - bounds.width - bounds.x - 1;
			}
		} else if(moveX < 0) { // when left arrow is down
			//this is the x coordinate of the tile column the left side of the player is in
			int x = (int) (pos.x + moveX) / Tiles.TILE_SIZE;
			//checks top left and bottom left corners for collisions
			if(world.canBeWalkedOn(x, (int) ((pos.y) / Tiles.TILE_SIZE)) && world.canBeWalkedOn(x, (int) ((pos.y + bounds.height) / Tiles.TILE_SIZE)))
			{
				//if the tiles the player's left side are in are walkable, the player moves
				this.pos.x += moveX;
				isMoving = true;
			} else {
				//if the tiles the player's left aren't walkable, the player moves to the edge of the tiles
				this.pos.x = x * Tiles.TILE_SIZE + Tiles.TILE_SIZE - bounds.x;
			}
		}
		//when the down arrow is pressed
		if(moveY > 0)
		{
			//this is the y coordinate of the tile row the bottom of the player is in
			int y = (int) (pos.y + bounds.height + moveY + 1) / Tiles.TILE_SIZE;
			//checks bottom left and right corners for collisions
			if(world.canBeWalkedOn((int) ((pos.x) / Tiles.TILE_SIZE), y) && world.canBeWalkedOn((int) (pos.x + bounds.width) / Tiles.TILE_SIZE, y))
			{
				//if the tiles the player's bottom is in are walkable, the player moves
				this.pos.y += moveY;
				isMoving = true;
			} else {
				//if the tiles the player's bottom is in aren't walkable, the player moves to the edge of the tiles
				this.pos.y = y * Tiles.TILE_SIZE - bounds.height - bounds.y - 1;
			}
		} else if(moveY < 0) { //when the player is moving up
			//this is the y coordinate of the tile row the top of the player is in
			int y = (int) (pos.y + moveY) / Tiles.TILE_SIZE;
			//checks top left and right corners for collisions
			if(world.canBeWalkedOn((int) (pos.x) / Tiles.TILE_SIZE, y) && world.canBeWalkedOn((int) (pos.x + bounds.width) / Tiles.TILE_SIZE, y))
			{
				//if the tiles the player's top is in are walkable, the player moves
				this.pos.y += moveY;
				isMoving = true;
			} else {
				//if the tiles the player's top is in aren't walkable, the player moves to the edge of the tiles
				this.pos.y = y * Tiles.TILE_SIZE + Tiles.TILE_SIZE - bounds.y;
			}
		}
	}
	
	/*
	 * Getters
	 */
	
	public int getHungerLevel() 
	{
		return hungerLevel;
	}

	public int getWaterLevel() 
	{
		return waterLevel;
	}

	public int getHealth() 
	{
		return health;
	}
	
	public boolean isHUDShown()
	{
		return HUDOverlayShown;
	}
	
	public void changeWaterLevel(int amount)
	{
		waterLevel = Util.clamp(waterLevel + amount, 0, 100);
	}
	
	public void changeHungerLevel(int amount)
	{
		hungerLevel = Util.clamp(hungerLevel + amount, 0, 100);
	}
	
	public void changeHealth(int amount)
	{
		health = Util.clamp(health + amount, 0, 100);
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public boolean isInventoryShown()
	{
		return inventoryShown;
	}
	
	public boolean isOverlayShown()
	{
		return HUDOverlayShown;
	}
	
	public void setHarvesting(boolean bool)
	{
		isHarvesting = bool;
	}
	
	public void setDirection(double radAngle)
	{
		direction = radAngle;
	}
}