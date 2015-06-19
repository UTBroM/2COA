package gamepack.data.drawable;


import gamepack.data.Point;
import gamepack.utility.Direction;
import gamepack.utility.ProjectMethods;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

//fait par le groupe
public class Tile implements DrawableObject
{
	private int			value;
	private int			powerOfTwo;
	
	protected Rectangle	rectangle;
	protected Color rectangleColor;
	
	private Direction	tileDirection;	//Give in which direction the tile goes
	protected Tile		arrivedTile;	//If the tile has an arrived Tile
	private Point		arrivedPoint;	//If the tile has an arrived Point
	
	
	
	public Tile(int x, int y, int value)
	{
		this.rectangle = new Rectangle(x, y, 50, 50);
		this.value = value;
		powerOfTwo = ProjectMethods.powerOfTwo(value);
		tileDirection = Direction.None;
		refreshColor();
	}
	
	public Tile(Tile t)
	{
		this.value = t.value;
		this.powerOfTwo = t.powerOfTwo;

		this.rectangle = new Rectangle(t.getX(), t.getY(), t.rectangle.getWidth(), t.rectangle.getHeight());
		this.tileDirection = t.tileDirection;
		this.arrivedTile = t.arrivedTile;
		this.arrivedPoint = t.arrivedPoint;
		refreshColor();
	}
	
	//GETTERS
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
	
	public int getCenterX()
	{
		return (int) this.rectangle.getCenterX();
	}
	
	public int getCenterY()
	{
		return (int) this.rectangle.getCenterY();
	}

	public Tile getArrivedTile()
	{
		return this.arrivedTile;
	}
	
	public Point getArrivedPoint() 
	{
		return arrivedPoint;
	}

	public int getArrivedPointX()
	{
		return arrivedPoint.getX();
	}
	
	public int getArrivedPointY()
	{
		return arrivedPoint.getY();
	}

	public Direction getDirection()
	{
		return this.tileDirection;
	}
	
	protected int getTextX()
	{
		return (int) (getCenterX() - getTextWidth()/2.0);
	}
	
	protected int getTextY()
	{
		return getCenterY()-5;
	}
	
	protected int getTextWidth()
	{
		return (int) (ProjectMethods.getLength(value)*6.0);
	}
	
	//SETTERS
	public void setX(int x)
	{
		this.rectangle.setX(x);
	}
	
	public void setY(int y)
	{
		this.rectangle.setY(y);
	}

	public void setArrivedTile(Tile t)
	{
		this.arrivedTile = t;
	}
	
	public void setArrivedPoint(Point arrivedPoint)
	{
		this.arrivedPoint = arrivedPoint;
	}
	
	public void setDirection(Direction tileDirection)
	{
		this.tileDirection = tileDirection;
	}
	
	public void setSize(int size)
	{
		rectangle.setSize(size, size);
	}
	
	
	
	//OTHER
	//Double the value of the tile, increment the powerOfTwo and refresh the color
	public void doubleValue()
	{
		value = 2*value;
		powerOfTwo++;
		refreshColor();
	}
	
	//move the tile 
	public void move(float x, float y)
	{
		this.rectangle.setX(this.rectangle.getX() + x);
		this.rectangle.setY(this.rectangle.getY() + y);
	}
	
	//Test if two tiles have the same positions and value
	public boolean equals(Tile secondTile)
	{
		//System.out.println(getX() + " " + secondTile.getX() + "   " + getY() + " " + secondTile.getY());
		if (secondTile != null && getX() == secondTile.getX() && getY() == secondTile.getY() && getValue()==secondTile.getValue()) 
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
	
	//Draw the Tile
	public void beDrawn(Graphics gr)
	{
		//Couleur mise
		gr.setColor(rectangleColor);
		//gr.fill(rectangle);
		gr.fillRoundRect(rectangle.getX(),
				rectangle.getY(), 
				rectangle.getWidth(), 
				rectangle.getHeight(), 
				4);

		if(getTextWidth() < rectangle.getWidth()/2)
		{
			gr.setColor(Color.white);
			gr.drawString("" + value, getTextX(), getTextY());
		}
	}
	
	//refresh the color of the Tile
	private void refreshColor()
	{
		int rgb[] = ProjectMethods.getRGBColor(powerOfTwo);
		rectangleColor = new Color(rgb[0],rgb[1],rgb[2]);
	}

	
	//draw the tile
	public String toString()
	{
		return ""+value;
	}
}
