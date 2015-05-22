import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class WindowGame extends BasicGame {
    private GameContainer container;
    
    private Rectangle carre;
    private Color carreCouleur;

	public WindowGame() {
        super("Lesson 1 :: WindowGame");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        carre = new Rectangle(200, 100, 100, 100);
        carreCouleur = new Color(255, 0, 0);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
    	Color background = new Color(255, 128, 0);
    	g.setBackground(background);
    	g.setColor(carreCouleur);
    	g.fill(carre);
    	g.setColor(Color.white);
    	g.drawString("2048", carre.getCenterX()-20, carre.getCenterY()-10);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }
    
    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_ESCAPE == key) {
            container.exit();
        }
    }
    
    public static void main(String[] args) throws SlickException {
        new AppGameContainer(new WindowGame(), 640, 480, false).start();
    }
}