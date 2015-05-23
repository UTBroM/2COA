import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class WindowGame extends BasicGame {
    private GameContainer container;
    
    private ArrayList<Rectangle> carres = new ArrayList<Rectangle>(9);
    private Color carreCouleur;

	public WindowGame() {
        super("2048");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        container.setVSync(true); //Synchronise les frames sur le rafraichissement l'écran
        for(int x=80;x<301;x+=110){
        	for(int y=80;y<301;y+=110){
        		carres.add(new Rectangle(x, y, 100, 100));
        	}
        }
        carreCouleur = new Color(255, 0, 0);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
    	Color background = new Color(255, 128, 0);
    	g.setBackground(background);
    	g.setColor(carreCouleur);
    	for(int i=0;i<carres.size();i++){
    		g.fill(carres.get(i));
    	}
    	g.setColor(Color.white);
    	g.drawString("2048", carres.get(0).getCenterX()-20, carres.get(0).getCenterY()-10);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    	Input input = container.getInput();
    	int speed = 200;
    	float distance = speed * ((float)delta/1000);
    	
        if (input.isKeyDown(Input.KEY_LEFT)) {
        	carres.get(0).setX(carres.get(0).getX() - distance);
        }
        if (input.isKeyDown(Input.KEY_RIGHT)) {
        	carres.get(0).setX(carres.get(0).getX() + distance);
        }
        if (input.isKeyDown(Input.KEY_UP)) {
        	carres.get(0).setY(carres.get(0).getY() - distance);
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
        	carres.get(0).setY(carres.get(0).getY() + distance);
        }
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