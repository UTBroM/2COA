package gamepack.data.drawable;

import gamepack.utility.ProjectMethods;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Bomb extends Tile{
	private int remainingMovement;
	private int explosionRadius;
	
	//Basic construtor
	private Bomb(int x, int y, int value, int remainingMovement, int explosionRadius)
	{
		super(x, y, value);
		this.remainingMovement = remainingMovement;
		this.explosionRadius = explosionRadius;
	}
	
	//Public construtor
	public Bomb(int x, int y, int value)
	{
		this(x, y, value, 10, 1);
	}
	
	//Constructor by copy
	public Bomb(Bomb b)
	{
		super(b);
		this.remainingMovement = b.getRemainingMovement();
		this.explosionRadius = b.getExplosionRadius();
	}
	
	public int getRemainingMovement()
	{
		return this.remainingMovement;
	}
	
	public int getExplosionRadius()
	{
		return this.explosionRadius;
	}
	
	//Is use to know the number of remaining movement before explosion
	public boolean minusRemainingMovement()
	{
		this.remainingMovement--; 
		if (this.remainingMovement <=0){
			return true;
		}else{
			return false;
		}
	}
	
	//Draw the Bomb
	public void beDrawn(Graphics gr)
	{
		//affichage bomb stylisé
		int v = 11-remainingMovement;
		boolean critical = false;
		if(remainingMovement == 1)
		{
			critical = true;
			v *= 2.5;
		}
		int dx = ProjectMethods.randInt(-v/2,+v/2);
		int dy = ProjectMethods.randInt(-v/2,+v/2);

		
		//tile fond
		gr.setColor(rectangleColor);
		gr.fillRoundRect(rectangle.getX()+dx,
				rectangle.getY()+dy, 
				rectangle.getWidth(), 
				rectangle.getHeight(), 
				4);
		
		//texte
		if(getTextWidth() < rectangle.getWidth()/2)
		{
			gr.setColor(Color.black);
			if(critical)
				gr.setColor(Color.red);
			gr.drawString("" + getValue(), getTextX()+dx, getTextY()+dy);
		}
	}
}
