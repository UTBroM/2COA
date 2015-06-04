package gamepack;

import java.util.ArrayList;
import java.util.Random;

public class TileListManager
{
	private TileList	tileList;
	private ArrayList<Point>	goodPositions;
	
	//Will compute the goodPositins where Tiles will be
	public TileListManager(int x, int y)
	{
		tileList = new TileList();
		this.goodPositions = new ArrayList<Point>(); 
		
		int min = (x < y ? x : y); // To let the grid be squared
		
		// delete min and replace by sizeX or by sizeY in the definitions if you no longer want to have a square
		
		int padX = 20 * min / 800;
		int padY = 20 * min / 800;
		int rectSizeX = 160 * min / 800;
		int rectSizeY = 160 * min / 800;
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				this.goodPositions.add(new Point(x + j * (rectSizeX + padX), y + i * (rectSizeY + padY)));
			}
		}
		
	}
	
	//Return the TileList of the TileListManager to display it
	public TileList getTileList()
	{
		return tileList;
	}
	
	//Create new tiles at the right positions
	public boolean generateNewTile()
	{
		Random alea = new Random();
		boolean isFree = true;
		Point currentGoodPosition;
		Tile currentTile;
		ArrayList<Point> goodFreeTile = new ArrayList<Point>();
		
		//Construction of a new TileList which contain only free space for new Tile
		for (int i = 0; i < goodPositions.size(); i++) {
			currentGoodPosition = goodPositions.get(i);
			
			for (int j = 0; j < tileList.getSize(); j++) {
				currentTile = tileList.getTile(j);
				
				if (currentGoodPosition.getX() == currentTile.getX() && currentGoodPosition.getY() == currentTile.getY())
				{
					isFree = false;
				}
			}
			if (isFree) {			//If any Tile in tileList is equal to the current goodPosition so it's a free tile
				goodFreeTile.add(new Point(currentGoodPosition.getX(),currentGoodPosition.getY()));
				isFree = true;
			}
		}
		
		//If after traversing all the goodPosition any Tile is free so we return an error
		if (goodFreeTile.isEmpty()) {
			return false;
		}else{
			//Choose randomly a free space and add the new Tile
			int tmp = alea.nextInt(goodFreeTile.size());
			tileList.addNewTile(new Tile(goodFreeTile.get(tmp).getX(), goodFreeTile.get(tmp).getY()));
			return true;
		}
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