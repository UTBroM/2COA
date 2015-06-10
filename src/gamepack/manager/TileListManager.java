package gamepack.manager;

import gamepack.data.Point;
import gamepack.data.drawable.Bomb;
import gamepack.data.drawable.DrawableObject;
import gamepack.data.drawable.Tile;
import gamepack.data.drawable.TileList;
import gamepack.data.drawable.TileMatrix;
import gamepack.utility.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class TileListManager
{
	private TileMatrix tileList;
	private final PointMatrix goodPositions;
	private final int tileSize;
	private final Random rand;
	
	//Will compute the goodPositins where Tiles will be
	public TileListManager(int tileSize, ArrayList<Rectangle> rectangleList)
	{
		tileList = new TileMatrix(rectangleList.size(), tileSize);
		rand = new Random();
		this.tileSize = tileSize;
		
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
		System.out.println(goodPositions);
		
	}
	
	//receive the tileList with bad coordinates and compute the good positions
	public void loadGame(TileList fakeList)
	{
		
	}
	
	
	//Return the TileList of the TileListManager to display it and to save it
	public TileMatrix getTileList()
	{
		return tileList;
	}
		
	//Return the TileList of the TileListManager to display it and to save it
	public PointMatrix getPointMatrix()
	{
		return goodPositions;
	}
	
	//Create new tiles at the right positions
	public boolean generateNewTile()
	{
		Random alea = new Random();
		boolean isFree;
		Point currentGoodPosition;
		Tile currentTile;
		ArrayList<Point> goodFreePoint = new ArrayList<Point>();
		
		//Construction of a new TileList which contain only free space for new Tile
		int k = 0;
		for (int i = 0; i < goodPositions.size(); i++)
		{
			if(k % goodPositions.size() == 0)
				k = 0;
			
			currentGoodPosition = goodPositions.get(i).get(k);
			isFree = true;
			
			
			for (int j = 0; j < tileList.getLinearSize(); j++)
			{
				currentTile = tileList.getAtLinear(j);
				if(currentTile != null)
					if (currentGoodPosition.getX() == currentTile.getX() && currentGoodPosition.getY() == currentTile.getY())
					{
						isFree = false;
					}
			}
			if (isFree)
			{ //If any Tile in tileList is equal to the current goodPosition so it's a free tile
				goodFreePoint.add(new Point(currentGoodPosition.getX(), currentGoodPosition.getY()));
				isFree = true;
			}
		}
		
		//If after traversing all the goodPosition any Tile is free so we return an error
		if (goodFreePoint.isEmpty()) {
			return false;
		}
		else
		{
			//Choose randomly a free space and add the new Tile
			int tmp = alea.nextInt(goodFreePoint.size());
			if (rand.nextInt(20) == 0) {
				tileList.add(new Bomb(goodFreePoint.get(tmp).getX(), goodFreePoint.get(tmp).getY(), this.getRandomTileValue(),tileSize));
			} else {
				tileList.add(new Tile(goodFreePoint.get(tmp).getX(), goodFreePoint.get(tmp).getY(), this.getRandomTileValue(),tileSize));
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
		ArrayList<TileList> mainMatrix = new ArrayList<TileList>();
		ArrayList<Integer> listX = new ArrayList<Integer>();
		ArrayList<Integer> listY = new ArrayList<Integer>();
		
		//Generation des listes de X et de Y (comme la liste goodPositions mais ordonnée);
		
		int curX = 0;
		int curY = 0;
		
		for(int i = 0 ; i < this.goodPositions.size(); i++)
			for (Point curPoint : this.goodPositions.get(i))
			{
				int xOfCurPoint = curPoint.getX();
				int yOfCurPoint = curPoint.getY();
				
				if (xOfCurPoint > curX)
				{
					listX.add(xOfCurPoint);
					curX = xOfCurPoint;
				}
				if (yOfCurPoint > curY)
				{
					listY.add(yOfCurPoint);
					curY = yOfCurPoint;
				}
			}
		if (d == Direction.Right)
			Collections.reverse(listX);
		else if (d == Direction.Down)
			Collections.reverse(listY);
		
		//Gestion de l'appui sur la touche droite et gauche
		
		if (d == Direction.Left || d == Direction.Right)
		{
			this.tileList.sortY(); //Tri sur Y de la liste de tuiles afin de les regrouper par lignes
			
			int i = 0;
			curY = (int) this.tileList.getTile(0).getY();
			mainMatrix.add(new TileList());
			
			//On trie toutes les tuiles par ligne
			for (Tile curTile : this.tileList.gettList())
			{
				if (curY != curTile.getY())
				{
					i++;
					mainMatrix.add(new TileList());
					curY = (int) curTile.getY();
				}
				mainMatrix.get(i).add(curTile);
			}
			
			//On veut maintenant gérer le déplacement à proprement parler
			for (TileList curTileList : mainMatrix)// Pour chaque ligne 
			{
				curTileList.sortX();//Tri de chaque ligne
				if (d == Direction.Right)
					Collections.reverse(curTileList.gettList());
				
				Tile precTile = null;//La tuile précédente qui sert aux fusions
				boolean precTilefus = false;//un booléen qui évite de faire des fusions en chaine
				int collumn = 0;
				
				//On condidère chaque tuile
				for (Tile curTile : curTileList.gettList())//pour chaque colonne
				{
					curTile.setDirection(d);//On défini la direction
					//Si on obtient null pour precTile c'est que c'est la première tuile
					if (precTile == null)
					{
						curTile.setArrivedTile(null);
						curTile.setArrivedPoint(new Point(listX.get(collumn), (int) curTile.getY()));
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
						}
						else
						{
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
			this.tileList.sortX(); //Tri sur X de la liste de tuiles afin de les regrouper par lignes
			
			int i = 0;
			curX = (int) this.tileList.getTile(0).getX();
			mainMatrix.add(new TileList());
			
			//On trie toutes les tuiles par ligne
			for (Tile curTile : this.tileList.gettList())
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
		}
	}
	
	//Move each Tile in the right direction and set them at the good position if they passe their good position
	//return true if there is movement, false otherwise
	public boolean manageMovement(int FPS)
	{
		
		//init
		boolean trueIfMovement = false; //For the state modification in WindowGame
		final float pixelPerSecond = 2000.0f;
		float pixelPerFrame = 0; //Speed of the tile
		
		for (int i = 0; i < tileList.getSize(); i++)
		{
			//current Tile in the list
			Tile currentTile = tileList.getTile(i);
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
		
		return trueIfMovement;
	}
	
	//call the function refreshFusion for each tile
	//if refreshFusion return true, delete the current tile from the list (null + remove)
	public void manageFusion()
	{
		for (int i = 0; i < tileList.getSize(); i++)
		{
			Tile t = this.tileList.getTile(i);
			if (t.getArrivedTile() != null && t.refreshFusion())
			{
				this.tileList.remove(i);
			}
		}
	}	
}