package gamepack;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


public class Tile implements DrawableObject
{
	private int value;
	private Rectangle rectangle;
	private Direction tileDirection;	//Indique dans quelle direction va le tile
	private Point arrivedPoint;			//Represent the point where the tile will go
	
	
	public Tile(int x, int y)
	{
		this(x,y,2); //changer  le 2 en random value entre 2 ou 4 ou bombe
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
	public float getCenterX()
	{
		return this.rectangle.getCenterX();
	}
	public float getCenterY()
	{
		return this.rectangle.getCenterY();
	}
	public Point getArrivedPoint()
	{
		return this.arrivedPoint;
	}
	public void setArrivedPoint(Point p)
	{
		this.arrivedPoint = p;
	}
	public Direction getDirection ()
	{
		return this.tileDirection;
	}
	public void setDirection(Direction tileDirection)
	{
		this.tileDirection = tileDirection;
	}
	
	public void move(float x, float y)
	{
		this.rectangle.setX(this.rectangle.getX()+x);
		this.rectangle.setY(this.rectangle.getY()+y);
	}
	public void beDrawn(Graphics g)
	{
		g.draw(rectangle);
		g.drawString(""+value, getCenterX(), getCenterY());
	}
}