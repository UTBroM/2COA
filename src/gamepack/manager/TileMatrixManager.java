package gamepack.manager;

import gamepack.data.Point;
import gamepack.data.PointMatrix;
import gamepack.data.drawable.Bomb;
import gamepack.data.drawable.Tile;
import gamepack.data.drawable.TileMatrix;
import gamepack.utility.Direction;
import gamepack.utility.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class TileMatrixManager
{
	//ATTRIBUTES
	private TileMatrix tileMatrix;
	private TileMatrix nextTileMatrix;
	private TileMatrix prevTileMatrix;
	private final PointMatrix goodPositions;
	private final Random rand;
	private int score;
	
	//METHODS
	// Will compute the goodPositins where Tiles will be
	public TileMatrixManager(ArrayList<Rectangle> rectangleList)
	{
		tileMatrix = new TileMatrix((int) Math.sqrt(rectangleList.size()), (int) rectangleList.get(0).getWidth());
		nextTileMatrix = tileMatrix;
		rand = new Random();
		this.score = 0;
		
		// Create the point matrix with the top-left positions of the rectangles
		// computed in the Grid
		this.goodPositions = new PointMatrix(rectangleList);
		
	}
	
	// Constructor by save
	public TileMatrixManager(ArrayList<Rectangle> rectangleList, ArrayList<Tile> savedTileList, int savedScore)
	{
		this(rectangleList);
		score = savedScore;
		//create the tile matrix
		for (int y = 0; y < tileMatrix.getMatrixSize(); y++)
			for (int x = 0; x < tileMatrix.getMatrixSize(); x++)
			{
				Tile currentTile = savedTileList.get(y * tileMatrix.getMatrixSize() + x);
				if (currentTile != null)
				{
					tileMatrix.setAt(x, y, currentTile);
					currentTile.setX(goodPositions.get(x, y).getX());
					tileMatrix.get(x, y).setY(goodPositions.get(x, y).getY());
				}
			}
		
		nextTileMatrix = tileMatrix;
	}
	
	// refresh the number of deplacement possible for a bomb and maybe ...
	// BOOOOOMM !
	public void refreshBomb()
	{
		boolean isExplosed;
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for (int j = 0; j < tileMatrix.getMatrixSize(); j++)
			{
				Tile currentTile = this.tileMatrix.get(i, j);
				if (currentTile instanceof Bomb)
				{
					isExplosed = ((Bomb) currentTile).minusRemainingMovement();
					if (isExplosed)
					{
						this.explosion(j, i, ((Bomb) currentTile).getExplosionRadius());
					}
				}
			}
		}
	}
	
	// Let's BOOOOOOOOM ! #bingbadaboum boum bim boum boum
	public void explosion(int x, int y, int explosionRadius)
	{
		for (int i = -explosionRadius; i <= explosionRadius; i++)
		{
			for (int j = -explosionRadius; j <= explosionRadius; j++)
			{
				int adjPosX = x + j;
				int adjPosY = y + i;
				boolean notOut = (0 <= adjPosX && adjPosX < this.tileMatrix.getMatrixSize() && 0 <= adjPosY && adjPosY < this.tileMatrix.getMatrixSize());
				if (notOut)
				{
					this.nextTileMatrix.deleteAt(adjPosX, adjPosY);
				}
			}
		}
		
	}
	
	// Return the TileMatrix of the TileMatrixManager to display it and to save it
	public TileMatrix getTileMatrix()
	{
		return tileMatrix;
	}
	
	// Return the TileMatrix of the TileMatrixManager to display it and to save it
	public TileMatrix getNextTileMatrix()
	{
		return nextTileMatrix;
	}
	
	// return the score
	public int getScore()
	{
		return score;
	}
	
	// Create new tiles at the right positions
	public boolean generateNewTile()
	{
		// Initializations
		boolean isFree;
		Point currentGoodPosition;
		Tile currentTile;
		ArrayList<Point> goodFreePoint = new ArrayList<Point>();
		
		// Construction of a new List of point which contain only free point for
		// new Tile
		for (int i = 0; i < goodPositions.size(); i++)
		{
			for (int j = 0; j < goodPositions.size(); j++)
			{
				// if the tile at the j and i position in the matrix is null,
				// then this position is free
				if (nextTileMatrix.get(j, i) == null)
				{
					currentGoodPosition = goodPositions.get(j, i);
					goodFreePoint.add(currentGoodPosition);
				}
			}
		}
		// If after traversing all the goodPosition any Tile is free so we
		// return an error
		if (goodFreePoint.isEmpty())
		{
			return false;
		}
		else
		{
			// Choose randomly a free space and add the new Tile
			int randInt = rand.nextInt(goodFreePoint.size());
			// Variable which contain the x position in the matrix
			int xNewTile = goodPositions.getPositionsOf(goodFreePoint.get(randInt))[0];
			// Variable which contain the y position in the matrix
			int yNewTile = goodPositions.getPositionsOf(goodFreePoint.get(randInt))[1];
			// System.out.println(nextTileMatrix);
			if (rand.nextInt(20) == 0)
			{
				Bomb newBomb = new Bomb(goodFreePoint.get(randInt).getX(), goodFreePoint.get(randInt).getY(), this.getRandomTileValue());
				nextTileMatrix.setAt(xNewTile, yNewTile, newBomb);
			}
			else
			{
				Tile newTile = new Tile(goodFreePoint.get(randInt).getX(), goodFreePoint.get(randInt).getY(), this.getRandomTileValue());
				nextTileMatrix.setAt(xNewTile, yNewTile, newTile);
			}
			return true;
		}
	}
	
	// Give a random tile value for a Tile
	public int getRandomTileValue()
	{
		int value;
		switch (rand.nextInt(4))
		{
			case 0:
				value = 4;
				break;
			
			default:
				value = 2;
				break;
		}
		
		return value;
	}
	
	// Give each tile his direction and his arrived Tile(null if no arrived tile, None if no Direction)
	// Then it will set the arrived point (coordinates of the arrivedTile if there is one)
	public void initMovement(Direction d)
	{
		// Initialization of the next matrice to compute on it the next state of
		// the grid
		tileMatrix = new TileMatrix(nextTileMatrix);
		nextTileMatrix.setDirection(d);
		
		// Computation on the nextMatrix
		int size = nextTileMatrix.getMatrixSize();
		
		// Initialization of the list of column on line
		ArrayList<ArrayList<Tile>> lineOrColumnList = new ArrayList<ArrayList<Tile>>();
		boolean line = false;
		
		// if left or right, we make the line list
		if (d == Direction.Right || d == Direction.Left)
			for (int i = 0; i < nextTileMatrix.getMatrixSize(); i++)
			{
				lineOrColumnList.add(nextTileMatrix.getLine(i));
				line = true;
			}
		
		// if up or down, we make the column list
		if (d == Direction.Down || d == Direction.Up)
			for (int i = 0; i < nextTileMatrix.getMatrixSize(); i++)
			{
				lineOrColumnList.add(nextTileMatrix.getColumn(i));
			}
		
		for (int y = 0; y < lineOrColumnList.size(); y++)
		{
			// Initializations
			ArrayList<Tile> lineOrColumn = lineOrColumnList.get(y);
			Tile curTile = null, prevTile = null;
			
			// Revert the line/column if we go right or down (simplify
			// computation)
			boolean revert = d == Direction.Right || d == Direction.Down;
			if (revert)
				Collections.reverse(lineOrColumn); // O(n)
				
			// we go through the line or the column
			for (int x = 0; x < lineOrColumn.size(); x++)
			{
				
				// Ok so from here it will get more complicated
				// The goal from now is to go through the column or the line of
				// tile from the left to the right
				// We will compute like that
				// curTile is the tile we are on
				// prevTile is the previousTile that was not null
				// x go through the ArrayList, so it can go in any direction as
				// the ArrayList lineOrColumn is used for the four directions, same for y
				// To simplify, one can imagine that the reverse and the use of the same variable
				// for line and column is made to turn every situation into a "press left" situation
				// where we will go through the lines from the left to the right to do the following computations:
				curTile = lineOrColumn.get(x);
				if (curTile != null)
				{
					// if the previous tile is null, then we are at the first non-null tile in the line
					// we have to put it at the full left of the grid & matrice
					if (prevTile == null)
					{
						if (line)
						{
							if (!revert)
								curTile.setArrivedPoint(goodPositions.get(0, y));
							else
								curTile.setArrivedPoint(goodPositions.get(size - 1, y));
						}
						else
						{
							if (!revert)
								curTile.setArrivedPoint(goodPositions.get(y, 0));
							else
								curTile.setArrivedPoint(goodPositions.get(y, size - 1));
						}
						curTile.setArrivedTile(null);
						lineOrColumn.set(0, curTile);
						if (x != 0)
							lineOrColumn.set(x, null);
					}
					//If we are not at the first tile
					else
					{
						
						// if the tile will fusion
						if (curTile.getValue() == prevTile.getValue() && prevTile.getArrivedTile() == null)
						{
							curTile.setArrivedPoint(new Point((int) prevTile.getArrivedPointX(), (int) prevTile.getArrivedPointY()));
							curTile.setArrivedTile(prevTile);
							lineOrColumn.set(x, null);
						}
						// otherwise
						else
						{
							
							Point prevTilePoint = prevTile.getArrivedPoint();
							int xPoint = goodPositions.getPositionsOf(prevTilePoint)[0];
							int yPoint = goodPositions.getPositionsOf(prevTilePoint)[1];
							
							Point ArrPoint = null;
							if (line)
							{
								if (xPoint + 1 > size)
									ArrPoint = goodPositions.get(xPoint, yPoint);
								else
								{
									if (!revert)
										ArrPoint = goodPositions.get(xPoint + 1, yPoint);
									else
										ArrPoint = goodPositions.get(xPoint - 1, yPoint);
									
								}
							}
							else
							{
								if (yPoint + 1 > size)
									ArrPoint = goodPositions.get(xPoint, yPoint);
								else
								{
									if (!revert)
										ArrPoint = goodPositions.get(xPoint, yPoint + 1);
									else
										ArrPoint = goodPositions.get(xPoint, yPoint - 1);
									
								}
							}
							
							curTile.setArrivedPoint(ArrPoint);
							
							//Change the tile position in the list
							
							// utiliser pour simplifier plus tard
							/*
							 * lineOrColumn.set(x, null);
							 * int newX =
							 * goodPositions.getPositionsOf(ArrPoint)[0];
							 * lineOrColumn.set(newX, curTile);
							 */
							boolean changeSet = false;
							
							if (line)
							{
								if (!revert)
									changeSet = xPoint + 1 != x;
								else
									changeSet = size - 1 - xPoint + 1 != x;
							}
							else
							{
								if (!revert)
									changeSet = yPoint + 1 != x;
								else
									changeSet = size - 1 - yPoint + 1 != x;
							}
							if (changeSet)
							{
								if (line)
								{
									lineOrColumn.set(x, null);
									if (!revert)
										lineOrColumn.set(xPoint + 1, curTile);
									else
										lineOrColumn.set(size - 1 - xPoint + 1, curTile);
								}
								else
								{
									lineOrColumn.set(x, null);
									if (!revert)
										lineOrColumn.set(yPoint + 1, curTile);
									else
										lineOrColumn.set(size - 1 - yPoint + 1, curTile);
								}
								
							}
							
						}
					}
					//if the current Tile is not null
					prevTile = curTile;
				}
			}
			// revert again if we have reverted only for computation
			if (revert)
				Collections.reverse(lineOrColumn);
			
			//re-add the line or the column to the matrix
			if (line) //O(1)
				nextTileMatrix.setLine(y, lineOrColumn);
			else
				//O(n)
				nextTileMatrix.setColumn(y, lineOrColumn);
			
		}
	}
	
	// Move each Tile in the right direction and set them at the good position
	// if they passe their good position
	// return true if there is movement, false otherwise
	public boolean manageMovement(int FPS)
	{
		
		// init
		boolean trueIfMovement = false; // For the state modification in WindowGame
		final float pixelPerSecond = 2000.0f;
		float pixelPerFrame = 0; // Speed of the tile
		
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for (int j = 0; j < tileMatrix.getMatrixSize(); j++)
			{
				// current Tile in the list
				Tile currentTile = tileMatrix.get(j, i);
				if (currentTile != null)
				{
					Direction currentDirection = currentTile.getDirection();
					
					// if the tile has to move
					if (currentDirection != Direction.None)
					{
						// if the tile is not arrived
						if (!currentTile.isArrived())
						{
							trueIfMovement = true;
							// move the tile depending on the FPS
							pixelPerFrame = pixelPerSecond / FPS;
							if (currentDirection == Direction.Left)
								currentTile.move(-pixelPerFrame, 0);
							else if (currentDirection == Direction.Right)
								currentTile.move(pixelPerFrame, 0);
							else if (currentDirection == Direction.Down)
								currentTile.move(0, +pixelPerFrame);
							else if (currentDirection == Direction.Up)
								currentTile.move(0, -pixelPerFrame);
							
							// if the tile has gone too far (because of low
							// framerate etc..)
							currentTile.improvePosition();
						}
						// if the tile is arrived, then its Direction will be
						// set to None
						else
							currentTile.setDirection(Direction.None);
						
					}
				}
			}
		}
		
		return trueIfMovement;
	}
	
	// call the function refreshFusion for each tile
	public void manageFusion()
	{
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for (int j = 0; j < tileMatrix.getMatrixSize(); j++)
			{
				Tile t = this.tileMatrix.get(i, j);
				if (t != null)
				{
					if (t.refreshFusion())
						score += t.getValue();
					//if there is a fusion, the tile double its value, and the arrivedTile will be deleted
					//when the tileMatrix will be deleted, at the beginning of the initMovement
				}
			}
		}
	}
	
	public GameState isOver()
	{
		boolean win = false, lose = true;
		for (int i = 0; i < nextTileMatrix.getMatrixSize(); i++)
			for (int j = 0; j < nextTileMatrix.getMatrixSize(); j++)
			{
				Tile t = this.nextTileMatrix.get(i, j);
				if (t == null)
				{
					lose = false;
				}
				else if (t.getValue() >= 2048)// Win value
					win = true;
			}
		if (win)
			return GameState.Win;
		else if (lose)
		{
			for (int i = 0; i < nextTileMatrix.getMatrixSize(); i++)
				for (int j = 0; j < nextTileMatrix.getMatrixSize(); j++)
				{
					Tile t, t2, t3;
					t = this.nextTileMatrix.get(i, j);
					if (t != null)
					{
						if (i < tileMatrix.getMatrixSize() - 1 && (t2 = this.nextTileMatrix.get(i + 1, j)) != null)
						{
							if (t.getValue() == t2.getValue())
								lose = false;
						}
						if (j < nextTileMatrix.getMatrixSize() - 1 && (t3 = this.nextTileMatrix.get(i, j + 1)) != null)
						{
							if (t.getValue() == t3.getValue())
								lose = false;
						}
					}
				}
			if (lose)
				return GameState.Lose;
		}
		return GameState.Ongoing;
	}
}
