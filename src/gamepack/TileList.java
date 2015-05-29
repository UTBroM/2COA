package gamepack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileList {
	private List<Tile> tList;
	
	public TileList()
	{
		this.tList = new ArrayList<Tile>();
	}
	
	public void add(int x, int y, int value)
	{
		tList.add(new Tile(x,y,value));
	}
	
	public void remove(int x, int y)
	{
		
	}
}
