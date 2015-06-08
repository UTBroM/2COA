package gamepack.data;


import gamepack.utily.Direction;
import gamepack.utily.DrawableObject;
import gamepack.utily.ProjectMethods;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Tile implements DrawableObject
{
	private int			value;
	private int			powerOfTwo;
	
	private Rectangle	rectangle;
	private Color 		rectangleColor;
	
	private Direction	tileDirection;	//Give in which direction the tile goes
	private Tile		arrivedTile;	//If the tile has an arrived Tile
	private Point		arrivedPoint;	//If the tile has an arrived Point
										
	public Point getArrivedPoint() {
		return arrivedPoint;
	}

	public Tile(int x, int y, int size)
	{
		this(x, y, alea(), size); //changer  le 2 en random value entre 2 ou 4 ou bombe

	}
	
	public Tile(int x, int y, int value, int size)
	{
		this.rectangle = new Rectangle(x, y, size, size);
		this.value = value;
		powerOfTwo = ProjectMethods.powerOfTwo(value);
		tileDirection = Direction.None;
		refreshColor();
	}
	
	//GET
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
	
	private int getCenterX()
	{
		return (int) this.rectangle.getCenterX();
	}
	
	private int getCenterY()
	{
		return (int) this.rectangle.getCenterY();
	}

	public Tile getArrivedTile()
	{
		return this.arrivedTile;
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
	
	private int getTextX()
	{
		return getCenterX();
	}
	
	private int getTextY()
	{
		return getCenterY();
	}
	
	//SET
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
		if (secondTile != null && getX() == secondTile.getX() && getY() == secondTile.getY() && value == secondTile.getValue()) 
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
		gr.fill(rectangle);
		
		gr.setColor(Color.white);
		gr.drawString("" + value, getTextX(), getTextY());
	}
	
	//refresh the color of the Tile
	private void refreshColor()
	{
		int rgb[] = ProjectMethods.getRGBColor(powerOfTwo);
		rectangleColor = new Color(rgb[0],rgb[1],rgb[2]);
	}


}