package game.thesurvival.main.worldstuff.structures;

import java.util.ArrayList;

import game.thesurvival.main.tilestuff.Tile;
import game.thesurvival.main.worldstuff.World;

@SuppressWarnings("unchecked")
//"static" class
public class Structures 
{
	private Structures() {}
	
	/*
	 * Tiles that are apart of a specific structure
	 */
	public static final ArrayList<Tile> TREE_STRUCTURE_ALLOWED_TILES;
	public static final ArrayList<Tile> ROCK_STRUCTURE_ALLOWED_TILES;
	public static final ArrayList<Tile> WATER_STRUCTURE_ALLOWED_TILES;
	//All tiles that are a part of any strucutres
	public static final ArrayList<Tile> STRUCTURE_ALLOWED_TILES;

	
	static
	{
		STRUCTURE_ALLOWED_TILES = new ArrayList<>();
		//adds all the tile arrays to the big one
		addStructures(
				TREE_STRUCTURE_ALLOWED_TILES = new ArrayList<>(),
				ROCK_STRUCTURE_ALLOWED_TILES = new ArrayList<>(),
				WATER_STRUCTURE_ALLOWED_TILES = new ArrayList<>()
		);
	}
	
	//helper to add structures to the big one
	private static void addStructures(final ArrayList<Tile>...tiles)
	{
		for(ArrayList<Tile> array : tiles)
		{
			World.ALLOWED_STRUCTURE_TILES.add(array);
		}
	}
}