package gamepack.manager;

import gamepack.data.Point;
import gamepack.data.PointMatrix;
import gamepack.data.drawable.Bomb;
import gamepack.data.drawable.Tile;
import gamepack.data.drawable.TileList;
import gamepack.data.drawable.TileMatrix;
import gamepack.utility.Direction;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class TileListManager
{
	private TileMatrix tileMatrix;
	private TileMatrix nextTileMatrix;
	private final PointMatrix goodPositions;
	private final int tileSize;
	private final Random rand;
	private int score;
	
	//Will compute the goodPositins where Tiles will be
	public TileListManager(int tileSize, ArrayList<Rectangle> rectangleList)
	{
		tileMatrix = new TileMatrix((int) Math.sqrt(rectangleList.size()), tileSize);
		//nextTileMatrix = new TileMatrix(tileMatrix);
		rand = new Random();
		this.tileSize = tileSize;
		this.score = 0;
		
		//Création de la liste deux dimensions de point
		this.goodPositions = new PointMatrix(rectangleList);
		
		/*int size = (int) Math.sqrt(rectangleList.size());
		ArrayList<Point> pointList = null;
		for(int i = 0; i < rectangleList.size(); i++)
		{
			Rectangle curRect = rectangleList.get(i);
			if(i % size == 0)
			{
				if(i != 0)
					this.goodPositions.add(pointList);
				pointList  = new ArrayList<Point>();
			}
			pointList.add(new Point((int) curRect.getX(), (int) curRect.getY()));
			
		}
		this.goodPositions.add(pointList);

		for(int i = 0; i < goodPositions.size(); i++)
			System.out.println(goodPositions.get(i));*/
		//System.out.println(goodPositions);
		
	}
	
	//receive the tileMatrix with bad coordinates and compute the good positions
	public void loadGame(TileList fakeList)
	{
		
	}
	
	//refresh the number of deplacement possible for a bomb and maybe ... BOOOOOMM !
	public void refreshBomb()
	{
		boolean isExplosed;
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for(int j = 0 ; j < tileMatrix.getMatrixSize(); j++)
			{
				Tile t = this.tileMatrix.get(i, j);
				if (t instanceof Bomb)
				{
					isExplosed = ((Bomb) t).minusRemainingMovement();
					if (isExplosed)
					{
						this.explosion(j , i, ((Bomb) t).getExplosionRadius());
					}
				}
			}
		}
	}
	
	//Let's BOOOOOOOOM !
	public void explosion(int x, int y, int explosionRadius)
	{
		for (int i = -explosionRadius; i <= explosionRadius; i++) {
			for (int j = -explosionRadius; j < -explosionRadius; j++) {
				int adjPosX = x + j;
				int adjPosY = y + i;
				boolean notOut = (0 <= adjPosX && adjPosX < this.tileMatrix.getMatrixSize() && 0 <= adjPosY && adjPosY < this.tileMatrix.getMatrixSize());
				if (notOut) {
					this.tileMatrix.deleteAt(adjPosX, adjPosY);
				}
			}
		}
		
	}
	
	//Return the TileList of the TileListManager to display it and to save it
	public TileMatrix getTileList()
	{
		return tileMatrix;
	}
		
	//Return the TileList of the TileListManager to display it and to save it
	public PointMatrix getPointMatrix()
	{
		return goodPositions;
	}
	
	public int getScore() {
		return score;
	}

	//Create new tiles at the right positions
	public boolean generateNewTile()
	{
		boolean isFree;
		Point currentGoodPosition;
		Tile currentTile;
		ArrayList<Point> goodFreePoint = new ArrayList<Point>();
		
		//Construction of a new TileList which contain only free space for new Tile
		for (int i = 0; i < goodPositions.size(); i++)
		{
			for(int k = 0; k < goodPositions.size();k ++)
			{
				currentGoodPosition = goodPositions.getAt(i,k);
				isFree = true;
				
				
				for (int j = 0; j < tileMatrix.getLinearSize(); j++)
				{
	
					currentTile = tileMatrix.getAtLinear(j);
					if(currentTile != null)
						if (currentGoodPosition.getX() == currentTile.getX() && currentGoodPosition.getY() == currentTile.getY())
						{
							isFree = false;
						}
				}
				if (isFree)
				{ //If any Tile in tileMatrix is equal to the current goodPosition so it's a free tile
					goodFreePoint.add(currentGoodPosition);
					isFree = true;
				}
			}
		}
		
		//If after traversing all the goodPosition any Tile is free so we return an error
		if (goodFreePoint.isEmpty()) {
			return false;
		}
		else
		{
			//Choose randomly a free space and add the new Tile
			int randInt = rand.nextInt(goodFreePoint.size());
			//Variable which contain the x position in the matrix
			int xNewTile = goodPositions.getPositionsOf(goodFreePoint.get(randInt))[0];
			//Variable which contain the y position in the matrix
			int yNewTile = goodPositions.getPositionsOf(goodFreePoint.get(randInt))[1];
						
			if (rand.nextInt(20) != 0) {
				Bomb newBomb = new Bomb(goodFreePoint.get(randInt).getX(), goodFreePoint.get(randInt).getY(), this.getRandomTileValue(),tileSize); 
				tileMatrix.setAt(xNewTile, yNewTile, newBomb);
			} else {
				Tile newTile = new Tile(goodFreePoint.get(randInt).getX(), goodFreePoint.get(randInt).getY(), this.getRandomTileValue(),tileSize);
				tileMatrix.setAt(xNewTile, yNewTile, newTile);
			}
			return true;
		}
	}
	
	public int getRandomTileValue()
	{
		int tmp;
		switch (rand.nextInt(4)) {
		case 0:
			tmp = 4;
			break;
			
		default:
			tmp = 2;
			break;
		}
		
		return tmp;
	}
	
	//Give each tile his direction and his arrived Tile(null if no arrived tile, None if no Direction)
	//Then it will set the arrived point (coordinates of the arrivedTile if there is one)
	public void initMovement(Direction d)
	{
		//Initialization of the next matrice to compute on it the next state of the grid
		nextTileMatrix = tileMatrix;
		//TileMatrix mainMatrix = nextTileMatrix;
		
		Tile curTile, prevTile;
		int x_init, y_init, x_end, y_end, x_delta, y_delta;
		boolean prevTileFus;
		int size = tileMatrix.getMatrixSize();
		/*
		// Compute how to parse the matrix :
		if(d == Direction.Left)
		{
			x_init = 0;
			x_end = size;
			y_init = 0;
			y_end = size;
			x_delta = 1;
			y_delta = 1;
		}
		else if(d == Direction.Right)
		{
			x_init = size-1;
			x_end = 0;
			y_init = 0;
			y_end = size;
			x_delta = -1;
			y_delta = 1;
		}
		else if(d == Direction.Up)
		{
			x_init = 0;
			x_end = size;
			y_init = 
			y_end = 
			x_delta = 
			y_delta = 
		}
		else if(d == Direction.Down)
		{
			x_init = 
			x_end = 
			y_init = 
			y_end = 
			x_delta = 
			y_delta = 
		}
		
		
		for(int y = y_init;y!=y_end;y++)
		{
			prevTile = null;
			for(int x = x_init;x<x_end;x++)
			{

				
				curTile = this.nextTileMatrix.get(x, y);
				if(prevTile == null)
				{
					curTile.setPrev();
					curTile.setArrivedTile(null);
					curTile.setArrivedPoint(prevTile.getArrivedPoint());
					prevTilefus = false;
				}
				else if(curTile.getValue() == prevTile.getValue() && !prevTilefus)
				{
					curTile.setMergedPrev(prevTile);
					curTile.setArrivedTile(prevTile);
					curTile.setArrivedPoint(prevTile.getArrivedPoint());
					precTilefus = true;
					this.score += curTile.getValue()* 2;
				}
				else
				{
					curTile.setPrev();
					curTile.setArrivedTile(null);
					//On récupère le point de la tuile précédente dans la liste des X possibles et on prend le suivant
					curTile.setArrivedPoint(new Point(listX.get(listX.indexOf(precTile.getArrivedPoint().getX()) + 1), (int) curTile.getY()));/***********/
					/*prevTilefus = false;
					collumn++;
				}
				prevTile = curTile;
			}
		}*/
		
		//Computation on the nextMatrix (no sort, add methods to the matrix if necessary, avoid using method with "(useless)" comment above it)
		//END
		/**
					curTile.setDirection(d);//On défini la direction
					//Si on obtient null pour precTile c'est que c'est la première tuile
					if (precTile == null)
					{
						curTile.setPrev();
						curTile.setArrivedTile(null);
						curTile.setArrivedPoint(new Point(listX.get(collumn), (int) curTile.getY()));
						precTile = curTile;
					}
					else
					{
						//On check si les valeurs sont égales et si on avait pas déjà fait une fusion avant
						if (curTile.getValue() == precTile.getValue() && !precTilefus)
						{
							curTile.setMergedPrev(precTile);
							curTile.setArrivedTile(precTile);
							curTile.setArrivedPoint(precTile.getArrivedPoint());
							precTile = curTile;
							precTilefus = true;
							this.score += curTile.getValue()* 2;
						}
						else
						{
							curTile.setPrev();
							curTile.setArrivedTile(null);//On se retrouve sur une case vide du coup
							//On récupère le point de la tuile précédente dans la liste des X possibles et on prend le suivant
							curTile.setArrivedPoint(new Point(listX.get(listX.indexOf(precTile.getArrivedPoint().getX()) + 1), (int) curTile.getY()));
							precTile = curTile;
							precTilefus = false;
							collumn++;
						}
					}
				}
			}
		}
		
		else if (d == Direction.Down || d == Direction.Up)
		{
			this.tileMatrix.sortX(); //Tri sur X de la liste de tuiles afin de les regrouper par lignes
			
			int i = 0;
			curX = (int) this.tileMatrix.getTile(0).getX();
			mainMatrix.add(new TileList());
			
			//On trie toutes les tuiles par ligne
			for (Tile curTile : this.tileMatrix.gettList())
			{
				if (curX != curTile.getX())
				{
					i++;
					mainMatrix.add(new TileList());
					curX = (int) curTile.getX();
				}
				
				mainMatrix.get(i).add(curTile);
			}
			
			//On veut maintenant gérer le déplacement à proprement parler
			for (TileList curTileList : mainMatrix)
			{
				curTileList.sortY();//Tri de chaque ligne
				
				if (d == Direction.Down)
					Collections.reverse(curTileList.gettList());
				
				Tile precTile = null;//La tuile précédente qui sert aux fusions
				boolean precTilefus = false;//un booléen qui évite de faire des fusions en chaine
				int collumn = 0;
				
				//On condidère chaque tuile
				for (Tile curTile : curTileList.gettList())
				{
					curTile.setDirection(d);//On défini la direction
					//Si on obtient null pour precTile c'est que c'est la première tuile
					if (precTile == null)
					{
						curTile.setArrivedTile(null);
						curTile.setArrivedPoint(new Point((int)curTile.getX(), listY.get(collumn)));
						precTile = curTile;
					}
					else
					{
						//On check si les valeurs sont égales et si on avait pas déjà fait une fusion avant
						if (curTile.getValue() == precTile.getValue() && !precTilefus)
						{
							curTile.setArrivedTile(precTile);
							curTile.setArrivedPoint(precTile.getArrivedPoint());
							precTile = curTile;
							precTilefus = true;
							this.score += curTile.getValue()* 2;
						}
						else
						{
							curTile.setArrivedTile(null);//On se retrouve sur une case vide du coup
							//On récupère le point de la tuile précédente dans la liste des Y possibles et on prend le suivant
							curTile.setArrivedPoint(new Point((int) curTile.getX(), listY.get(listY.indexOf(precTile.getArrivedPoint().getY()) + 1)));
							precTile = curTile;
							precTilefus = false;
							collumn++;
						}
					}
				}
			}
		}*/
	}
	
	//Move each Tile in the right direction and set them at the good position if they passe their good position
	//return true if there is movement, false otherwise
	public boolean manageMovement(int FPS)
	{
		
		//init
		boolean trueIfMovement = false; //For the state modification in WindowGame
		final float pixelPerSecond = 2000.0f;
		float pixelPerFrame = 0; //Speed of the tile
		
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for(int j = 0; j < tileMatrix.getMatrixSize(); j++)
			{
				//current Tile in the list
				Tile currentTile = tileMatrix.get(j, i);
				if(currentTile != null)
				{
					
				Direction currentDirection = currentTile.getDirection();
				
					//if the tile has to move
					if (currentDirection != Direction.None)
					{
						trueIfMovement = true;
						//if the tile is not arrived
						if (!currentTile.isArrived())
						{
							//move the tile depending on the FPS
							pixelPerFrame = pixelPerSecond/FPS;
							if (currentDirection == Direction.Left)
								currentTile.move(-pixelPerFrame, 0);
							else if (currentDirection == Direction.Right)
								currentTile.move(pixelPerFrame, 0);
							else if (currentDirection == Direction.Down)
								currentTile.move(0, +pixelPerFrame);
							else if (currentDirection == Direction.Up)
								currentTile.move(0, -pixelPerFrame);
							
							//if the tile has gone too far (because of low framerate etc..) 
							currentTile.improvePosition();
						}
						//if the tile is arrived, then its Direction will be set to None
						else
							currentTile.setDirection(Direction.None);
						
					}
				}
			}
		}
		
		return trueIfMovement;
	}
	
	//call the function refreshFusion for each tile
	//if refreshFusion return true, delete the current tile from the list (null + remove)
	public void manageFusion()
	{
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for(int j = 0 ; j < tileMatrix.getMatrixSize(); j++)
			{
				Tile t = this.tileMatrix.get(i, j);
				if(t != null)
				{
					if (t.getArrivedTile() != null && t.refreshFusion())
					{
						this.tileMatrix.setAt(i, j, null);
					}
				}
			}
		}
	}
	
	public void undo()
	{
		for (int i = 0; i < tileMatrix.getMatrixSize(); i++)
		{
			for(int j = 0 ; j < tileMatrix.getMatrixSize(); j++)
			{
				Tile t = this.tileMatrix.get(i, j);
				if(t != null)
				{
					if(t.getMergedTile() != null)
					{
						tileMatrix.add(t.getMergedTile());
					}
					t.undo();
				}
			}
		}
	}
}
