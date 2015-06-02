package gamepack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Tile implements DrawableObject
{
	private int			value;
	private Rectangle	rectangle;
	private Direction	tileDirection;	//Give in which direction the tile goes
										
	private Tile		arrivedTile;	//If the tile has an arrived Tile
	private Point		arrivedPoint;	//If the tile has an arrived Point
										
	public Tile(int x, int y)
	{
		this(x, y, 2); //changer  le 2 en random value entre 2 ou 4 ou bombe
	}
	
	public Tile(int x, int y, int value)
	{
		this.rectangle = new Rectangle(x, y, 140, 140);
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
	
	//If the tile and his arrivedTile has the same coordinates, return true
	//then, double the value of the arrivedTile (use a method doubleValue())
	public Boolean refreshFusion()
	{
		if (this.equals(this.arrivedTile)) {
			this.value = (this.value) * 2;
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
		g.draw(rectangle);
		g.drawString("" + value, getCenterX(), getCenterY());
	}
}