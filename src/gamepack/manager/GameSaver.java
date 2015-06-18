package gamepack.manager;

import gamepack.data.drawable.Bomb;
import gamepack.data.drawable.Tile;
import gamepack.data.drawable.TileMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GameSaver
{
	private String pathData;
	private String pathScore;
	private String pathHighscores;
	
	//Set the path
	public GameSaver(String pathData, String pathScore, String pathHighscores)
	{
		this.pathData = pathData;
		this.pathScore = pathScore;
		this.pathHighscores = pathHighscores;
		
		//if files are not available, create the files
		if(!areFilesAvailable())
		{
			writeInFile(pathData, "", false);
			writeInFile(pathScore, "0", false);
		}
	}
	
	//Return true if the files exist & are not empty
	public boolean areFilesAvailable()
	{
		//init
		File dataFile = new File(pathData);
		File scoreFile = new File(pathScore);
		
		//exist & not empty
		if(dataFile.exists() && dataFile.length() != 0)
			if (scoreFile.exists() && scoreFile.length() != 0)
			{
				return true;
			}
		return false;
	}
	
	//get the score saved in the score.txt
	public int getScore()
	{
		//create the scanner
		Scanner fileScanner = null;
		try
		{
			File file = new File(pathScore);
			if(file.length() != 0)
				fileScanner = new Scanner(file);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		//generate the tile list
		int score = 0;
		if(fileScanner != null)
		{
			while(fileScanner.hasNext())
			{
				//if we are not at the score
				if(fileScanner.hasNextInt())
				{
					score = fileScanner.nextInt();

				}
				
			}

			fileScanner.close();
			return score;
		}
		
		return score;
	}
	
	//return a list containing the saved tile
	public ArrayList<Tile> getSavedTileList()
	{
		//create the scanner
		Scanner fileScanner = null;
		try
		{
			File file = new File(pathData);
			if(file.length() != 0)
				fileScanner = new Scanner(file);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//generate the tile list
		if(fileScanner != null)
		{
			ArrayList<Tile> savedTile = new ArrayList<Tile>();
			while(fileScanner.hasNext())	//if there is a data to take after
			{
				if(fileScanner.hasNextInt())	//if this data is an integer
				{
					int v = fileScanner.nextInt();
					//if v < 0, it's a bomb
					if(v < 0)
						savedTile.add(new Bomb(0, 0, -v));
					//if v > 0, it's a classic tile
					else if(v > 0)
						savedTile.add(new Tile(0, 0, v));
					//if v = 0, the grid is empty at this position
					else
						savedTile.add(null);
					
				}
			}
			//close the fileScanner and return the list
			fileScanner.close();
			return savedTile;
		}
		
		
		return null;
	}
	
	//save the tile matrix in the file
	public void save(TileMatrix tMatrix, int score, String pseudo)
	{
		//Initialization
		String dataToSave = "";

		//Take each tile in the tile matrix
		for(int i = 0 ; i < tMatrix.getMatrixSize(); i++)
		{
			for(int j = 0 ; j < tMatrix.getMatrixSize(); j++)
			{
				//if the tile exists at the given position, register the tile only by using
				//its value
				if(tMatrix.get(j, i) != null)
				{
					if(tMatrix.get(j, i) instanceof Bomb)
						dataToSave += "-" + tMatrix.get(j, i) + " ";
					else 
						dataToSave += tMatrix.get(j, i) + " ";
					
				}
				//otherwise, set 0
				else
					dataToSave += "0" + " ";
			}
			//space at the end of each line
			dataToSave += "\n";
		}
		
		//save the matrix and the score
		writeInFile(pathData, dataToSave, false);
		writeInFile(pathScore, ""+score, false);
		
		//Save the pseudo if there is one
		if(!pseudo.equals(""))
		{
			writeInFile(pathHighscores, pseudo+":"+score+'\n', true);
		}
		
	}
	
	//Write $data in $path (not necessarily optimized as we create a new PrintWriter but better for clarity)
	private void writeInFile(String path, String data, boolean append)
	{
		System.out.println(path + "  " + data);
		//Initialization
		PrintWriter writer;
		try
		{
			//Writer is used to write in the file
			writer = new PrintWriter(new FileWriter(path, append));
			
			//Write in the file & close is
			if(!append)
				writer.print(data);
			else
				writer.append(data);
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//delete the save
	public void deleteSave()
	{
		writeInFile(pathScore, "", false);
		writeInFile(pathData, "", false);
	}
}
