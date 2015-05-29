import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class WindowGame extends BasicGame {
	private GameContainer container;
	private final int windowSizeX = 800;
	private final int windowSizeY = 800;

	private int state; /* 0 = attente d'input 
					    1 = en cours de déplacement (pas d'input possible)*/
	private TileList board;
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
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException {
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}

	public static void main(String[] args) throws SlickException 
	{
		WindowGame wGame = new WindowGame() ;
		new AppGameContainer(wGame, wGame.getWindowSizeX(), wGame.getWindowSizeY(), false).start();
	}
}