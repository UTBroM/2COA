package gamepack;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class WindowGame extends BasicGame 
{
	//		ATTRIBUTES
	private GameContainer container;
	private final int windowSizeX = 800;
	private final int windowSizeY = 600;

	private int state; /* 	0 = attente d'input  
					    	1 = en cours de déplacement (pas d'input possible)*/
	private TileListManager game;
	
	private TileList board;
	private Tile yolo;
	private Grid grid = new Grid(windowSizeX, windowSizeY);
	
	//		METHODS
	public WindowGame() 
	{
		super("2C0A");
	}	
	public int getWindowSizeX() 
	{
		return windowSizeX;
	}
	public int getWindowSizeY() 
	{
		return windowSizeY;
	}

	@Override
	public void init(GameContainer container) throws SlickException 
	{
		this.container = container;
		// A modifier board = new TileList(4, container.getHeight(), container.getWidth());
		// container.setTargetFrameRate(60);
		yolo = new Tile(1,1);
		game = new TileListManager(/* ?????? */);
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException 
	{
		//yolo.beDrawn(g);
		grid.beDrawn(g);
		 
	}
	

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		System.out.println(key);
	}
	

	public static void main(String[] args) throws SlickException 
	{
		AppGameContainer appgc;
		WindowGame wGame = new WindowGame();
		appgc = new AppGameContainer(wGame);
		appgc.setDisplayMode(wGame.getWindowSizeX(), wGame.getWindowSizeY(), false);
		appgc.setShowFPS(false);
		appgc.setVSync(true);
		appgc.start();		
	}
}