package game.thesurvival.main.objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import game.thesurvival.main.Game;
import game.thesurvival.main.handlers.GameObjectHandler;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.Camera;
import game.thesurvival.main.worldstuff.World;

//renderable and tickable
public abstract class GameObject 
{
	protected final Game game;
	protected final GameObjectHandler objectHandler;
	//game camera
	protected final Camera camera;
	//x and y
	protected final Point.Float pos;
	protected final Image texture;
	protected final Random rand;
	//the position of the bounding box RELATIVE TO THE OBJECT
	protected final Rectangle bounds;
	protected final World world;
	
	public GameObject(Game game, Image texture, Rectangle bounds, Point.Float pos) 
	{ 
		this.game = game;
		this.world = game.getWorld();
		this.camera = game.getCamera();
		this.pos = pos;
		this.bounds = bounds;
		this.texture = texture;
		this.objectHandler = game.getObjectHandler();
		this.rand = new Random();
		setUp();
	}
	
	public GameObject(Game game, Image texture, Rectangle bounds, Point pos) 
	{ 
		this(game, texture, bounds, new Point2D.Float(pos.x, pos.y));
	}

	//every game tick
	public abstract void tick();
	//to reset. can override
	protected void resetObject(){}
	//cant override. Calls setup and restar methods
	public final void reset()
	{
		setUp();
		resetObject();
	}
	//can override. called when object is created
	public void setUp(){};
	
	//every game render. Renders buffered image if
	public final void render(final Graphics2D g)
	{
		if(game.isInHighDetial() && !(texture == null)) g.drawImage(texture, (int) getBoundsWithCamera().x, (int) getBoundsWithCamera().y, null);
		else renderObject(g);
		renderExtra(g);
	}
	
	protected abstract void renderObject(final Graphics2D g);
	protected void renderExtra(final Graphics2D g){}

	//for collisions
	public final Rectangle2D.Float getBoundsWithCamera()
	{
		return new Rectangle2D.Float(pos.x + bounds.x - camera.getXOffset(), pos.y + bounds.y - camera.getYOffset(), bounds.width, bounds.height);
	}
	
	public final Rectangle2D.Float getBoundsWithoutCamera()
	{
		return new Rectangle2D.Float(pos.x + bounds.x, pos.y + bounds.y, bounds.width, bounds.height);
	}
	
	public final Rectangle getBounds()
	{
		return bounds;
	}
	
	//used to get the position of the object on the tile grid
	public final Point getTilePosition()
	{
		return new Point(Math.round(pos.x / Tiles.TILE_SIZE), Math.round(pos.y / Tiles.TILE_SIZE));
	}
	
	/*
	 * Getters
	 */
	public final Point.Float getPos()
	{
		return pos;
	}
}