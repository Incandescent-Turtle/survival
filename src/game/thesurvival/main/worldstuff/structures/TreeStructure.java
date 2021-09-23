package game.thesurvival.main.worldstuff.structures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.PopUp;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.objects.fruits.Fruit;
import game.thesurvival.main.objects.fruits.FruitType;
import game.thesurvival.main.playerstuff.inventory.itemstuff.Items;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.Camera;

//tree
public class TreeStructure extends MaterialStructure 
{
	//all the leaves
	private final ArrayList<Point> leaves = new ArrayList<>();
	//all the stumps
	private final ArrayList<Point> stumps = new ArrayList<>();
	//all the fruit
	private final CopyOnWriteArrayList<Fruit> fruit = new CopyOnWriteArrayList<>();
	private Random rand = new Random();
	//the fruitType of this tree
	private final FruitType fruitType;
	//since a fruit has been spawned 
	private int ticksSinceFruitSpawned = 0;
	
	private int fruitLeft;
	
	public TreeStructure(Game game, ArrayList<Point> poses) 
	{
		super(game, poses);
		//loops through all the poses
		for(Point pos : poses)
		{ 
			//adds leaves
			if(game.getWorld().getTile(pos) == Tiles.LEAF)
			{
				leaves.add(pos);
				//adds stumps
			} else if(game.getWorld().getTile(pos) == Tiles.STUMP)
			{
				stumps.add(pos);
			}
		}
		//removes leaves from the poses array beacuse you can only harvest wood from the stump
		poses.removeAll(leaves);
		//sets the fruit type to a random fruit type
		fruitType = FruitType.getRandomFruitType();
		
		fruitLeft = leaves.size() * 2;
		materialAmount = stumps.size() * 100;
		
		materialAmount = 1;
	}

	@Override
	public void renderStructure(Graphics2D g) 
	{
		Camera camera = game.getCamera();
		
		if(!disappearing)
		{
			//renders the stumps so they render above the player
			for(Point pos : stumps)
			{
				if(camera.isOnScreen(new Point(pos.x * Tiles.TILE_SIZE, pos.y * Tiles.TILE_SIZE)))
				{
					Tiles.LEAF.render(g, game.isInHighDetial(), (int) (pos.x * Tiles.TILE_SIZE - camera.getXOffset()), (int) (pos.y * Tiles.TILE_SIZE - camera.getYOffset()));
					Tiles.STUMP.render(g, game.isInHighDetial(), (int) (pos.x * Tiles.TILE_SIZE - camera.getXOffset()), (int) (pos.y * Tiles.TILE_SIZE - camera.getYOffset()));
				}
			}
		} else {
			if(disappearing) 
			{	
				stumps.forEach(pos -> Util.drawTranslucentImage(game, g, Textures.STUMP_TILE, pos, opacity));
				for(Point pos : leaves)
				{
					Point screenPos = Util.tilePosToScreenPos(game, pos);
					Tiles.DIRT.render(g, game.isInHighDetial(), screenPos.x, screenPos.y);
					Util.drawTranslucentImage(game, g, Textures.LEAF_TILE, pos, opacity);
				}
			}

		}
		
		
		//render fruit
		for(Fruit fruit : fruit)
		{
			if(camera.isOnScreen(fruit.getPos())) fruit.render(g);
		}
	}

	@Override
	public void tickStructure() 
	{
		ticksSinceFruitSpawned++;
		//base spawning off number of stumps. Every stump spawns fruit every 2 seconds
		if(ticksSinceFruitSpawned >= (double) (180/stumps.size()) * 60 && leaves.size() > 1)
		{
			ticksSinceFruitSpawned = 0;
			@SuppressWarnings("unchecked")
			//all the leaves that dont currently ahve fruit
			ArrayList<Point> openLeaves = (ArrayList<Point>) leaves.clone();
			//for every fruit on the tree, takes that pos of the open leaves
			fruit.forEach(f -> openLeaves.remove(f.getStartPos()));
			//not all the leaves can have branches. The more stumps there are the more fruit it can sustain
			if(openLeaves.size() > 0 && openLeaves.size() >= leaves.size()-(leaves.size()/3) - (stumps.size()-1))
			{
				//random leaf pos (fruit class uses Random to offset positions randomly)
				Point pos = openLeaves.get(rand.nextInt(openLeaves.size()));
				//constructs the fruit and adds to arrays
				Fruit f = new Fruit(game, this, fruitType, new Random(), pos);
				fruit.add(f);
				fruitLeft--;
				game.getObjectHandler().addObject(f);
			}
		}
	}

	
	@Override
	protected void vanishStructure() 
	{
		for(Point pos : leaves)
		{
			world.setTile(pos, Tiles.DIRT);
			world.getLeaves().remove(pos);
		}
	}
	
	//mining the stumps
	@Override
	public void mineStructure(Point tilePos) 
	{
		if(ticksSinceLastHarvested > 100)
		{
			//adds wood to inventory
			player.getInventory().addItem(Items.WOOD);
			ticksSinceLastHarvested = 0;
			//popups while harvesting
			Point newScreenPos = new Point(tilePos.x * Tiles.TILE_SIZE, tilePos.y * Tiles.TILE_SIZE);
			newScreenPos.translate(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2);
			game.getPopUpManager().addPopUp(new PopUp(game, "+1 Wood", newScreenPos, Tiles.STUMP.getColor()));
			materialAmount--;
		}
	}
	
	@Override
	public String getMaterialDisplay() 
	{
		//displays fruit left and wood left
		return fruitLeft + " " + fruitType.getName(fruitLeft) + "\n" + materialAmount + " wood";
	}
	
	@Override
	public boolean isMineable() 
	{
		return true;
	}
	
	@Override
	public void resetStructure() 
	{
		fruit.clear();
	}
	
	public ArrayList<Point> getLeaves()
	{
		return leaves;
	}
	
	public CopyOnWriteArrayList<Fruit> getFruit()
	{
		return fruit;
	}
}