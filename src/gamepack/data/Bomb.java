package gamepack.data;


public class Bomb extends Tile{
	private int tempsExplosion;
	private int rayonExplosion;
	
	public Bomb(int x, int y, int value, int size, int tempsExplosion, int rayonExplosion)
	{
		super(x, y, value, size);
		this.tempsExplosion = tempsExplosion;
		this.rayonExplosion = rayonExplosion;
	}
	
	public Bomb(int x, int y, int size, int tempsExplosion)
	{
		super(x,y,size);
		this.tempsExplosion = tempsExplosion;
		this.rayonExplosion = rayonExplosion;
	}
	
}
