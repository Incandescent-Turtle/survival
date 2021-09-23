package game.thesurvival.main.objects.fruits;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import game.thesurvival.main.Game;
import game.thesurvival.main.gfx.PopUp;
import game.thesurvival.main.objects.GroundItem;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.structures.TreeStructure;

//fruit from TreeStructures
public class Fruit extends GroundItem
{
	//the tree it comes from
	protected final TreeStructure tree;
	//it's starting TILE position
	protected Point startPoint;
	//the type of fruit
	protected final FruitType fruitType;
		
	public Fruit(Game game, TreeStructure tree, FruitType fruitType, Random rand, Point tilePos) 
	{
		super(game, fruitType.getTexture(), new Rectangle(0, 0, fruitType.getDim().width, fruitType.getDim().height), new Point(0, 0));
		pos.x = tilePos.x * Tiles.TILE_SIZE;
		pos.y = tilePos.y * Tiles.TILE_SIZE;
		
		if(fruitType.getDim().width != Tiles.TILE_SIZE)
		{
			pos.x += rand.nextInt(Tiles.TILE_SIZE - fruitType.getDim().width);
		}
		
		if(fruitType.getDim().height != Tiles.TILE_SIZE)
		{
			pos.y += rand.nextInt(Tiles.TILE_SIZE - fruitType.getDim().height);
		}
		
		this.tree = tree;
		this.startPoint = tilePos;
		this.fruitType = fruitType;
	}
	
	//renders the fruit type
	@Override
	public void renderObject(Graphics2D g) 
	{
		//calls the fruittypes render method
		fruitType.render(game.getCamera(), g, (int) pos.x, pos.y);
	}
	
	//when the fruit gets picked up
	@Override
	protected void pickUpAction() 
	{
		//for popups
		Point tilePos = getTilePosition();
		Point newScreenPos = new Point(tilePos.x * Tiles.TILE_SIZE, tilePos.y * Tiles.TILE_SIZE);
		newScreenPos.translate(Tiles.TILE_SIZE/2, Tiles.TILE_SIZE/2);
		game.getPopUpManager().addPopUp(new PopUp(game, "+1 " + fruitType.getName(1), newScreenPos, new Color(255, 255, 255)));
		
		game.getPlayer().getInventory().addItem(fruitType.getItem());
		tree.getFruit().remove(this);	
	}
	
	/*
	 * Getters
	 */
	public Point getStartPos()
	{
		return startPoint;
	}
}