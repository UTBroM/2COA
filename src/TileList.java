import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class TileList {
	private Tile[][] tileList;
	private Point[][] positionList;
	private int windowSizeX;
	private int windowSizeY;
	private int rectangleSize;

	public TileList(int size) {
		tileList = new Tile[size][size];
		positionList = new Point[size][size];
		this.windowSizeX = 800;
		this.windowSizeY = 800;
		initPosList();
		initTileList();
	}

	public TileList(int size, int windowSizeX, int windowSizeY) {
		tileList = new Tile[size][size];
		positionList = new Point[size][size];
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeX;// X Pour avoir un carré en faite
		this.rectangleSize = 140;
		initPosList();
		initTileList();
	}

	public Tile[][] getTileList() {
		return this.tileList;
	}

	public int getDistanceTile() {
		return positionList[1][1].x - positionList[0][0].x;
	}

	private void initPosList() {
		int marginTop = 40 * 800 / windowSizeY + 20 * 800 / windowSizeY;
		int marginLeft = 40 * 800 / windowSizeX + 20 * 800 / windowSizeX;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				positionList[i][j] = new Point(marginLeft + 180 * j, marginTop + 180 * i);
			}
		}
	}

	private void initTileList() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tileList[i][j] = new Tile(new Rectangle(positionList[i][j].x, positionList[i][j].y, rectangleSize, rectangleSize));
			}
		}
	}

	public boolean move(Direction direction) { // Gérer les déplacement dans une
												// directions
		if (direction == Direction.Left) {
			// Bouge toutes les tuiles vers la gauche
			// Les tuiles bougés sont fusionnées avec leur destination si
			// nécessaire
			// Les tuiles fusionnées sont suprimées
			// De nouvelles tuiles sont créés aux endroits ou il n'y en a plus
		} else if (direction == Direction.Right) {
			// Bouge toutes les tuiles vers la droite
		} else if (direction == Direction.Down) {
			// Bouge toutes les tuiles vers le bas
		} else if (direction == Direction.Up) {
			// Bouge toutes les tuiles vers le haut
		}
		return false;
	}

	public boolean isMove(int direction, int x, int y) {// Regarder si une tuile
														// doit être bougee ou
														// pas
		if (direction == -1)// Gauche
		{
			if (y == 0)// Si la tuile est coll� au cot� gauche ...
				return false;
			else if (tileList[x][y - 1].getValue() == 0)// Si il n'y a rien �
														// gauche de la tuile
														// s�lectionn�e
				return true;
		} else if (direction == 1)// Droite
		{
			if (y == this.tileList[0].length - 1)
				return false;
			else if (tileList[x][y + 1].getValue() == 0)
				return true;
		} else if (direction == -2)// Bas
		{
			// Bouge toutes les tuiles vers le bas
			if (y == this.tileList[0].length - 1)
				return false;
			else if (tileList[x + 1][y].getValue() == 0)
				return true;
		} else if (direction == 2)// Haut
		{
			// Bouge toutes les tuiles vers le haut
			if (x == 0)
				return false;
			else if (tileList[x - 1][y].getValue() == 0)
				return true;
		}
		return false;
	}

	public boolean addTile() {
		int nbVides = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (tileList[i][j].isEmpty())
					nbVides++;
		if (nbVides == 0) {
			return false;
		} else {
			int randInt = new Random().nextInt(nbVides);
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++)
					if (tileList[i][j].isEmpty())
						if (randInt-- == 0)
							tileList[i][j].combine(null);
		}
		return true;
	}
}
