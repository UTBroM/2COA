package gamepack.manager;

import gamepack.data.Point;
import gamepack.data.drawable.TileMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//sauvegarde quand on ferme la fenêtre
public class GameSaver
{
	private Scanner fileScanner;
	
	//Initialize the file scanner if the file is not empty
	public GameSaver(String path)
	{
		fileScanner = null;
		try
		{
			File file = new File(path);
			if(file.length() == 0)
				fileScanner = new Scanner(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Fichier non ouvert.");
		}
		
	}
	
	
	//return the file
	private String readFile()
	{
		//Initialization
		String file = "";
		String l ="";
		
		//Read File
		l = fileScanner.nextLine();
		while (l != null)
		{
			file += l;
			l = null;
			if(fileScanner.hasNextLine())
				l = fileScanner.nextLine();
		}
		
		return file;
	}
	
	public TileMatrix getSavedTileList(int tileSize)
	{
		String savedFile = readFile();
		
	
		

		TileMatrix tMatrix = new TileMatrix(0,tileSize);
		return tMatrix;
	}
	
	public void save(TileMatrix tMatrix)
	{
		String file = "";
		
	
	}
}
