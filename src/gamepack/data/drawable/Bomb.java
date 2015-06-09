package gamepack.data.drawable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Bomb extends Tile{
	private int remainingMovement;
	private int explosionRadius;
	
	private Bomb(int x, int y, int value, int size, int remainingMovement, int explosionRadius)
	{
		super(x, y, value, size);
		this.remainingMovement = remainingMovement;
		this.explosionRadius = explosionRadius;
	}
	
	public Bomb(int x, int y, int value, int size)
	{
		this(x, y, value, size, 10, 1);
	}
	
	public int getRemainingMovement()
	{
		return this.remainingMovement;
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
		gr.fill(rectangle);
		
		gr.setColor(Color.black);
		gr.drawString("" + getValue(), getTextX(), getTextY());
	}
	
}
