package gamepack.view;

import gamepack.data.drawable.Grid;
import gamepack.data.drawable.TileMatrix;
import gamepack.manager.GameSaver;
import gamepack.manager.TileMatrixManager;
import gamepack.utility.Direction;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
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
	private TileMatrixManager gameManager;
	private int gameFPS;
	private int numberOfFrameWithMovement;		//in order to generate a new tile only if there is movement
	private GameSaver gSave;
	
	
	//		METHODS
	public WindowGame()
	{
		//Parent Constructor
		super("2C0A");
		
		
		
		//Attributes initialization
		gSave = new GameSaver("save.txt");
		numberOfFrameWithMovement = 0;
		windowSizeX  = 800;
		windowSizeY = 600;
		grid = new Grid(windowSizeX, windowSizeY);
		//    Get the new size of the size depending on the resolution (80% of the grid rectangle size)
		gameManager = new TileMatrixManager(grid.getRectangles());
		state = 0;
		gameFPS = 100;
		
		//The game starts with the generation of new tiles
		gameManager.generateNewTile();
		gameManager.generateNewTile();
		
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
		if(state == 0)
			gameManager.getNextTileMatrix().beDrawn(g);
		else
			gameManager.getTileMatrix().beDrawn(g);
		g.setColor(Color.white);
		g.drawString("score : " + this.gameManager.getScore(), container.getWidth() -150, 10);
		
	}
	
	public void update(GameContainer gc, int delta) throws SlickException
	{
		
		if(state != 0)
		{
			//Once the movement is done, we generate new tiles
			if (state == 2)
			{

				if(numberOfFrameWithMovement != 1)	//if there was a movement, we generate a new tile
				{
					gameManager.refreshBomb();
				}
				state = 0;
				numberOfFrameWithMovement = 0;
			}
			
			//if we press a touch, we manage the movement and the fusions of tiles
			if (state == 1)
			{
				if(!gameManager.manageMovement(gameFPS))	//if there is no movement
					state = 2;
				else 										//if there is a movement
					numberOfFrameWithMovement++;			//we notice that there was a movement
				gameManager.manageFusion();
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
				gameManager.initMovement(Direction.Left);
			else if (key == Input.KEY_RIGHT)
				gameManager.initMovement(Direction.Right);
			else if (key == Input.KEY_DOWN)
				gameManager.initMovement(Direction.Down);
			else if (key == Input.KEY_UP)
				gameManager.initMovement(Direction.Up);
			else
				state = 0; //if no interesting event were encoutered
			if(state != 0)
				gameManager.generateNewTile();
		}
		else
			gameManager.manageMovement(1);
		
	}
	
	public boolean closeRequested()
	{
		//gSave.save(gameManager.getTileList());
		
		
		
		return true;
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