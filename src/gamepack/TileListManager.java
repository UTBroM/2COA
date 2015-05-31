package gamepack;

import java.util.Iterator;
import java.util.List;

public class TileListManager 
{
	private TileList tileList;
	private List<Point> goodPositions;

	//Will compute the goodPositins where Tiles will be
	public TileListManager() 
	{
		tileList = new TileList();
	}
	
	//Return the TileList of the TileListManager to display it
	public TileList getTileList() 
	{
		return tileList;
	}
	
	//Create new tiles at the right positions
	public void generateNewTile()
	{
		
	}
	
	
	//Give each tile his direction and his arrived Tile(null if no arrived tile, None if no Direction)
	//Then it will set the arrived point (coordinnates of the arrivedTile if there is one)
	public void initMovement(Direction d)
	{
		System.out.println(d);
	}
	
	//Move each Tile in the right direction and set them at the good position if they passe their good position
	public int manageMovement(int FPS)
	{
		
		return 0;//temp
	}
	
	//call the function refreshFusion for each tile
	//if refreshFusion return true, delete the current tile from the list (null + remove)
	public void manageFusion()
	{
		
		
	}
	
	
}
