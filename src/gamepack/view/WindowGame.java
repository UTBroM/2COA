package gamepack.view;

import gamepack.data.drawable.Grid;
import gamepack.manager.GameSaver;
import gamepack.manager.TileMatrixManager;
import gamepack.utility.Direction;
import gamepack.utility.GameState;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class WindowGame extends BasicGame
{
	//		ATTRIBUTES
	private final int windowSizeX;
	private final int windowSizeY;
	
	private GameState state;
	
	private Grid grid;
	private TileMatrixManager gameManager;
	private int gameFPS;
	private int numberOfFrameWithMovement; //in order to generate a new tile only if there is movement
	private GameSaver gSave;
	
	private  Color transparentbg;
	private  Font font;
	private TrueTypeFont ttf;
	private Image explosionImage1;
	private Image explosionImage2;
	private Animation explosionAnimation = new Animation();

	private float tileSpeedMultiplicator = 1;
	
	//INTERFACE
	private String strWin1 = new String("Congratulation !");
	private String strWin2 = new String("You won with a score of ");
	private String strLose1 = new String("You lost ...");
	private String strLose2 = new String("But try again and beat your score of ");
	private Color prevColor;
	
	
	//		METHODS
	public WindowGame()
	{
		//Parent Constructor
		super("2C0A");
		
		//Attributes initialization
		windowSizeX = 800;
		windowSizeY = 600;
		state = GameState.Ongoing;
		gameFPS = 100;
		numberOfFrameWithMovement = 0;
		
		//Object initialization
		grid = new Grid(windowSizeX, windowSizeY);
		gSave = new GameSaver("save.txt", "score.txt");
		
		transparentbg = new Color(193, 184, 176, 136);
		font = new Font("Times New Roman", Font.BOLD, 32);
		
		//Matrix Manager Initialization
		generateGameManager();
		
	}
	
	//Slick2D method which start when the game container start
	public void init(GameContainer container) throws SlickException
	{
		//Initialisation of animation when BOOOOM !
		explosionImage1 = new Image ("boom.gif");
		explosionImage2 = new Image ("boom2.png");
		explosionAnimation.addFrame(explosionImage1, 200);
		explosionAnimation.addFrame(explosionImage2, 200);
		explosionAnimation.setLooping(false);
		
		//Graphic aspect
		ttf = new TrueTypeFont(font, true);
		container.getGraphics().setBackground(new Color(193, 184, 176)); 
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
		
	private void generateGameManager()
	{
		//If there is no save
		if (!gSave.areFilesAvailable())
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
	
	//check if the FPS is correct (the default function to manage FPS is not really good)
	public void refreshFPS(int fps)
	{
		if (fps == 0)
			fps = 60;
		
		gameFPS = fps;
	}
	
	//Draw the score
	public void drawRightPannel(Graphics g)
	{
		int commandsTopPositon = 20;
		int scoreTopPositon = commandsTopPositon+15*8;
		g.setColor(Color.white);
		g.drawString("Options :", grid.getRightPosition(), commandsTopPositon);
		g.drawString("F1 : Save game",grid.getRightPosition(), commandsTopPositon+15*1);
		g.drawString("F2 : load game",grid.getRightPosition(), commandsTopPositon+15*2);
		g.drawString("F3 : New game", grid.getRightPosition(), commandsTopPositon+15*3);
		g.drawString("F4 : Slow Motion", grid.getRightPosition(), commandsTopPositon+15*4);
		g.drawString("Back : Rewind", grid.getRightPosition(), commandsTopPositon+15*5);
		
		g.setColor(Color.white);
		g.drawString("Score : " + this.gameManager.getScore(), grid.getRightPosition(), scoreTopPositon);
		
	}
	
	public void drawWin(Graphics g)
	{
		grid.beDrawn(g);
		gameManager.getTileMatrix().beDrawn(g);
		prevColor = g.getColor();
		g.setColor(transparentbg);
		g.fillRect(0, 0, windowSizeX, windowSizeY); // Draw a rectangle to 'hide' the background
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strWin1))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strWin1)*1.5), strWin1, Color.black);
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strWin2))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strWin2)+ttf.getHeight(strWin1)), strWin2 + this.gameManager.getScore(), Color.black);
		g.setColor(prevColor);
	}
	
	public void drawLose(Graphics g)
	{
		grid.beDrawn(g);
		gameManager.getNextTileMatrix().beDrawn(g);
		prevColor = g.getColor();
		g.setColor(transparentbg);
		g.fillRect(0, 0, windowSizeX, windowSizeY); // Draw a rectangle to 'hide' the background
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strLose1))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strLose1)*1.5), strLose1, Color.black);
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strLose2))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strLose2)+ttf.getHeight(strLose1)), strLose2 + this.gameManager.getScore()+ " !", Color.black);
		g.setColor(prevColor);
	}
	
	@Override
	public boolean closeRequested()
	{
		// Save the game when closing
		if(state != GameState.Lose && state != GameState.Win )
			gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore());
		return true;
	}

	//when a key is pressed
	public void keyPressed(int key, char c)
	{
		//if we press a command
		if (key == Input.KEY_F1) //F1 save the game
		{
			if(state != GameState.Win && state != GameState.Lose)
				gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore());
		}
		else if (key == Input.KEY_F2) //F2 load the game
		{
			state = GameState.Ongoing;
			gameManager = new TileMatrixManager(grid.getRectangles(), gSave.getSavedTileList(), gSave.getScore());
		}
		else if (key == Input.KEY_F3) //F3 make a new game
		{
			state = GameState.Ongoing;
			gSave.deleteSave();
			generateGameManager();
		}
		else if (key == Input.KEY_F4 && tileSpeedMultiplicator == 1) //F4 Slow Motion (for the next movement)
		{
			tileSpeedMultiplicator /= 10.0;
		}
		else if (key == Input.KEY_BACK) //Undo the previous movement
		{
			gameManager.undo();
		}
		
		//If it wasn't a command
		else
		{
			//If we are waiting for an event or if the movement has been done
			if (state == GameState.Ongoing || state == GameState.DoneMoving)
			{
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
				
				//If we have press a key for a movement
				if (directionPressed != Direction.None)
				{
					state = GameState.Moving;
					numberOfFrameWithMovement = 0; //set the number of frame with movement at 0
					explosionAnimation.restart();
					gameManager.initMovement(directionPressed); //launch the movement for all tiles
					
				}
			}
			//If we press a key while there is a movement, we accelerate the movement
			else if(state != GameState.Win && state != GameState.Lose)
				gameManager.manageMovement(gameFPS,20);
		}
	}
	
	//--------------- Default function of Slick2D -----------
	//Refresh the screen
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		//Draw the grid
		if (state == GameState.Win)
		{
			this.drawWin(g);
		}
		else if (state == GameState.Lose)
		{
			this.drawLose(g);
		}
		else
		{
			grid.beDrawn(g);
			
			//Draw the next tile matrix if movements have been done, otherwise draw the tile matrix
			if (state == GameState.Ongoing || state == GameState.DoneMoving)
			{
				gameManager.getNextTileMatrix().beDrawn(g);
				if (!explosionAnimation.isStopped())
				{
					for (int i = 0; i < gameManager.getExplosionPositions().size(); i++) 
					{
						//Central Position
						final int x = gameManager.getExplosionPositions().get(i).getX() - explosionAnimation.getWidth()/2;
						final int y = gameManager.getExplosionPositions().get(i).getY() - explosionAnimation.getHeight()/2;
						explosionAnimation.draw(x, y);
					}
				}
			}
			else
				gameManager.getTileMatrix().beDrawn(g);
			
			//Draw the score
			this.drawRightPannel(g);
		}

	}
	
	//Do computation
	public void update(GameContainer gc, int delta) throws SlickException
	{
		//if we're not waiting for an event
		
		if (state != GameState.Ongoing)
		{
			//Once tiles have finished their movement
			if (state == GameState.DoneMoving)
			{
				//If there was a movement (so if each tiles are not stuck in a corner for example)
				//(one movement is always done to check if the tile are arrived and to reset their arrived point)
				if (numberOfFrameWithMovement != 0)
				{
					//We generate a new tile
					gameManager.generateNewTile();
					gameManager.refreshBomb();
					state = gameManager.isOver();
					tileSpeedMultiplicator = 1;
				}
			}
			//if a movement has been initialized
			if (state == GameState.Moving)
			{
				if (!gameManager.manageMovement(gameFPS,tileSpeedMultiplicator)) //if there is no movement (all tiles have their arrivedPoint equal to null)
					state = GameState.DoneMoving;
				else
					//if there is a movement
					numberOfFrameWithMovement++; //we notice that there was a movement
				gameManager.manageFusion();
			}
			refreshFPS(gc.getFPS());
		}
		
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