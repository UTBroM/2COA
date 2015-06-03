package gamepack;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileListManager
{
	private TileList	tileList;
	private List<Point>	goodPositions;
	
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
		Random alea = new Random();
		int tmp = alea.nextInt(goodPositions.size());
		int i = 0;
		Tile tmpTile = new Tile(goodPositions.get(tmp).getX(),goodPositions.get(tmp).getY());
		boolean existTile = true;
		
		
		while (existTile) 
		{
			tmp = alea.nextInt(goodPositions.size());
			tmpTile.setX(goodPositions.get(tmp).getX());
			tmpTile.setY(goodPositions.get(tmp).getY());
			
			while(i < tileList.getSize() && existTile)
			{
				if (!tmpTile.equals(tileList.getTile(i))) 
				{
					existTile = false;
				}
			}
			
		}
		this.tileList.addNewTile(tmpTile);
	}
	
	//Give each tile his direction and his arrived Tile(null if no arrived tile, None if no Direction)
	//Then it will set the arrived point (coordinnates of the arrivedTile if there is one)
	public void initMovement(Direction d)
	{
		System.out.println(d);
	}
	
	//Move each Tile in the right direction and set them at the good position if they passe their good position
	//return true if there is movement, false otherwise
	public boolean manageMovement(int FPS)
	{
		//init
		boolean trueIfMovement = false;				//For the state modification in WindowGame
		final float movementDurationInSec = 1.0f;
		float pixelPerFrame = 0;					//Speed of the tile
		
		for(int i = 0; i < tileList.getSize(); i++)		
		{
			//current Tile in the list
			Tile currentTile = tileList.getTile(i);
			Direction currentDirection = currentTile.getDirection();
			
			//if the tile has to move
			if(currentDirection != Direction.None)
			{
				trueIfMovement = true;
				//if the tile is not arrived
				if(!currentTile.isArrived())
				{
					//move the tile depending on the FPS
					pixelPerFrame = movementDurationInSec/FPS;
					if(currentDirection == Direction.Left)
						currentTile.move(-pixelPerFrame,0);
					else if(currentDirection == Direction.Right)
						currentTile.move(pixelPerFrame,0);
					else if(currentDirection == Direction.Down)
						currentTile.move(0,+pixelPerFrame);
					else if(currentDirection == Direction.Up)
						currentTile.move(0,-pixelPerFrame);
					
					//if the tile has gone too far (because of low framerate etc..) 
					if(pixelPerFrame > 1)
						currentTile.improvePosition();
				}
				//if the tile is arrived, then its Direction will be set to None
				else
					currentTile.setDirection(Direction.None);
				
			}
		}
		
		return trueIfMovement;
	}
	
	//call the function refreshFusion for each tile
	//if refreshFusion return true, delete the current tile from the list (null + remove)
	public void manageFusion()
	{
		for(int i = 0; i < tileList.getSize(); i++)		
		{
			if (this.tileList.getTile(i).refreshFusion())
			{
				this.tileList.remove(i);
			}
		}
	}
	
}