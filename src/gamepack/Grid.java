package gamepack;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Grid implements DrawableObject
{
	private int	sizeX;
	private int	sizeY;
	
	public Grid(int x, int y)
	{
		this.sizeX = x;
		this.sizeY = y;
	}
	
	public void beDrawn(Graphics g)
	{
		int min = (sizeX < sizeY ? sizeX : sizeY); // To let the grid be squared
		
		// delete min and replace by sizeX or by sizeY in the definitions if you
		// no longer want to have a square
		
		//int marginX = 30 * min / 800;
		int marginY = 60 * min / 800;
		int padX = 20 * min / 800;
		int padY = 20 * min / 800;
		int rectSizeX = 160 * min / 800;
		int rectSizeY = 160 * min / 800;
		
		int marginX = (sizeX - 4 * (rectSizeX + padX)) / 2; // Align the grid in the middle of the window
		
		Color bgColor = new Color(0xC1B8B0);
		Color interiorColor = new Color(0xD6CDC4);
		
		/* BackGround */
		g.setColor(bgColor);
		g.fillRect(0, 0, sizeX, sizeY);
		
		/* Rectangles */
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				g.setColor(interiorColor);
				g.fillRoundRect(marginX + j * (rectSizeX + padX), marginY + i * (rectSizeY + padY), rectSizeX, rectSizeY, 4);
			}
		}
	}
}
