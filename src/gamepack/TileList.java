/*
 * Contient la liste des tuiles présentent dans la grille
 * 
 */
package gamepack;
import java.util.ArrayList;
import java.util.List;

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
	public void add(int x, int y)
	{
		tList.add(new Tile(x,y));
	}
	
	public Tile getTile(int i)
	{
		return this.tList.get(i);
	}
	public void setTile(int i, int x, int y, int value)
	{
		Tile tmp = new Tile(x,y,value);
		tList.set(i, tmp);
	}
	public void remove(int i)
	{
		tList.remove(i);
	}
	public int getSize()
	{
		return this.tList.size();
	}
}
