package game.thesurvival.main.gfx;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
//to load images
public class ImageLoader 
{
	public BufferedImage loadImage(String path)
	{
		String newPath = path.replaceAll(" ", "_");
		try {
			return ImageIO.read(getClass().getResource("/" + newPath.replaceAll(" ", "_") + ".png"));
		} catch (Exception e) {
			System.out.println("Error! Couldn't find texture at location " + "res/" + newPath + ".png" + " Ensure that the png file is named correctly and is placed within the correct folder.");
		}
		return null;
	}	
}