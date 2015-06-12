package gamepack.data.drawable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Grid implements DrawableObject
{
	private int sizeX;
	private int sizeY;
	
	private Color bgColor = new Color(0xC1B8B0);
	private Color interiorColor = new Color(0xD6CDC4);
	
	private ArrayList<Rectangle> rectangleList;
	
	public Grid(int x, int y)
	{
		// Initialization of the base attributes of the grid :
		int gridSize = 4;
		
		float padX = 20;
		float padY = 20;
		
		float marginY = 30;
		float leftMarginX = 30;
		float rightMarginX = 50;
		float rectSizeX, rectSizeY, minRectSize;
		
		this.sizeX = x;
		this.sizeY = y;
		
		float min = (sizeX < sizeY ? sizeX : sizeY); // To let the grid be squared
		
		padX *= min / 800;
		padY *= min / 800;
		marginY *= min / 800;
		leftMarginX *= min/800;
		rightMarginX *= min/800;
		rectSizeX = (min - (leftMarginX + rightMarginX) - (gridSize-1)*padX)/(gridSize);
		rectSizeY = (min - (2*marginY) - (gridSize-1)*padY)/(gridSize);
		minRectSize = (float) Math.floor((rectSizeX < rectSizeY ? rectSizeX : rectSizeY));
		rectangleList = new ArrayList<Rectangle>();
		for (int i = 0; i < gridSize; i++)
		{
			for (int j = 0; j < gridSize; j++)
			{
				rectangleList.add(new Rectangle(leftMarginX + j * (minRectSize + padX), marginY + i * (minRectSize + padY), minRectSize, minRectSize));
			}
		}
	}
	
	public ArrayList<Rectangle> getRectangles()
	{
		return this.rectangleList;
	}
	
	public int squareSize()
	{
		return (int) ((Rectangle)rectangleList.get(0)).getWidth();
	}
	
	public void beDrawn(Graphics g)
	{
		/* BackGround */
		g.setColor(bgColor);
		g.fillRect(0, 0, sizeX, sizeY);
		
		/* Rectangles */
		g.setColor(interiorColor);
		for (Rectangle rectangle : rectangleList)
		{
			//g.fill(rectangle);
			g.fillRoundRect(rectangle.getX(),
					rectangle.getY(), 
					rectangle.getWidth(), 
					rectangle.getHeight(), 
					4);
		}
	}
}
