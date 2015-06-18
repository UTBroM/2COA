package gamepack.utility;

import java.util.Random;

public abstract class ProjectMethods
{
	//return the power of two of the value
	public static int powerOfTwo(int v)
	{
		int r = 0;
		if(v > 0)
			while(v % 2 == 0)
			{
				v = v/2;
				r++;
			}
		if(v == 1)
			return r;
		return 0;
	}
	
	//Give the color of the tile in an array depending of its value
	public static int[] getRGBColor(int power)
	{
		int r=0,g=0,b=0;
		if(power <= 6) //jusqu'� 64
		{
			r = 230;
			g = 210-power*20;
			b = 200-power*20;
		}
		else if(power <= 11) //jusqu'� 2048
		{
			r = 230-(power-7)*3;
			g = 210-(power-7)*5;
			b = 110-(power-7)*10;
		}
		
		return new int[]{r,g,b};
	}
	
	
	// Give a random tile value for a Tile
	public static int getRandomTileValue(Random rand)
	{
		int value;
		switch (rand.nextInt(4))
		{
			case 0:
				value = 4;
				break;
			
			default:
				value = 2;
				break;
		}
		
		return value;
	}
	
}
