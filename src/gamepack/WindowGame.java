package gamepack;

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
						 * 0 = attente d'input 
						 * 1 = en cours de déplacement (pas d'input possible)
						 * 2 = Fin du déplacement, génération des nouveaux tile
						 */
	
	private Grid grid;
	private TileListManager GameManager;
	
	//		METHODS
	public WindowGame()
	{
		//Parent Constructor
		super("2C0A");
		
		
		//Attributes initialization
		windowSizeX  = 800;
		windowSizeY = 600;
		grid = new Grid(windowSizeX, windowSizeY);
		//    Get the new size of the size depending on the resolution (80% of the grid rectangle size)
		int tileSize = (int) (1 * grid.squareSize());
		GameManager = new TileListManager(tileSize, grid.getRectangles());
		state = 0;
		
		
		//The game starts with the generation of new tiles
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
		//Once the movement is done, we generate new tiles
		if (state == 2)
		{
			GameManager.generateNewTile();
			state = 0;
		}
		
		//if we press a touch, we manage the movement and the fusions of tiles
		if (state == 1)
		{
			if(!GameManager.manageMovement(gc.getFPS()))	//if there is no movement
				state = 2;
			GameManager.manageFusion();
		}
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
		
	}
	
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer appgc;
		WindowGame wGame = new WindowGame();
		appgc = new AppGameContainer(wGame);
		appgc.setDisplayMode(wGame.getWindowSizeX(), wGame.getWindowSizeY(), false);
		appgc.setShowFPS(false);
		appgc.start();
	}
}