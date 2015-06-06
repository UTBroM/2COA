package gamepack;

public abstract class ProjectMethods
{
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
}
