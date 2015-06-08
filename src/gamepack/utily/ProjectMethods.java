package gamepack.utily;

public abstract class ProjectMethods
{
	//return the power of two of the value
	static int powerOfTwo(int v)
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
	static int[] getRGBColor(int power)
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
}