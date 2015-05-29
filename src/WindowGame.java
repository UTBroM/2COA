import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class WindowGame extends BasicGame {
	private GameContainer container;
	private Rectangle rc;
	private int state; // 0 = attente d'input
						// 1 = en cours de déplacement (pas d'imput possible)
	private Direction direction;
	private TileList board;

	public WindowGame() {
		super("Lesson 1 :: WindowGame");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		rc = new Rectangle(60, 60, 140, 140);
		board = new TileList(4, container.getHeight(), container.getWidth());
		// container.setTargetFrameRate(60);
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		g.draw(rc);
		for (Tile[] tab : board.getTileList()) {
			for (Tile tile : tab) {
				g.draw(tile.getRectangle());
				if (tile.getValue() != 1)
					g.drawString(Integer.toString(tile.getValue()), tile.getRectangle().getCenterX() - 5, tile.getRectangle().getCenterY() - 5);
			}
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// if (state == 0) {
		// Attends une entrée utilisateur
		int deltaTime = 1000;
		int delaDist = (int) (1000 * board.getDistanceTile() / ((float) (gc.getFPS() * deltaTime)));
		if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
			System.out.println("KEY_RIGHT");
			// board.getTileList()[0][0].getRectangle().setX(board.getTileList()[0][0].getRectangle().getX()
			// + delaDist);//
			state = 1;
			direction = Direction.Right;
			rc.setX(rc.getX() + delaDist);//
		}
		if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
			// board.getTileList()[0][0].getRectangle().setX(board.getTileList()[0][0].getRectangle().getX()
			// - delaDist);//
			System.out.println("KEY_LEFT");
			state = 1;
			direction = Direction.Left;
			rc.setX(rc.getX() - delaDist);//
		}
		if (gc.getInput().isKeyDown(Input.KEY_UP)) {
			System.out.println("KEY_UP");
			state = 1;
			direction = Direction.Up;
			rc.setY(rc.getY() - delaDist);//
		}
		if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
			System.out.println("KEY_DOWN");
			state = 1;
			direction = Direction.Down;
			rc.setY(rc.getY() + delaDist);//
		}
		
		if(board.move(direction))//Si le déplacement est valide
		{
			board.addTile();
		}
		
		/*
		 * } if (state == 1) { // Redéfinit les bonnes coordonnées pour chaque
		 * point //if(!board.move(direction))//Déplacement terminé state = 0; }
		 */
		// Gérer lorsque l'on a appuyé sur une touche et que le jeu déplace les
		// tuiles
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}
	}

	public static void main(String[] args) throws SlickException {
		new AppGameContainer(new WindowGame(), 800, 800, false).start();
	}
}