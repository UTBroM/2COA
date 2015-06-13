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
		windowSizeX  = 800;
		windowSizeY = 600;
		state = 0;
		gameFPS = 100;
		numberOfFrameWithMovement = 0;
		
		//Object initialization
		grid = new Grid(windowSizeX, windowSizeY);
		gSave = new GameSaver("save.txt", "score.txt");

		//Matrix Manager Initialization
		generateGameManager();
		
	}
	
	private void generateGameManager()
	{
		//If there is no save
		if(!gSave.areFilesAvailable())
		{
			gameManager = new TileMatrixManager(grid.getRectangles());
			
			//The game starts with the generation of new tiles
			gameManager.generateNewTile();
			gameManager.generateNewTile();
			
			gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore());
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
		if(state == 0 || state == 2)
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
			//if we press a key, we manage the movement and the fusions of tiles
			if(state == 2)
			{
				//If there was a movement 
				//(one movement is always done to check if the tile are arrived and to reset their arrived point)
				if(numberOfFrameWithMovement != 1)
				{
					gameManager.generateNewTile();
					gameManager.refreshBomb();
					state = 0;
				}
			}
			//if a movement has been initialized
			if (state == 1)
			{
				if(!gameManager.manageMovement(gameFPS))	//if there is no movement (all tiles have their arrivedPoint equal to null)
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
		//If we are waiting for an event or if the movement has been done
		if (state == 0 || state == 2)
		{
			state = 1;
			
			//if we press a direction
			Direction directionPressed = Direction.None;
			if (key == Input.KEY_LEFT || key == Input.KEY_Q)
				directionPressed = Direction.Left;
			else if (key == Input.KEY_RIGHT || key == Input.KEY_D)
				directionPressed = Direction.Right;
			else if (key == Input.KEY_DOWN || key == Input.KEY_S)
				directionPressed = Direction.Down;
			else if (key == Input.KEY_UP || key == Input.KEY_Z)
				directionPressed = Direction.Up;
			//if we press a command
			else 
			{
				state=0; 
				if (key == Input.KEY_F1)	//F1 save the game
					gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore());
				else if (key == Input.KEY_F2)	//F2 load the game
					gameManager = new TileMatrixManager(grid.getRectangles(), gSave.getSavedTileList(), gSave.getScore());
				else if (key == Input.KEY_F3)	//F3 make a new game
				{
					gSave.deleteSave();
					generateGameManager();
				}
			}
			
			
			//If we are saving, we do not generate tile
			if(state == 1)
			{
				numberOfFrameWithMovement = 0;	//set the number of frame with movement at 0
				gameManager.initMovement(directionPressed);	//launch the movement for all tiles
				
			}
		}
		//If we press a key while there is a movement, we accelerate the movement
		else
			gameManager.manageMovement(1);
		
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