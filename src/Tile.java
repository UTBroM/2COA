import org.newdawn.slick.geom.Rectangle;


public class Tile {
	private int value;
	private Rectangle rectangle;
	//private int special;
	
	public Tile(int x, int y)
	{
		this(x,y,2);
	}
	
	public Tile(int x, int y, int value)
	{
		this.rectangle = new Rectangle(x, y, 140, 140); 
		//vérifier si la value est un multiple de 2
		this.value = value;
	}
		
	public int getValue()
	{
		return this.value;
	}
	
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}
	
	public void move(float x, float y)
	{
		this.rectangle.setX(this.rectangle.getX()+x);
		this.rectangle.setY(this.rectangle.getY()+y);
	}
	
	public float getCenterX()
	{
		return this.rectangle.getCenterX();
	}
	public float getCenterY()
	{
		return this.rectangle.getCenterY();
	}
	
}
