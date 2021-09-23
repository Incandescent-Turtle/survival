package game.thesurvival.main.tilestuff;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;
import game.thesurvival.main.worldstuff.structures.Structures;
import game.thesurvival.main.worldstuff.structures.WaterStructure;
//part of the water strucutre
//has animations
public class WaterTile extends StructureSpawnTile 
{
	//animation strip
	private BufferedImage waterImg = Util.imageToBufferedImage(Textures.WATER_TILE);
	private int ticksSinceTextureChanged = 0;
	//xPos the teture strip is at
	private int x;

	public WaterTile() 
	{
		super(null, Structures.WATER_STRUCTURE_ALLOWED_TILES, Color.BLUE);
		texture = waterImg.getSubimage(0, 0, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
	}
	
	@Override
	public void tick() 
	{
		ticksSinceTextureChanged++;
		//animates the water. 1 second to travel tile length
		if(ticksSinceTextureChanged >= 60/Tiles.TILE_SIZE)
		{
			ticksSinceTextureChanged = 0;
			moveTexture();
		}
	}
	
	private void moveTexture()
	{		
		//moves the x position and re-assigns the texture
		texture = waterImg.getSubimage(x++, 0, Tiles.TILE_SIZE, Tiles.TILE_SIZE);
		if(x == Tiles.TILE_SIZE + 1) x = 1;
	}
	
	//part of a water structure
	@Override
	public MaterialStructure newStructure(Game game, ArrayList<Point> poses) 
	{
		return new WaterStructure(game, poses);
	}
}