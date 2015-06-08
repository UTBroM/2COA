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
		this(x, y, 2, size, tempsExplosion, 1);
	}
	
	public int getTempExplo()
	{
		return this.tempsExplosion;
	}
	public int getRayonExplo()
	{
		return this.rayonExplosion;
	}
	
}
