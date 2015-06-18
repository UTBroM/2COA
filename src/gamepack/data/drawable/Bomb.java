package gamepack.data.drawable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Bomb extends Tile{
	private int remainingMovement;
	private int explosionRadius;
	
	private Bomb(int x, int y, int value, int remainingMovement, int explosionRadius)
	{
		super(x, y, value);
		this.remainingMovement = remainingMovement;
		this.explosionRadius = explosionRadius;
	}
	
	public Bomb(int x, int y, int value)
	{
		this(x, y, value, 10, 1);
	}
	
	public int getRemainingMovement()
	{
		return this.remainingMovement;
	}
	public boolean minusRemainingMovement()
	{
		this.remainingMovement--; 
		if (this.remainingMovement <=0){
			return true;
		}else{
			return false;
		}
	}
	
	public int getExplosionRadius()
	{
		return this.explosionRadius;
	}
	
	//Draw the Bomb
	public void beDrawn(Graphics gr)
	{
		//Color
		gr.setColor(rectangleColor);
		//gr.fill(rectangle);
		gr.fillRoundRect(rectangle.getX(),
				rectangle.getY(), 
				rectangle.getWidth(), 
				rectangle.getHeight(), 
				4);
		
		gr.setColor(Color.black);
		gr.drawString("" + getValue() + "\n\n"+ getRemainingMovement() + " move", getTextX()-5, getTextY()-5);
	}
}
