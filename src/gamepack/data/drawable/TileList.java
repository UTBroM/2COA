/*
 * Contient la liste des tuiles présentent dans la grille
 * 
 */
package gamepack.data.drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.Graphics;

public class TileList implements DrawableObject
{
	private ArrayList<Tile>	tList;
	
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
	
	public void sortX(){
		
		Collections.sort(tList,new PointCompareX());
	}
	
    public class PointCompareX implements Comparator<Tile> {

	    public int compare(final Tile a, final Tile b) {
	        if (a.getX() < b.getX()) {
	            return -1;
	        }
	        else if (a.getX() > b.getX()) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	}
	
	public void sortY(){

		Collections.sort(tList,new PointCompareY());
		
	}
	
    public class PointCompareY implements Comparator<Tile> {

	    public int compare(final Tile a, final Tile b) {
	        if (a.getY() < b.getY()) {
	            return -1;
	        }
	        else if (a.getY() > b.getY()) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	}
	
}
