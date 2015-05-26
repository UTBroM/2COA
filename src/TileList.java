
public class TileList {
	private Tile[][] tileList;
	private Point[][] positionList;
	private int windowSizeX;
	private int windowSizeY;

	public TileList(int size) {
		tileList = new Tile[size][size];
		positionList = new Point[size][size];
		this.windowSizeX = 800;
		this.windowSizeY = 800;
		initPosList();
	}

	public TileList(int size, int windowSizeX, int windowSizeY) {
		tileList = new Tile[size][size];
		positionList = new Point[size][size];
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		initPosList();
	}

	private void initPosList()
	{
		int marginTop = 40*800/windowSizeY + 20*800/windowSizeY;
		int marginLeft = 40*800/windowSizeX + 20*800/windowSizeX;
		int i = 0, j = 0;
		for(Point[] tab : positionList)
		{
			for(Point pt : tab)
			{
				pt = new Point(marginLeft + 180*(j++)*800/windowSizeX, marginTop + 180*i*800/windowSizeY);
			}
			++i;
		}
	}
	
	public boolean move(int direction)
	{
		if(direction == -1)
		{
			//Bouge toutes les tuiles vers la gouche
		}
		else if(direction == 1)
		{
			//Bouge toutes les tuiles vers la droite
		}
		else if(direction == -2)
		{
			//Bouge toutes les tuiles vers le bas
		}
		else if(direction == 2)
		{
			//Bouge toutes les tuiles vers le haut
		}
		return false;
	}
	
	public boolean isMove(int direction, int x, int y)
	{
		if(direction == -1)//Gauche
		{
			if(y == 0)// Si la tuile est collé au coté gauche ...
				return false;
			else if(tileList[x][y-1].getValue() == 0)// Si il n'y a rien à gauche de la tuile sélectionnée 
				return true;
		}
		else if(direction == 1)//Droite
		{
			if(y == this.tileList[0].length - 1)
				return false;
			else if(tileList[x][y+1].getValue() == 0)
				return true;
		}
		else if(direction == -2)//Bas
		{
			//Bouge toutes les tuiles vers le bas
			if(y == this.tileList[0].length - 1)
				return false;
			else if(tileList[x+1][y].getValue() == 0)
				return true;
		}
		else if(direction == 2)//Haut
		{
			//Bouge toutes les tuiles vers le haut
			if(x == 0)
				return false;
			else if(tileList[x-1][y].getValue() == 0)
				return true;
		}
		return false;
	}
}
