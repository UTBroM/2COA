package gamepack.view;

import gamepack.data.Player;
import gamepack.data.drawable.Grid;
import gamepack.data.drawable.TextArea;
import gamepack.data.drawable.Tile;
import gamepack.manager.GameSaver;
import gamepack.manager.TileMatrixManager;
import gamepack.utility.Direction;
import gamepack.utility.GameState;
import gamepack.utility.ProjectMethods;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;

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

//fait par le groupe
public class WindowGame extends BasicGame
{
	//		ATTRIBUTES
	private final int windowSizeX;
	private final int windowSizeY;
	
	private GameState state; //géré par FS
	
	private Grid grid;
	private TileMatrixManager gameManager;
	private int gameFPS;
	private int numberOfFrameWithMovement; //in order to generate a new tile only if there is movement
	private GameSaver gSave;

	
	private float tileSpeedMultiplicator = 1;
	
	//manage an "auto movement" mode (fait par JD)
	private char[] moveArray = {'q','s','d','z'};
	private boolean autoMove;
	private boolean strongAutoMove;
	
	//INTERFACE (fait par FS, VS, et JD)
	//text for win and lose (FS)
	private int commandPosition = 20;
	private String strWin1 = new String("Congratulation !");
	private String strWin2 = new String("You won with a score of ");
	private String strLose1 = new String("You lost ...");
	private String strLose2 = new String("But try again and beat your score of ");
	private Color prevColor;
	
	private  Color transparentbg;
	private  Font font;
	private TrueTypeFont ttf;
	
	//images for explosion (VS)
	private Image explosionImage1;
	private Image explosionImage2;
	private Image explosionImage3;
	private Image explosionImage4;
	private Animation explosionAnimation;
	//highscore (JD)
	private TextArea pseudoEntry;
	private ArrayList<Player> players;
	private String playersString;
	private Player currentPlayer;
	
	
	//		METHODS
	//--------------- Constructor and Initialisation -----------
	public WindowGame() //fait par JD
	{

		
		//Parent Constructor
		super("2C0A");
		
		//Attributes initialization
		windowSizeX = 800;
		windowSizeY = 600;
		state = GameState.Ongoing;
		gameFPS = 100;
		numberOfFrameWithMovement = 0;
		autoMove = false;
		strongAutoMove = false;
		
		//Object initialization
		final int sizeGrid = 4;
		grid = new Grid(windowSizeX, windowSizeY, sizeGrid);
		gSave = new GameSaver("save.txt", "score.txt","highscores.txt");
		players = gSave.getHighscores();
		if(players == null)
			players = new ArrayList<Player>();
		updatePlayerString(5);
		currentPlayer = null;
		
		transparentbg = new Color(193, 184, 176, 136);
		font = new Font("Times New Roman", Font.BOLD, 32);
		
		//Matrix Manager Initialization
		generateGameManager();
		
		
	}
	
	//Slick2D method which start when the game container start
	public void init(GameContainer container) throws SlickException //fait par VS, JD et FS
	{
		
		//Initialisation of animation when BOOOOM !
		explosionAnimation  = new Animation();
		explosionImage1 = new Image ("boom1.png");
		explosionImage2 = new Image ("boom2.png");
		explosionImage3 = new Image ("boom3.png");
		explosionImage4 = new Image ("boom4.png");
		explosionAnimation.addFrame(explosionImage1, 25);
		explosionAnimation.addFrame(explosionImage2, 35);
		explosionAnimation.addFrame(explosionImage3, 35);
		explosionAnimation.addFrame(explosionImage4, 150);
		explosionAnimation.setLooping(false);
		
		//Graphic aspect
		ttf = new TrueTypeFont(font, true);
		container.getGraphics().setBackground(new Color(193, 184, 176)); 
		
		//Input management on TextArea
		pseudoEntry = new TextArea(20,20,150,20);
		container.getInput().addKeyListener(pseudoEntry);
		container.getInput().addMouseListener(pseudoEntry);
		pseudoEntry.setPosition(grid.getRightPosition(), getNextCommandPosition(0));
	}
	
	
	
	

	//--------------- Others -----------
	//Generate a new TileMatrixManager	(from save or not depending on the saves)
	private void generateGameManager() //fait par JD
	{
		//If there is no save
		if (!gSave.areFilesAvailable())
		{
			gameManager = new TileMatrixManager(grid.getRectangles());
			
			//The game starts with the generation of new tiles
			gameManager.generateNewTile();
			gameManager.generateNewTile();

			gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore(), "");
		}
		//otherwise, we load the save if the grid has the good size
		else
		{
			ArrayList<Tile> list =  gSave.getSavedTileList();
			if(list.size() == grid.getRectangles().size())
				gameManager = new TileMatrixManager(grid.getRectangles(), list, gSave.getScore());
			else 
			{
				gSave.deleteSave();
				generateGameManager();
			}
		}
	}
	
	//check if the FPS is correct (the default function to manage FPS is not really good)
	public void refreshFPS(int fps)
	{
		if (fps == 0)
			fps = 60;
		
		gameFPS = fps;
	}
	
	
	
	//Execute if the window is closed (overriden)
	@Override
	public boolean closeRequested() //fait par JD et FS
	{
		// Save the game when closing
		if(state != GameState.Lose && state != GameState.Win )
			gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore(), "");
		else //if we lose/win & then leave: register score, delete save
		{
			currentPlayer = new Player(pseudoEntry.getText(), gameManager.getScore());
			if(currentPlayer.getName() != "")
			{
				players.add(currentPlayer);
				updatePlayerString(5);
				gSave.save(gameManager.getNextTileMatrix(), currentPlayer);
				gSave.deleteSave();
			}
		}
		return true;
	}
	
	//when a key is pressed
	public void keyPressed(int key, char c) //fait par JD
	{
		//if we're not entering a pseudo
		if(!pseudoEntry.isEnteringText())
		{
			
			//if we press a command
			if (key == Input.KEY_F1) //F1 save the game
			{
				if(state != GameState.Win && state != GameState.Lose)
				{
					gSave.save(gameManager.getNextTileMatrix(), gameManager.getScore(), "");
				}
			}
			else if (key == Input.KEY_F2) //F2 load the game
			{
				state = GameState.Ongoing;
				gameManager = new TileMatrixManager(grid.getRectangles(), gSave.getSavedTileList(), gSave.getScore());
			}
			else if (key == Input.KEY_F3) //F3 make a new game (and save the score of the precedent if lose/win)
			{
				restartGame();
			}
			else if (key == Input.KEY_F4 && tileSpeedMultiplicator == 1) //F4 Slow Motion (for the next movement)
			{
				tileSpeedMultiplicator /= 10.0;
			}
			else if (key == Input.KEY_BACK) //Undo the previous movement
			{
				gameManager.undo();
			}
			else if (key == Input.KEY_F5) //Auto random movements in the game
			{
				autoMove = !autoMove;
				strongAutoMove = false;
			}
			else if (key == Input.KEY_F6) //Auto random movements in the game until win
			{
				strongAutoMove = !strongAutoMove;
				autoMove = false;
			}
			
			
			//If it wasn't a command
			else
			{
				//If we are waiting for an event or if the movement has been done
				if (state == GameState.Ongoing || state == GameState.DoneMoving)
				{
					//if we press a direction
					Direction directionPressed = Direction.None;
					if (key == Input.KEY_LEFT || Character.toLowerCase(c) == 'q')
						directionPressed = Direction.Left;
					else if (key == Input.KEY_RIGHT || Character.toLowerCase(c) == 'd')
						directionPressed = Direction.Right;
					else if (key == Input.KEY_DOWN || Character.toLowerCase(c) == 's')
						directionPressed = Direction.Down;
					else if (key == Input.KEY_UP || Character.toLowerCase(c) == 'z')
						directionPressed = Direction.Up;
					
					//If we have press a key for a movement
					if (directionPressed != Direction.None)
					{
						state = GameState.Moving;
						numberOfFrameWithMovement = 0; //set the number of frame with movement at 0
						explosionAnimation.restart();
						gameManager.getExplosionPositions().clear();
						gameManager.initMovement(directionPressed); //launch the movement for all tiles
						
					}
				}
				//If we press a key while there is a movement, we accelerate the movement
				else if(state != GameState.Win && state != GameState.Lose)
					gameManager.manageMovement(gameFPS,20);
			}
		}
	}
	
	//restart the game after losing/winning
	public void restartGame() //fait par JD
	{
		if(state == GameState.Win || state == GameState.Lose)
		{
			currentPlayer = new Player(pseudoEntry.getText(), gameManager.getScore());
			if(currentPlayer.getName() != "")
			{
				players.add(currentPlayer);
				updatePlayerString(5);
				gSave.save(gameManager.getNextTileMatrix(), currentPlayer);
			}
		}
			
		state = GameState.Ongoing;
		gSave.deleteSave();
		generateGameManager();
	}

	
	
	
	
	//--------------- GUI (draw) -----------
	//Draw the score
	public void drawInterface(Graphics g) //fait par FS et JD
	{
		//Text Entry
		pseudoEntry.beDrawn(g);
		
		//Right Pannel
		//space
		g.setColor(Color.white);
		g.drawString("___________", grid.getRightPosition(), getNextCommandPosition(3));
		
		//commands
		g.setColor(Color.white);
		g.drawString("Options :", grid.getRightPosition(), getNextCommandPosition(2));
		g.drawString("F1 : Save game",grid.getRightPosition(), getNextCommandPosition(1));
		g.drawString("F2 : load game",grid.getRightPosition(), getNextCommandPosition(1));
		g.drawString("F3 : New game", grid.getRightPosition(), getNextCommandPosition(1));
		g.drawString("F4 : Slow Motion", grid.getRightPosition(),getNextCommandPosition(1));
		g.drawString("F5 : Auto Movements", grid.getRightPosition(),getNextCommandPosition(1));
		g.drawString("F6 : Auto Movements+", grid.getRightPosition(),getNextCommandPosition(1));
		g.drawString("Back : Rewind", grid.getRightPosition(), getNextCommandPosition(1));
		
		//score
		g.setColor(Color.white);
		g.drawString("Score : " + this.gameManager.getScore(), grid.getRightPosition(), getNextCommandPosition(3));
		
		//space
		g.setColor(Color.white);
		g.drawString("___________", grid.getRightPosition(), getNextCommandPosition(3));
		
		//top 5
		g.setColor(Color.white);
		g.drawString(playersString, grid.getRightPosition(), getNextCommandPosition(3));
		
		
		//reset positions of elements
		commandPosition = 0;
		commandPosition = pseudoEntry.getY()+getNextCommandPosition(1);
	}
	
	//Draw the winning situation
	public void drawWin(Graphics g) //fait par FS
	{
		grid.beDrawn(g);
		gameManager.getNextTileMatrix().beDrawn(g);
		prevColor = g.getColor();
		g.setColor(transparentbg);
		g.fillRect(0, 0, windowSizeX, windowSizeY); // Draw a rectangle to 'hide' the background
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strWin1))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strWin1)*1.5), strWin1, Color.black);
		ttf.drawString((float)(this.windowSizeX - ttf.getWidth(strWin2))/2, (float)(this.windowSizeY/2 - ttf.getHeight(strWin2)+ttf.getHeight(strWin1)), strWin2 + this.gameManager.getScore(), Color.black);
		g.setColor(prevColor);
	}

	//Draw the losing situation
	public void drawLose(Graphics g) //fait par FS
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
	
	
	
	
	
	//--------------- GUI (computation) -----------
	//To get the next position on the right pannel
	public int getNextCommandPosition(int jump) //fait par JD
	{
		commandPosition += jump*15;
		return commandPosition;
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
	

	//update the string to show the top n
	private void updatePlayerString(int n) //fait par JD
	{
		playersString = "";

		if(players != null && players.size() != 0)
		{
			Collections.sort(players);
			for(int i = 0; i < n && i < players.size(); i++)	//TOP 5
				playersString += ""+players.get(i) + '\n';
		}
	}

	
	
	
	
	//--------------- Default function of Slick2D -----------
	//Refresh the screen
	public void render(GameContainer container, Graphics g) throws SlickException //fait par JD, FS et VS
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
				
			}
			else
				gameManager.getTileMatrix().beDrawn(g);
			
			//Draw the score
			this.drawInterface(g);
			
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

	}
	
	//Do computation
	public void update(GameContainer gc, int delta) throws SlickException //fait par JD
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
		//automatic movement 
		if (state == GameState.Ongoing || (state == GameState.DoneMoving && numberOfFrameWithMovement == 0))
		{
			if(autoMove || strongAutoMove)
			{
				char c = moveArray[ProjectMethods.randInt(0, 3)];
				keyPressed(0, c);
				if(strongAutoMove)
					tileSpeedMultiplicator = 300;
			}
		}
		//if we don't care about losing or winning
		if (strongAutoMove) //F3 make a new game (and save the score of the precedent if lose/win)
		{
			if(state == GameState.Win || state == GameState.Lose)
				restartGame();
		}
		
	}
	
	
	
	
	
	
	//--------------- Main -----------
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