package gamepack.data.drawable;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Grid implements DrawableObject
{
	private float sizeX;
	private float sizeY;
	
	private Color interiorColor = new Color(0xD6CDC4);
	
	private ArrayList<Rectangle> rectangleList;
	private int gridSize;
	
	public Grid(int x, int y)
	{
		//Compute such that there is enough place for the score at the right of the screen
		final float sizeOnLeft = 120;
		if((float)x * 0.2 < sizeOnLeft)
		{
			float multiplX = sizeOnLeft / (float)x;

			x *= (1-multiplX);
		
		}
		// Initialization of the base attributes of the grid :
		gridSize = 4;
		
		float padX = 20;
		float padY = 20;
		
		float marginY = 30;
		float leftMarginX = 30;
		float rightMarginX = 50;
		float rectSizeX, rectSizeY, maxRectSize;
		
		this.sizeX = x;
		this.sizeY = y;
		
		float min = (sizeX - 150 < sizeY ? sizeX - 150 : sizeY); // To let the grid be squared
		
		padX *= min / 800;
		padY *= min / 800;
		marginY *= min / 800;
		leftMarginX *= min / 800;
		rightMarginX *= min / 800;
		rectSizeX = (min - (leftMarginX + rightMarginX) - (gridSize - 1) * padX) / (gridSize);
		rectSizeY = (min - (2 * marginY) - (gridSize - 1) * padY) / (gridSize);
		maxRectSize = (float) Math.floor((rectSizeX > rectSizeY ? rectSizeX : rectSizeY));
		rectangleList = new ArrayList<Rectangle>();
		for (int i = 0; i < gridSize; i++)
		{
			for (int j = 0; j < gridSize; j++)
			{
				rectangleList.add(new Rectangle(leftMarginX + j * (maxRectSize + padX), marginY + i * (maxRectSize + padY), maxRectSize, maxRectSize));
			}
		}
	}
	
	public ArrayList<Rectangle> getRectangles()
	{
		return this.rectangleList;
	}
	
	public int squareSize()
	{
		return (int) ((Rectangle) rectangleList.get(0)).getWidth();
	}
	
	public int getRightPosition()
	{
		//20pixel at the right of the grid
		return (int) (rectangleList.get(gridSize-1).getX()+rectangleList.get(gridSize-1).getWidth()+20);
	
	}
	
	public void beDrawn(Graphics g)
	{
		/* Rectangles */
		g.setColor(interiorColor);
		for (Rectangle rectangle : rectangleList)
		{
			//g.fill(rectangle);
			g.fillRoundRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), 4);
		}
		// Menu 
		g.setColor(Color.white);
	}
}
