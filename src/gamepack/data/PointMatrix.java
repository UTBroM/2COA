package gamepack.data;


import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public class PointMatrix
{
	private ArrayList<ArrayList<Point>> matrix;
	private int matrixSize;
	
	//Create the null PointMatrix with the specified size (must not be used outside)
	private PointMatrix(int matrixSize)
	{
		matrix = new ArrayList<ArrayList<Point>>();
		this.matrixSize = matrixSize;
		for(int i = 0 ; i < matrixSize;i ++)
			matrix.add(new ArrayList<Point>(matrixSize));
	}
	
	//Create the PointMatrix depending on a rectangle list (take the top-left coordinates of rectangles)
	public PointMatrix(ArrayList<Rectangle> rectList)
	{
		this((int) Math.sqrt(rectList.size()));
		int i = 0;
		for(int y = 0 ; y< matrixSize; y++)
			for(int x = 0; x < matrixSize; x++)
			{
				set(x, y, new Point((int)rectList.get(i).getX(),(int)rectList.get(i).getY()));
				i++;
			}
	}

	//set the point at the x and y coordinates
	private void set(int x, int y,Point point)
	{
		matrix.get(y).add(x, point);
	}
	
	//get the point at the x and y coordinates
	public Point getAt(int x, int y)
	{
		return matrix.get(y).get(x);
	}
	
	//return the width / height of the square matrix
	public int size()
	{
		return matrixSize;
	}
	
	//write the matrix
	public String toString()
	{
		String s="";
		for(int y = 0 ; y< matrixSize; y++)
			s += matrix.get(y);
		return s;
	}

	//get the position of the point in the matrix
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
			System.err.println("Bad positionning in PointMatrix");
		return positions;
	}
	
}
