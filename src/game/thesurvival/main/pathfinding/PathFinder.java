package game.thesurvival.main.pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.thesurvival.main.Game;
import game.thesurvival.main.Util;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.World;
//for pathfinding
public class PathFinder
{
	//copy of the world tiles as nodes
	private final Node[][] nodes;
	private final World world;
	private final Game game;
	//the amount of tiles that have been checked in a single
	private final int maxTilesChecked;
	
	public PathFinder(Game game, int maxTilesChecked) 
	{
		this.maxTilesChecked = maxTilesChecked;
		this.game = game;
		this.world = game.getWorld();
		//setting up nodes array
		nodes = new Node[world.getMapWidth()][world.getMapHeight()];
		for(int x = 0; x < world.getMapWidth(); x++)
			for(int y = 0; y < world.getMapHeight(); y++)
				nodes[x][y] = new Node(x, y);
	}
	
	public PathFinder(Game game) 
	{
		this(game, Integer.MAX_VALUE);
	}
	
	public List<Point> getPath(Point startTilePos, Point targetTilePoint) 
	{
		int nodesChecked = 0;
		try {
			Node startNode = nodes[startTilePos.x][startTilePos.y];
			Node targetNode = nodes[targetTilePoint.x][targetTilePoint.y];
	
			ArrayList<Node> openSet = new ArrayList<>();
			ArrayList<Node> closedSet = new ArrayList<>();
			openSet.add(startNode);
			Comparator<Node> fCostComparator = new Comparator<Node>() {
	
				@Override
				public int compare(Node node1, Node node2) 
				{
					return node1.getFCost() < node2.getFCost() ? -1: node1.getFCost() == node2.getFCost() ? 0 : 1;
				}
			};
	
			while (openSet.size() > 0 && nodesChecked <= maxTilesChecked) 
			{
				nodesChecked++;
				Collections.sort(openSet, fCostComparator);
				Node currentNode = openSet.get(0);
			
				openSet.remove(currentNode);
				closedSet.add(currentNode);
	
				if (currentNode == targetNode) return createPath(startNode, currentNode);
	
				for(Point neighbourPos : Util.getSquareTilePositions(game, currentNode, 1, false)) 
				{
					Node neighbour = nodes[neighbourPos.x][neighbourPos.y];
					if((!canBeWalkedOn(neighbour.x, currentNode.y) || !canBeWalkedOn(currentNode.x, neighbour.y) || !canBeWalkedOn(neighbour.x, neighbour.y)) || closedSet.contains(neighbour)) continue;
					int newCostToNeighbour = currentNode.gCost + findDistance(currentNode, neighbour);
					if (newCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
						neighbour.gCost = newCostToNeighbour;
						neighbour.hCost = findDistance(neighbour, targetNode);
						neighbour.parent = currentNode;
	
						if (!openSet.contains(neighbour)) openSet.add(neighbour);
					}
				}
			}
		} catch (Exception e) {
			return new ArrayList<Point>();
		}
		return new ArrayList<Point>();
	}
	
	private List<Point> createPath(Node startNode, Node endNode) 
	{
		ArrayList<Point> path = new ArrayList<>();
		Node currentNode = endNode;

		while (currentNode != startNode) 
		{
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		Collections.reverse(path);
		return path;
	}
	
	public int findDistance(Point pos1, Point pos2) 
	{
		int dstX = Math.abs(pos1.x - pos2.x);
		int dstY = Math.abs(pos1.y - pos2.y);

		if (dstX > dstY)
			return 14*dstY + 10* (dstX-dstY);
		return 14*dstX + 10 * (dstY-dstX);
	}
	
	public void resetNodes()
	{
		for(int x = 0; x < world.getMapWidth() - 1; x++)
			for(int y = 0; y < world.getMapHeight() - 1; y++)
			{
				Node node = nodes[x][y];
				node.gCost = 0;
				node.hCost = 0;
				node.parent = null;
			}
	}
	
	private boolean canBeWalkedOn(int x, int y)
	{
		return world.canBeWalkedOn(x, y) || world.getTile(x, y) == Tiles.CLOSED_DOOR;
	}
}