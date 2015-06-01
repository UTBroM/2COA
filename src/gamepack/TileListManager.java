package gamepack;

import java.util.Random;

public class TileListManager
{
	private TileList	tileList;
	
	public void addTile()
	{
		int nbFreeTile = 16 - tileList.getSize();
		int randInt = new Random().nextInt(nbFreeTile + 1);
		
	}
	
	public void manageTileList()
	{
		// modifier la liste selon la position des points des rectangles (haut à gauche) 
		// savoir la position des tiles suivant leur rectangle dessiné 
	}
}
