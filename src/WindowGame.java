import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class WindowGame extends BasicGame {
    private GameContainer container;
    
    private ArrayList<Rectangle> carres = new ArrayList<Rectangle>(9);
    private Color carreCouleur;
    
    //Animation Eddy "WOW"
    private float x = 300, y = 300;
    private boolean appear=false;
    private Animation[] animations = new Animation[1];
    private Music wowSound;

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
        
        SpriteSheet spriteSheet = new SpriteSheet("sprites/eddy_wow.png", 250, 250);
        this.animations[0] = loadAnimation(spriteSheet, 0, 36, 0);
        this.wowSound = new Music("sound/Wally.ogg");
        
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
    	
    	if(this.appear && this.animations[0].getFrame()<35){
    		g.drawAnimation(this.animations[0], this.x, this.y);
    	}
    	else{
    		this.animations[0].restart();
    		this.appear = false;
    	}
    	
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
        
        if (input.isKeyDown(Input.KEY_SPACE)) {
        	this.appear = true;
	        this.wowSound.play();
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
    
    private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
        Animation animation = new Animation();
        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), 100);
        }
        return animation;
    }

}