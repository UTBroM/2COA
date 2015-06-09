package gamepack.view;

import gamepack.data.drawable.TileList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameSaver
{
	private Scanner fileScanner;
	
	public GameSaver(String path)
	{
		fileScanner = null;
		try
		{
			fileScanner = new Scanner(new File(path));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Fichier non ouvert.");
		}
		
	}
	
	
	
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
	
	public TileList getSavedTileList()
	{
		String savedFile = readFile();
		TileList list = new TileList();
		
	
		
		
		return list;
	}
}
