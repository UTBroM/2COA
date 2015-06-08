package gamepack.utily;

import java.util.Random;

public abstract class ProjectMethods
{
	//return the power of two of the value
	public static int powerOfTwo(int v)
	{
		int r = 0;
		while(v % 2 == 0)
		{
			v = v/2;
			r++;
		}
		return r+1;
	}
	
	//Give the color of the tile in an array depending of its value
	public static int[] getRGBColor(int power)
	{
		int r=0,g=0,b=0;
		if(power <= 7) //jusqu'� 64
		{
			r = 230;
			g = 210-power*20;
			b = 200-power*20;
		}
		else if(power <= 12) //jusqu'� 2048
		{
			r = 230-(power-7)*3;
			g = 210-(power-7)*5;
			b = 110-(power-7)*10;
		}
		
		return new int[]{r,g,b};
	}
	
	public static int alea()
	{
		Random rand = new Random();
		switch (rand.nextInt(3)) {
			case 0:
				return 1;
				break;
			case 1:
				return 2;
				break;
			case 2:
				return 4;
				break;
	
			default:
				return -1;
				break;
		}
		
	}
}
