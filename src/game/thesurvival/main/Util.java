package game.thesurvival.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import game.thesurvival.main.objects.GameObject;
import game.thesurvival.main.tilestuff.Tiles;

//Game Utilities
public class Util 
{
	private Util() {};
	
	//checking if the point is on the player's current screen
	public static boolean isOnScreen(Point pos)
	{
		return pos.x >= 0 && pos.x <= Game.width && pos.y >= 0 && pos.y <= Game.height;
	}
	
	//returns the tiles adjecent to the one given. Uses tile map positions
	public static ArrayList<Point> getAdjacentTilePoses(Point tilePos)
	{
		ArrayList<Point> list = new ArrayList<>();
		list.addAll((Arrays.asList(new Point[] { modifyPos(tilePos, -1, 0), modifyPos(tilePos, 0, -1), modifyPos(tilePos, 1, 0), modifyPos(tilePos, 0, 1) })));
		return list;
	}
	
	public static ArrayList<Point> getSquareTilePositions(Game game, Point pos, int radius)
	{
		return getSquareTilePositions(game, pos, radius, true);
	}
	
	public static ArrayList<Point> getSquareTilePositions(Game game, Point pos, int radius, boolean startIncluded)
	{
		ArrayList<Point> poses = new ArrayList<>();
		for(int x = -radius; x < radius+1; x++)
		{
			for(int y = -radius; y < radius+1; y++)
			{
				Point newPos = new Point(pos.x + x, pos.y + y);
				if(game.getWorld().isWithinWorld(newPos) && (((!startIncluded && !(x == 0 && y == 0)) || startIncluded))) poses.add(newPos);
			}
		}
		return poses;
	}
	//used to easily modify Points
	public static Point modifyPos(Point pos, int x, int y)
	{
		return new Point(pos.x + x, pos.y + y);
	}
	
	//used to easily modify colours
	public static Color modifyColour(Color color, int r, int g, int b, int a)
	{
		return new Color(color.getRed() + r, color.getGreen() + g, color.getBlue() + b, color.getAlpha() + a);
	}
	
	//used to easily modify colours
	public static Color modifyColour(Color color, int r, int g, int b)
	{
		return modifyColour(color, r, g, b, 0);
	}
	
	public static int clamp(int value, int min, int max)
	{
		return value > max ? max : value < min ? min : value;
	}
	
	public static BufferedImage imageToBufferedImage(Image im) 
	{
	     BufferedImage bi = new BufferedImage (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	     Graphics bg = bi.getGraphics();
	     bg.drawImage(im, 0, 0, null);
	     bg.dispose();
	     return bi;
	}
	
	public static void drawRotatedImage(Image img, Graphics2D g, double radAngle, int xPos, int yPos, int xRotate, int yRotate)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform trans = new AffineTransform();
		trans.rotate(radAngle, xRotate, yRotate);
		g2d.transform( trans );
		g2d.drawImage(img, xPos, yPos, null);
		g2d.dispose();
	}
	
	//rotates around center
	public static void drawRotatedImage(Image img, Graphics2D g, double radAngle, int xPos, int yPos)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform trans = new AffineTransform();
		trans.rotate(radAngle, xPos + img.getWidth(null)/2, yPos + img.getHeight(null)/2);
		g2d.transform(trans);
		g2d.drawImage(img, xPos, yPos, null);
		g2d.dispose();
	}
	
	//gets a string's width given the current font and such
	public static int getStringWidth(String message, Graphics2D g)
	{
		return (int) g.getFont().getStringBounds(message, g.getFontRenderContext()).getWidth();
	}

	//gets a string's height given the current font and such
	public static int getStringHeight(Graphics2D g)
	{
		return g.getFontMetrics().getHeight();
	}
	
	//gets strings dimensions
	public static Dimension getStringDemensions(String message, Graphics2D g)
	{
		Rectangle rect = g.getFont().getStringBounds(message, g.getFontRenderContext()).getBounds();
		return new Dimension((int)rect.getWidth(), (int)rect.getHeight());
	}
	
	//centers a string graphically on the x axis (given coord will be center)
	public static void drawXCenteredString(String message, double x, double y, Graphics2D g)
	{
		g.drawString(message, (float) (x-getStringWidth(message, g)/2.0), (float)y);
	} 
	
	//draw a string centered on the Y axis (given coord will be in the middle of the height of the string)
	public static void drawYCenteredString(String message, double x, double y, Graphics2D g)
	{
		g.drawString(message, (float) x, (float) (y+getStringHeight(g)/2.0));
	}
	
	//the given coords will be the dead center of the string
	public static void drawCenteredString(String message, double x, double y, Graphics2D g)
	{
		g.drawString(message, (float) (x-getStringWidth(message, g)/2.0), (float) (y+getStringHeight(g)/2)-getStringHeight(g)/4);
	}
	
	public static ArrayList<String> parseStringToLines(String str, final int lineSpace, final int lineLimit)
	{
		ArrayList<String> lines = new ArrayList<>();
		lines.add("");
		
		if(str.length() > lineLimit || !str.contains(" "))
		{
			ArrayList<String> allWords = new ArrayList<>(Arrays.asList(str.split(" "))); 
			for(final String word : allWords)
			{ 
				String currentLine = lines.get(lines.size() - 1);
				lines.remove(lines.size() - 1);
				if(currentLine.length() + word.length() < lineLimit && !word.contains("\n"))
				{
					currentLine += word + " ";
					lines.add(currentLine);
				} else {
					lines.add(currentLine);
					lines.add(word + " ");
				}
			}
			return lines;
		} else {
			ArrayList<String> list = new ArrayList<>();
			list.add(str);
			return list;
			}
	}
	
	public static void drawMultiLineString(Graphics2D g, final String str, int x, int y, int lineSpace, int lineLimit)
	{
		ArrayList<String> lines = parseStringToLines(str, lineSpace, lineLimit);
		
		for(String line : lines)
			g.drawString(line, x, y + Util.getStringHeight(g)*lines.indexOf(line) + lineSpace * lines.indexOf(line));
	}
	
	public static int getHeightOfMultiLineString(Graphics2D g, String str, int lineSpace, int lineLimit)
	{
		ArrayList<String> lines = parseStringToLines(str, lineSpace, lineLimit);
		return Util.getStringHeight(g)*(lines.size()-1) + lineSpace * (lines.size()-1);
	}
	
	//returns "a" or "an"
	public static String getIndefiniteArticleFor(String str)
	{
		return "aeiou".contains("" + str.toLowerCase().charAt(0)) ? "an" : "a";
	}
	
	//examples: apple -> an apple : dog -> a dog
	public static String getStringWithIndefiniteAcrticle(String str)
	{
		return getIndefiniteArticleFor(str) + " " + str;
	}
	
	//converts a tilepos to a position to draw a graphic
	public static Point tilePosToScreenPos(Game game, Point tilePos)
	{
		return new Point(tilePos.x * Tiles.TILE_SIZE - (int)game.getCamera().getXOffset(), tilePos.y * Tiles.TILE_SIZE - (int) game.getCamera().getYOffset());
	}
	//world position
	public static Point tilePosToWorldPos(Point tilePos)
	{
		return new Point(tilePos.x * Tiles.TILE_SIZE, tilePos.y * Tiles.TILE_SIZE);
	}
	
	//draws a see through image
	public static void drawTranslucentImage(Game game, Graphics2D g, Image img, Point tilePos, int opacity)
	{
		Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity/100f));
		Point screenPos = tilePosToScreenPos(game, tilePos);
        g2.drawImage(img, screenPos.x, screenPos.y, null);		
		g2.dispose();
	}
	
	//whether the tile is within the player aoe
	public static boolean posWithinPlayerAOE(Game game, Point tilePos, int radius)
	{
		return Util.getSquareTilePositions(game, game.getPlayer().getTilePosition(), radius).contains(tilePos);
	}
	
	//if obj is within the tile
	public static boolean objectTouchingTilePos(Game game, GameObject obj, Point tilePos)
	{
		Point screenPos = tilePosToScreenPos(game, tilePos);
		return obj.getBoundsWithCamera().intersects(new Rectangle2D.Float(screenPos.x, screenPos.y, Tiles.TILE_SIZE, Tiles.TILE_SIZE));
	}
	
	public static Point point2DToPoint(Point2D point)
	{
		return new Point((int)point.getX(), (int) point.getY());
	}
}