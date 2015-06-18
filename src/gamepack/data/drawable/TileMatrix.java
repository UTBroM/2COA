package gamepack.data.drawable;

import gamepack.utility.Direction;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;

public class TileMatrix implements DrawableObject
{
	private ArrayList<ArrayList<Tile>> matrix;
	private int matrixSize;
	private int tileSize;
	
	//Constructor of a tile matrix, it already knows his size and all the tile are initialized to null
	public TileMatrix(int matrixSize, int tileSize)
	{
		matrix = new ArrayList<ArrayList<Tile>>();
		this.matrixSize = matrixSize;
		for(int i = 0 ; i < matrixSize;i ++)
		{
			ArrayList<Tile> t = new ArrayList<Tile>(matrixSize);
			for(int j =0 ; j < matrixSize; j++)
				t.add(null);
			matrix.add(t);
		}
		this.tileSize = tileSize;
		
	}
		
	//Constructor by copy (create new arraylist, conserve tile references if b = true, reatenew one otherwise)
	public TileMatrix(TileMatrix tMatrix, boolean b)
	{
		this.tileSize = tMatrix.tileSize;
		this.matrixSize = tMatrix.matrixSize;
		
		matrix = new ArrayList<ArrayList<Tile>>();
		for(int i = 0 ; i < matrixSize;i ++)
		{
			ArrayList<Tile> t = new ArrayList<Tile>();
			for(int j =0 ; j < matrixSize; j++)
				t.add(null);
			matrix.add(t);
		}
		
		if(b) // Create new tiles
			for(int i = 0 ; i < matrixSize;i ++)
				for(int j = 0 ; j < matrixSize;j ++)
				{
					Tile tileRef = tMatrix.get(j, i);
					if(tileRef != null)
					{
						if(tileRef instanceof Bomb)
							matrix.get(i).set(j, new Bomb((Bomb)tileRef));
						else
							matrix.get(i).set(j, new Tile(tileRef));
					}
				}
		else // copy existent tiles
			for(int i = 0 ; i < matrixSize;i ++)
				for(int j = 0 ; j < matrixSize;j ++)
					matrix.get(i).set(j, tMatrix.get(j, i));
	}
	
	//Set the specified direction to every tile
	public void setDirection(Direction d)
	{
		for(ArrayList<Tile> line : matrix)
		{
			for(Tile tile : line)
			{
				if(tile != null)
				{
					tile.setDirection(d);
				}
			}
		}
	}
	
	//to get a tile using linear coordinates, for example for a 4x4 matrix, getAtLinear(5) will return the tile
	//with coordinates (0,1)
	public Tile getAtLinear(int index)
	{
		int x = index, y = 0;
		if(index >= matrixSize)
		{
			y = index/matrixSize;
			x = index%matrixSize;
		}
		return get(x, y);
		
	}
	
	//get the number of tile
	public int getLinearSize()
	{
		return matrixSize*matrixSize;
	}
	
	//get the tile at coordinates x and y in the matrix, may be null
	public Tile get(int x, int y)
	{
		return matrix.get(y).get(x);
	}
	
	//add a tile at the x, y coordinates in the matrix
	public void set(int x, int y, Tile t)
	{
		if(t != null)
			t.setSize(tileSize);
		matrix.get(y).set(x, t);
	
	}
	
	//remove a tile at the end of the matrix
	public void removeLinear(int index)
	{
		int x = index, y = 0;
		if(index > matrixSize)
		{
			y = index/matrixSize;
			x = index%matrixSize;
		}
		matrix.get(y).remove(x);
	}
	
	//remove the tile at the x and y coordinates in the matrix
	public void deleteAt(int x, int y)
	{
		matrix.get(y).set(x, null);
	}
	
	//remove the tile which has the positions tileX and tileY
	public void removeTileWithPositions(int tileX, int tileY)
	{
		for(int i = 0 ; i < getLinearSize(); i++)
			if(getAtLinear(i).getX() == tileX && getAtLinear(i).getY() == tileY)
				removeLinear(i);
	}
	
	//draw the matrice
	public void beDrawn(Graphics g)
	{
		for(int i =0 ;i < matrix.size(); i++)
			for(int j =0 ;j < matrix.size();j++)
				if(matrix.get(i).get(j) != null)
					matrix.get(i).get(j).beDrawn(g);
	}	
	
	//return the number of non-null tile 
	public int getNumberOfTile()
	{
		int sum = 0;
		for(int i = 0; i < matrix.size(); i++)
			for(int j = 0;j < matrix.size(); j++)
				if(get(i, j) != null)
					sum ++;
		return sum;
	}
	
	//get the size of the matrix
	public int getMatrixSize()
	{
		return matrixSize;
	}
	
	//return the line at the specefied position
	public ArrayList<Tile> getLine(int y)
	{
		return matrix.get(y);
	}
	
	//return the column at the specified position
	public ArrayList<Tile> getColumn(int x)
	{
		ArrayList<Tile> col = new ArrayList<Tile>();
		for(int i = 0; i< matrixSize; i++)
			col.add(get(x,i));
		return col;
	}
	
	//write the matrix
	public String toString()
	{
		String s= "----Matrix\n";
		for(int i = 0 ; i < matrixSize; i++)
		{
			for(int j = 0 ; j < matrixSize; j++)
				s += "(" + get(j,i) + ")\t";
			s+="\n";
				
		}
		return s + "----\n";
	}
	
	//set the line arr at the specified position
	public void setLine(int y, ArrayList<Tile> arr)
	{
		matrix.set(y,arr); // technically useless but useful for symetry with the setColumn (which is needed)
	}
	
	//set the column arr at the specified position
	public void setColumn(int x, ArrayList<Tile> arr)
	{
		for(int i = 0; i< matrixSize; i++)
			matrix.get(i).set(x, arr.get(i));
	}
	
	
}
