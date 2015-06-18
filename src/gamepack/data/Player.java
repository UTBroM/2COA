package gamepack.data;

public class Player implements Comparable<Player>
{
	private String name;
	private int score;
	
	public Player(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public void setName(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}

	public int getScore()
	{
		return score;
	}

	public String toString()
	{
		return name + ": " + score;
	}


	public int compareTo(Player p)
	{
		if(score > p.score)
			return -1;
		else if(score < p.score)
			return 1;
		return 0;
	}
	
}
