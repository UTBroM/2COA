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
	private int direction;
	private TileList board;

	public WindowGame() {
		super("Lesson 1 :: WindowGame");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		rc = new Rectangle(60,60,140,140);
		board = new TileList(4, container.getHeight(), container.getWidth());
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		g.draw(rc);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (state == 0) {
			//Attends une entrée utilisateur
			if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
				state = 1;
				direction = 1;
				rc.setX(rc.getX() + 1);//
			}
			/*else */if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
				state = 1;
				direction = -1;
				rc.setX(rc.getX() - 1);//
			}
			/*else */if (gc.getInput().isKeyDown(Input.KEY_UP)) {
				state = 1;
				direction = 2;
				rc.setY(rc.getY() - 1);//
			}
			/*else */if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
				state = 1;
				direction = -2;
				rc.setY(rc.getY() + 1);//
			}
		}
		if (state == 1) {
			// Redéfinit les bonnes coordonnées pour chaque point
			if(!board.move(direction))//Déplacement terminé
				state = 0;
		}
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