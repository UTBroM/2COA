package gamepack.data;

import gamepack.data.drawable.TileList;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public class PointMatrix
{
	private ArrayList<ArrayList<Point>> matrix;
	private int matrixSize;
	
	private PointMatrix(int matrixSize)
	{
		matrix = new ArrayList<ArrayList<Point>>();
		this.matrixSize = matrixSize;
		for(int i = 0 ; i < matrixSize;i ++)
			matrix.add(new ArrayList<Point>(matrixSize));
	}
	
	public PointMatrix(ArrayList<Rectangle> rectList)
	{
		this((int) Math.sqrt(rectList.size()));
		int i = 0;
		for(int y = 0 ; y< matrixSize; y++)
			for(int x = 0; x < matrixSize; x++)
			{
				addAt(x, y, new Point((int)rectList.get(i).getX(),(int)rectList.get(i).getY()));
				i++;
			}
	}

	public void addAt(int x, int y,Point point)
	{
		matrix.get(y).add(x, point);
	}
	
	public Point getAt(int x, int y)
	{
		return matrix.get(y).get(x);
	}
	
	public int size()
	{
		return matrixSize;
	}
	
	public String toString()
	{
		String s="";
		for(int y = 0 ; y< matrixSize; y++)
			s += matrix.get(y);
		return s;
	}

	public int[] getPositionsOf(Point point)
	{
		int[] positions = {-1,-1};
		for (int i = 0; i < matrixSize; i++)
		{
			for (int j = 0; j < matrixSize; j++)
			{
				if(getAt(i, j).getX() == point.getX() && getAt(i, j).getY() == point.getY())
				{
					positions[0] = i;
					positions[1] = j;
				}
			}
		}
		if(positions[0] == -1 || positions[1] == -1)
			System.out.println("bla");
		return positions;
	}
	
	public int[] getReversePositionsOf(Point point)
	{
		int[] positions = getPositionsOf(point);
		positions[0] = size()-1-positions[0];
		positions[1] = size()-1-positions[1];
		return positions;
	}
}
