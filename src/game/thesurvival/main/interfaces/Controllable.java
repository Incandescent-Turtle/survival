package game.thesurvival.main.interfaces;

//for GameObjects that need controls passed to them
public interface Controllable 
{
	public abstract void keyPressed(final int key);
	public abstract void keyReleased(final int key);
}