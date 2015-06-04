/*
 * Contient la liste des tuiles présentent dans la grille
 * 
 */
package gamepack;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

public class TileList implements DrawableObject
{
	private List<Tile>	tList;
	
	public List<Tile> gettList() {
		return tList;
	}

	public TileList()
	{
		this.tList = new ArrayList<Tile>();
	}
	
	public void addNewTile(int x, int y, int value, int size)
	{
		tList.add(new Tile(x, y, value,size));
	}
	public void addNewTile(int x, int y, int size)
	{
		tList.add(new Tile(x, y,size));
	}
	public void add(Tile newTile)
	{
		tList.add(newTile);
	}
	
	public Tile getTile(int i)
	{
		return this.tList.get(i);
	}
	
	public void setTile(int i, Tile tmp)
	{
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
	
	public void beDrawn(Graphics g)
	{
		for (int i = 0; i < tList.size(); i++)
		{
			tList.get(i).beDrawn(g);
		}
		
	}
	
}
