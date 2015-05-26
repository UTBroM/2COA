import org.newdawn.slick.geom.Rectangle;


public class Tile {
	private int value;
	private Rectangle rectangle;
	//private int special;
	
	public Tile(Rectangle rc)
	{
		this.value = 2;
	}
	
	public Tile(int value, Rectangle rc)
	{
		this.value = value;
		this.rectangle = rc;
	}
	
	public boolean combine(Tile other)
	{
		other.value = 0;
		return (this.value*=2) == 2048;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}
}
