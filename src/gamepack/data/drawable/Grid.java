package gamepack.data.drawable;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Grid implements DrawableObject
{
	private int sizeX;
	private int sizeY;
	private int padX;
	private int padY;
	private int rectSizeX;
	private int rectSizeY;
	private int marginX;
	private int marginY;
	
	private Color bgColor = new Color(0xC1B8B0);
	private Color interiorColor = new Color(0xD6CDC4);
	
	private Collection<Rectangle> rectangleList;
	
	public Grid(int x, int y)
	{
		this.sizeX = x;
		this.sizeY = y;
		
		int min = (sizeX < sizeY ? sizeX : sizeY); // To let the grid be squared
		
		// delete min and replace by sizeX or by sizeY in the definitions if you no longer want to have a square
		
		this.padX = 20 * min / 800;
		this.padY = 20 * min / 800;
		this.rectSizeX = 160 * min / 800;
		this.rectSizeY = 160 * min / 800;
		this.marginY = 60 * min / 800;
		
		this.marginX = (sizeX - 4 * (rectSizeX + padX)) / 2; // Align the grid in the middle of the window
		
		rectangleList = new ArrayList<Rectangle>();
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				rectangleList.add(new Rectangle(marginX + j * (rectSizeX + padX), marginY + i * (rectSizeY + padY), rectSizeX, rectSizeY));
			}
		}
	}
	
	public Collection<Rectangle> getRectangles()
	{
		return this.rectangleList;
	}
	
	public int squareSize()
	{
		return (int) ((Rectangle)((ArrayList)rectangleList).get(0)).getWidth();
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
			g.fill(rectangle);
		}
	}
}
