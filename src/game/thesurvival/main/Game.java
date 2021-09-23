package game.thesurvival.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.Map;

import javax.swing.JFrame;

import game.thesurvival.main.gfx.PopUpManager;
import game.thesurvival.main.gfx.Textures;
import game.thesurvival.main.gui.PlayerHUD;
import game.thesurvival.main.handlers.CollisionHandler;
import game.thesurvival.main.handlers.GameObjectHandler;
import game.thesurvival.main.handlers.KeyHandler;
import game.thesurvival.main.handlers.MouseHandler;
import game.thesurvival.main.playerstuff.Player;
import game.thesurvival.main.tilestuff.Tiles;
import game.thesurvival.main.worldstuff.Camera;
import game.thesurvival.main.worldstuff.World;

//Main
public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	//Game world
	private final World world;
	//to handle all the game objects
	private final GameObjectHandler objectHandler;
	//for scrolling
	private final Camera camera;
	//gameobject - main character
	private final Player player;
	//player HUD (health, points, food, water, ammo, inventory)
	private final PlayerHUD playerHUD;
	//to check gameobject collisions
	private CollisionHandler collisionHandler;
	//whether to redner buffered images or not
	private final boolean isInHighDetail = true;
	private Thread thread;
	private final JFrame frame;
	private boolean running;
	
	private final KeyHandler keyHandler;
	
	//for displaying when materials are harvested
	private final PopUpManager popUpManager;
		
	public static int width, height;
	
	public Game() 
	{
		//creates JFrame
		frame = newWindow();
		//initilizing stuff
		camera = new Camera(this);
		objectHandler = new GameObjectHandler();
		keyHandler = new KeyHandler(this);
		world = new World(this);
		objectHandler.addObject(player = new Player(this));
		playerHUD = new PlayerHUD(this);
		collisionHandler = new CollisionHandler(this);
		MouseHandler mouse = new MouseHandler();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addKeyListener(keyHandler);
		world.loadWorld(Textures.WORLD);
		frame.setVisible(true);
		popUpManager = new PopUpManager();
		//starts game
		start();
	}
	
	//every tick
	private void tick()
	{
		width = getWidth();
		height = getHeight();
		world.tick();
		collisionHandler.checkCollisions();
		popUpManager.tick();
	}
	
	//every render tick
	public void render() 
	{
		//creates a buffer strategy to avoid bad rendering
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null)
		{
			this.createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
		if (desktopHints != null) g.setRenderingHints(desktopHints);
		g.setColor(Tiles.STONE.getColor());
		g.fillRect(0, 0, width, height);
		//renders the world
		world.render(g);
		//renders playerhud (water and food lvls, health, points, inventory()
		playerHUD.render(g);
		g.setFont(new Font("ariel", 1, 20));
		popUpManager.render(g);
		//g.drawString(MouseHandler.mouseX + " " + MouseHandler.mouseY, 100, 100);
		//refreshes the graphics object
		g.dispose();
		bs.show();
		
		if(keyHandler.changeFullScreen)
		{
			JFrame frame = getFrame();
			GraphicsDevice fullscreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			
			frame.dispose();

		    if (frame.isUndecorated()) {
		      fullscreenDevice.setFullScreenWindow(null);
		      frame.setUndecorated(false);
		    } else {
		      frame.setUndecorated(true);
		      fullscreenDevice.setFullScreenWindow(frame);
		    }

		    frame.setVisible(true);
		    frame.repaint();
		    keyHandler.changeFullScreen = !keyHandler.changeFullScreen;
			requestFocus();
		}
	}
	
	public void gameOver() 
	{
		
	}
	
	//called whenever the game is exited
	public void terminate()
	{
		System.exit(0);
	}
	
	public void reset()
	{
		objectHandler.reset();
		world.reset();
	}
	
	//JFrame helper
	private JFrame newWindow()
	{
		JFrame frame = new JFrame("The Survival");
		Dimension dim = new Dimension(800, 600);
		frame.setResizable(true);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(false);
		frame.add(this);
		frame.setVisible(false);
		width = dim.width;
		height = dim.height;
		return frame;
	}
	
	//start thread
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	//stop thread
	public synchronized void stop()
	{
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//run game
	@Override
	public void run() 
	{
		//game loop - ticks and renders everything
		setFocusable(true);
		requestFocus();
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;
	        while(delta >=1)
            {
                tick();
                delta--;
            }
            if(running) render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
       stop();
	}
	
	//creates Game Object
	public static void main(String[] args) 
	{
		new Game();
	}
	
	/*
	 * Getters
	 */
	
	public GameObjectHandler getObjectHandler()
	{
		return objectHandler;
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public boolean isInHighDetial()
	{
		return isInHighDetail;
	}
	
	public KeyHandler getKeyHandler()
	{
		return keyHandler;
	}
	
	public PopUpManager getPopUpManager()
	{
		return popUpManager;
	}
}