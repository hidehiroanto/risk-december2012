import java.awt.Font;
import java.awt.Graphics;

public class Country 
{

	private Country [] neighbors;		//an array of the neighboring countries
	private int numTroops;				//number of troops occupying country
	private AIPlayer owner = null;		//player that owns country
	private String name;				//country name
	private int x, y;					//x and y coordinates on screen

	public Country(Country [] neighborsIn, String nameIn)
	{

		neighbors = neighborsIn;
		name = nameIn;
	}

	public Country(Country [] neighborsIn, int numTroopsIn, AIPlayer ownerIn, String nameIn, int xIn, int yIn)
	{
		neighbors = neighborsIn;
		numTroops = numTroopsIn;
		owner = ownerIn;
		name = nameIn;
		x = xIn;
		y = yIn;
	}

	/**
	 * Checks if there are any troops in a country
	 * @return If there are troops
	 */
	public boolean hasTroops()
	{
		if (numTroops == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 
	 * @return number of troops in country
	 */
	public int getTroops()
	{
		return numTroops;
	}

	/**
	 * 
	 * @param newNumTroops The new number of troops
	 */
	public void setTroops(int newNumTroops)
	{
		numTroops = newNumTroops;
	}

	/**
	 * Add more troops to numTroops
	 * @param newNumTroops The number of troops being added
	 */
	public void addTroops(int newNumTroops)
	{
		numTroops += newNumTroops;
	}

	/**
	 * 
	 * @return An array of neighboring countries
	 */
	public Country [] getNeighbors()
	{
		return neighbors;
	}

	/**
	 * Adds neighbors to the neighbors array
	 * @param neighbors New neighbors array
	 */
	public void addNeighbors(Country[] neighbors)
	{
		this.neighbors = neighbors;
	}

	/**
	 * 
	 * @return The player that owns country
	 */
	public AIPlayer getOwner()
	{
		return owner;
	}

	/**
	 * Resets the player that owns 
	 * @param newOwner
	 */
	public void setOwner(AIPlayer newOwner)
	{
		owner = newOwner;
	}
	
	/**
	 * 
	 * @return x coordinate on screen
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * 
	 * @return y coordinate on screen
	 */
	public int getY()
	{
		return y;
	}

	public String toString()
	{
		return name;
	}

	/**
	 * Draw method:
	 * 	- draws number of troops in country
	 * @param g Graphics Engine
	 */
	public void draw(Graphics g)
	{
		g.setFont(new Font("Serif", Font.BOLD, 22));
		g.setColor(owner.getColor());
		g.drawString(String.valueOf(numTroops), x, y);
	}

}
