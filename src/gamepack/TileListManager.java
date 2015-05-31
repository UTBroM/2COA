package gamepack;

import java.util.Iterator;

public class TileListManager {
	private TileList tileList;

	public TileList getTileList() {
		return tileList;
	}
	public void setTileList(TileList tileList) {
		this.tileList = tileList;
	}
	
	public void fusion(){
		for (int i = 0; i < tileList.getSize(); i++) 
		{
			for (int j = 0; j < tileList.getSize(); j++) 
			{
				if (tileList.getTile(i).getCenterX() == tileList.getTile(j).getX() && tileList.getTile(i).getCenterY() == tileList.getTile(j).getY())
				{
					tileList.setTile(i,(int)tileList.getTile(i).getX(), (int)tileList.getTile(i).getY(), (tileList.getTile(i).getValue()) * 2);
					// Faut pas appeler un listener là ?
					tileList.remove(j);
				}
			}	
		}
	}
}
