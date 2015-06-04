package gamepack;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Tile implements DrawableObject
{
	private int			value;
	private Rectangle	rectangle;
	private Direction	tileDirection;	//Give in which direction the tile goes
										
	private Tile		arrivedTile;	//If the tile has an arrived Tile
	private Point		arrivedPoint;	//If the tile has an arrived Point
										
	public Point getArrivedPoint() {
		return arrivedPoint;
	}

	public Tile(int x, int y, int size)
	{
		this(x, y, 2,size); //changer  le 2 en random value entre 2 ou 4 ou bombe
	}
	
	public Tile(int x, int y, int value, int size)
	{
		this.rectangle = new Rectangle(x, y, size, size);
		this.value = value;
		tileDirection = Direction.None;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public float getX()
	{
		return this.rectangle.getX();
	}
	
	public float getY()
	{
		return this.rectangle.getY();
	}
	
	public void setX(int x)
	{
		this.rectangle.setX(x);
	}
	
	public void setY(int y)
	{
		this.rectangle.setY(y);
	}
	public void doubleValue()
	{
		value = 2*value;
	}
	private float getCenterX()
	{
		return this.rectangle.getCenterX();
	}
	
	private float getCenterY()
	{
		return this.rectangle.getCenterY();
	}
	
	public Tile getArrivedTile()
	{
		return this.arrivedTile;
	}
	
	public void setArrivedTile(Tile t)
	{
		this.arrivedTile = t;
		arrivedPoint = new Point((int)t.getX(), (int)t.getY());
	}
	
	public int getArrivedPointX()
	{
		return arrivedPoint.getX();
	}
	
	public int getArrivedPointY()
	{
		return arrivedPoint.getY();
	}
	
	public void setArrivedPoint(Point arrivedPoint)
	{
		this.arrivedPoint = arrivedPoint;
	}
	
	public Direction getDirection()
	{
		return this.tileDirection;
	}
	
	public void setDirection(Direction tileDirection)
	{
		this.tileDirection = tileDirection;
	}
	
	public void move(float x, float y)
	{
		this.rectangle.setX(this.rectangle.getX() + x);
		this.rectangle.setY(this.rectangle.getY() + y);
	}
	
	//Test if two tiles have the same
	public boolean equals(Tile secondTile)
	{
		if (getX() == secondTile.getX() && getY() == secondTile.getY() && value == secondTile.getValue()) 
			return true;
			return false;
	}
	
	//If the tile and his arrivedTile has the same coordinates, return true
	//then, double the value of the arrivedTile (use a method doubleValue())
	public Boolean refreshFusion()
	{
		if (this.equals(this.arrivedTile)) {
			this.arrivedTile.doubleValue();
			return true;
		}else{
			return false;
		}
	}
	
	//return true if the tile has arrived to his arrivedPoint 
	public boolean isArrived()
	{
		if(arrivedPoint != null)
			return getX() == arrivedPoint.getX() && getY() == arrivedPoint.getY();
		return true;
	}
	
	//avoid a bad positionning of the tile (it could be positionning after the arrived point due to
	//bad framerate)
	public void improvePosition()
	{
		if(tileDirection == Direction.Left)
		{
			if(getX() < getArrivedPointX())
				setX(getArrivedPointX());
		}
		else if(tileDirection == Direction.Right)
		{
			if(getX() > getArrivedPointX())
				setX(getArrivedPointX());
		}
		else if(tileDirection == Direction.Up)
		{
			if(getY() < getArrivedPointY())
				setY(getArrivedPointY());
		}
		else if(tileDirection == Direction.Down)
		{
			if(getY() > getArrivedPointY())
				setY(getArrivedPointY());
		}
	}
	
	public void beDrawn(Graphics g)
	{
		//Couleurs de tests, � changer
		g.setColor(Color.red);
		g.fill(rectangle);
		
		g.draw(rectangle);
		g.setColor(Color.white);
		g.drawString("" + value, getCenterX(), getCenterY());
	}
}