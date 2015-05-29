import org.newdawn.slick.geom.Rectangle;


public class Tile {
	private int value;
	private Rectangle rectangle;
	//private int special;
	
	public Tile(Rectangle rc)
	{
		this.value = -1;
		this.rectangle = rc;
	}
	
	public Tile(int value, Rectangle rc)
	{
		this.value = value;
		this.rectangle = rc;
	}
	
	public boolean combine(Tile other)
	{
		if(other != null)// tile.combine(null) initialise la valeure de tile  � 2 si la case n'�tait pas encore cr�e
			other.value = -1;
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
	
	public boolean isEmpty()
	{
		return this.value == -1;
	}
}
