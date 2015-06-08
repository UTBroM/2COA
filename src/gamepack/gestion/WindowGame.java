package gamepack.gestion;

import gamepack.data.Grid;
import gamepack.utily.Direction;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class WindowGame extends BasicGame
{
	//		ATTRIBUTES
	private GameContainer container;
	private final int windowSizeX;
	private final int windowSizeY;
	
	private int state; /*
						 * 0 = wait for input
						 * 1 = keep calm it's moving (input isn't possible)
						 * 2 = end of moving & generation of a new tile
						 */
	
	private Grid grid;
	private TileListManager GameManager;
	private int gameFPS;
	private int numberOfFrameWithMovement;		//in order to generate a new tile only if there is movement
	
	
	//		METHODS
	public WindowGame()
	{
		//Parent Constructor
		super("2C0A");
		
		
		
		//Attributes initialization
		numberOfFrameWithMovement = 0;
		windowSizeX  = 800;
		windowSizeY = 600;
		grid = new Grid(windowSizeX, windowSizeY);
		//    Get the new size of the size depending on the resolution (80% of the grid rectangle size)
		int tileSize = (int) (1 * grid.squareSize());
		GameManager = new TileListManager(tileSize, grid.getRectangles());
		state = 0;
		gameFPS = 100;
		
		//The game starts with the generation of new tiles
		GameManager.generateNewTile();
		GameManager.generateNewTile();
		
	}
	
	public int getWindowSizeX()
	{
		return windowSizeX;
	}
	
	public int getWindowSizeY()
	{
		return windowSizeY;
	}
	
	public void init(GameContainer container) throws SlickException
	{
		this.container = container;
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		grid.beDrawn(g);
		GameManager.getTileList().beDrawn(g);
		
	}
	
	public void update(GameContainer gc, int delta) throws SlickException
	{
		if(state != 0)
		{
			//Once the movement is done, we generate new tiles
			if (state == 2)
			{
				if(numberOfFrameWithMovement != 1)	//if there was a movement, we generate a new tile
					GameManager.generateNewTile();
				state = 0;
				numberOfFrameWithMovement = 0;
			}
			
			//if we press a touch, we manage the movement and the fusions of tiles
			if (state == 1)
			{
				if(!GameManager.manageMovement(gameFPS))	//if there is no movement
					state = 2;
				else 										//if there is a movement
					numberOfFrameWithMovement++;			//we notice that there was a movement
				GameManager.manageFusion();
			}
			refreshFPS(gc.getFPS());
		}
		
	}
	
	public void refreshFPS(int fps)
	{
		if(fps == 0)
			fps = 60;

		gameFPS = fps;
	}
	
	public void keyPressed(int key, char c)
	{
		//If we are waiting for an event
		if (state == 0)
		{
			state = 1;
			if (key == Input.KEY_LEFT)
				GameManager.initMovement(Direction.Left);
			else if (key == Input.KEY_RIGHT)
				GameManager.initMovement(Direction.Right);
			else if (key == Input.KEY_DOWN)
				GameManager.initMovement(Direction.Down);
			else if (key == Input.KEY_UP)
				GameManager.initMovement(Direction.Up);
			else
				state = 0; //if no interesting event were encoutered
		}
		else
			GameManager.manageMovement(1);
		
	}
	
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer appgc;
		WindowGame wGame = new WindowGame();
		appgc = new AppGameContainer(wGame);
		appgc.setDisplayMode(wGame.getWindowSizeX(), wGame.getWindowSizeY(), false);
		appgc.setShowFPS(false);
		appgc.setTargetFrameRate(100);
		appgc.start();
	}
}