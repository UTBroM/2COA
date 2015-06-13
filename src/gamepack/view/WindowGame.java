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
		gSave = new GameSaver("save.txt", "score.txt");
		numberOfFrameWithMovement = 0;
		windowSizeX  = 800;
		windowSizeY = 600;
		grid = new Grid(windowSizeX, windowSizeY);
		//    Get the new size of the size depending on the resolution (80% of the grid rectangle size)
		state = 0;
		gameFPS = 100;


		//If there is no save
		if(!gSave.areFilesAvailable())
		{
			gameManager = new TileMatrixManager(grid.getRectangles());
			
			//The game starts with the generation of new tiles
			gameManager.generateNewTile();
			gameManager.generateNewTile();
		}
		//otherwise, we load the save
		else
		{
			gameManager = new TileMatrixManager(grid.getRectangles(), gSave.getSavedTileList(), gSave.getScore());
		}
		
	}
	
	//Slick2D method which start when the game container start
	public void init(GameContainer container) throws SlickException
	{
		this.container = container;
	}
	
	//Size methods for the container
	public int getWindowSizeX()
	{
		return windowSizeX;
	}

	//Size methods for the container
	public int getWindowSizeY()
	{
		return windowSizeY;
	}
	
	//Refresh the screen
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		//Draw the grid
		grid.beDrawn(g);
		
		//Draw the next tile matrix if movements have been done, otherwise draw the tile matrix
		if(state == 0)
			gameManager.getNextTileMatrix().beDrawn(g);
		else
			gameManager.getTileMatrix().beDrawn(g);
		
		//Draw the score
		this.drawScore(g);
		
	}
	
	//Do computation
	public void update(GameContainer gc, int delta) throws SlickException
	{
		//if we're not waiting for an event
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
	
	
	
	//check if the FPS is correct (the default function to manage FPS is not really good)
	public void refreshFPS(int fps)
	{
		if(fps == 0)
			fps = 60;

		gameFPS = fps;
	}
	
	//Draw the score
	public void drawScore(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString("score : " + this.gameManager.getScore(), container.getWidth() -150, 10);
		
	}
	
	
	//when a key is pressed
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
				state = 0; 
			
			//if no interesting event were encoutered, we generate a new Tile
			if(state != 0 && numberOfFrameWithMovement != 1)
			{
				//System.out.println(numberOfFrameWithMovement); // à corriger
				gameManager.generateNewTile();
				numberOfFrameWithMovement = 0;
			}
		}
		//If we press a touch while there is a movement, we accelerate the movement
		else
			gameManager.manageMovement(1);
		
	}
	
	//Register the game when we quit the game
	//maybe add a "do you want to save, yes no"
	public boolean closeRequested()
	{
		gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore());
		
		
		
		return true;
	}
	
	//Main methods, create the window
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer appgc;
		WindowGame wGame = new WindowGame();
		appgc = new AppGameContainer(wGame); //set our game in the container
		appgc.setDisplayMode(wGame.getWindowSizeX(), wGame.getWindowSizeY(), false); //the container & the game have the same dimensions
		appgc.setShowFPS(false); //don't show the FPS
		appgc.setTargetFrameRate(100); //default frame rate
		appgc.start(); //Launch the game
	}
}