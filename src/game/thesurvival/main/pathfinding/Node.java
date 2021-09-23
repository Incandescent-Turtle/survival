package game.thesurvival.main.pathfinding;

import java.awt.Point;
//node for pathfinding
public class Node extends Point
{
	private static final long serialVersionUID = 1L;
	
	//distance from start
	public int gCost;
	//distance from target(calculated)
	public int hCost;
	//used for tracing the path
	public Node parent;
	
	public Node(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}

	public int getFCost()
	{
		return hCost + gCost;
	}
}