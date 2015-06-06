package gamepack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class TileListManager
{
	private TileList tileList;
	private ArrayList<Point> goodPositions;
	private final int tileSize;
	
	//Will compute the goodPositins where Tiles will be
	public TileListManager(int tileSize, Collection<Rectangle> rectangleList)
	{
		tileList = new TileList();
		this.tileSize = tileSize;
		
		this.goodPositions = new ArrayList<Point>();
		
		for (Rectangle curRect : rectangleList)
		{
			this.goodPositions.add(new Point((int) curRect.getX(), (int) curRect.getY()));
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
		for (int i = 0; i < goodPositions.size(); i++)
		{
			currentGoodPosition = goodPositions.get(i);
			
			for (int j = 0; j < tileList.getSize(); j++)
			{
				currentTile = tileList.getTile(j);
				
				if (currentGoodPosition.getX() == currentTile.getX() && currentGoodPosition.getY() == currentTile.getY())
				{
					isFree = false;
				}
			}
			if (isFree)
			{ //If any Tile in tileList is equal to the current goodPosition so it's a free tile
				goodFreeTile.add(new Point(currentGoodPosition.getX(), currentGoodPosition.getY()));
				isFree = true;
			}
		}
		
		//If after traversing all the goodPosition any Tile is free so we return an error
		if (goodFreeTile.isEmpty()) {
			System.out.println("goodFreeTile is empty");
			return false;
		}
		else
		{
			//Choose randomly a free space and add the new Tile
			int tmp = alea.nextInt(goodFreeTile.size());
			System.out.println("tmp : " + tmp);
			System.out.println("new Tile x : " + goodFreeTile.get(tmp).getX()+"new Tile y : " + goodFreeTile.get(tmp).getY() );
			tileList.addNewTile(goodFreeTile.get(tmp).getX(), goodFreeTile.get(tmp).getY(), tileSize);

			return true;
		}
	}
	
	//Give each tile his direction and his arrived Tile(null if no arrived tile, None if no Direction)
	//Then it will set the arrived point (coordinates of the arrivedTile if there is one)
	public void initMovement(Direction d)
	{
		System.out.println(d);
		ArrayList<TileList> swagList = new ArrayList<TileList>();
		ArrayList<Integer> listX = new ArrayList<Integer>();
		ArrayList<Integer> listY = new ArrayList<Integer>();
		
		//Generation des listes de X et de Y (comme la liste goodPositions mais ordonnée);
		
		int curX = 0;
		int curY = 0;
		
		for (Point curPoint : this.goodPositions)
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
			swagList.add(new TileList());
			
			//On trie toutes les tuiles par ligne
			for (Tile curTile : this.tileList.gettList())
			{
				if (curY != curTile.getY())
				{
					i++;
					swagList.add(new TileList());
					curY = (int) curTile.getY();
				}
				swagList.get(i).add(curTile);
			}
			
			//On veut maintenant gérer le déplacement à proprement parler
			for (TileList curTileList : swagList)// Pour chaque ligne 
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
			swagList.add(new TileList());
			
			//On trie toutes les tuiles par ligne
			for (Tile curTile : this.tileList.gettList())
			{
				if (curX != curTile.getX())
				{
					i++;
					swagList.add(new TileList());
					curX = (int) curTile.getX();
				}
				
				swagList.get(i).add(curTile);
			}
			
			//On veut maintenant gérer le déplacement à proprement parler
			for (TileList curTileList : swagList)
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