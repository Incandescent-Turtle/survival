package game.thesurvival.main.tilestuff;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import game.thesurvival.main.Game;
import game.thesurvival.main.worldstuff.structures.MaterialStructure;

//a tile that can spawn a structure
public abstract class StructureSpawnTile extends Tile
{
	public StructureSpawnTile(Image texture, ArrayList<Tile> structureTileArray, Color color) 
	{
		super(texture, structureTileArray, color);
	}
	
	//strucutre it spawns
	public abstract MaterialStructure newStructure(Game game, ArrayList<Point> poses);
}
