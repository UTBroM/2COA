package gamepack.data;

//fait par le groupe
public final class Point
{
	private final int	x;
	private final int	y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}
}
