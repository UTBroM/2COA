package gamepack.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameSaver
{
	private Scanner fileScanner;
	
	public GameSaver(String path)
	{
		try
		{
			fileScanner = new Scanner(new File(path));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Fichier non ouvert.");
		}
		
	}
	
	public String readFile()
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
}
