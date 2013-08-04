import java.util.ArrayList;

public class MorgenthalerPlayer extends AIPlayer
{	
	public MorgenthalerPlayer(Risk board, int playerNumber)
	{
		this.board = board;
		this.playerNumber = playerNumber; 
	}

	public void placeTroops(int numTroopsAvailable)
	{
		ArrayList<Country> chokepoints = new ArrayList<Country>();
		Country[][] world = Risk.world;
		for (Country[] continent : world)
			for (Country current : continent)
				if (current.getOwner() == this)
					chokepoints.add(current);
		for (int i = 0; i < numTroopsAvailable && chokepoints.size() != 0; i++)
		{
			Country c = chokepoints.get(i % chokepoints.size());
			board.requestTroopPlacement(c, 1, this);
		}
	}

	public void attack()
	{
		ArrayList<Country> possessions = new ArrayList<Country>();
		Country[][] world = Risk.world;
		for (Country[] continent : world)
			for (Country current : continent)
				if (current.getOwner() == this)
					possessions.add(current);
		for (int i = 0; i < possessions.size() - 1; i++)
		{
			int biggest = i;
			for (int j = i + 1; j < possessions.size(); j++)
				if (possessions.get(j).getTroops() > possessions.get(biggest).getTroops())
					biggest = j;
			Country biggestCountry = possessions.get(biggest);
			possessions.set(biggest, possessions.get(i));
			possessions.set(i, biggestCountry);
		}
		for (int i = 0; i < possessions.size(); i++)
		{
			Country mostThreateningNeighbor = getMostThreateningNeighbor(possessions, i);
			for (int weird = 0; mostThreateningNeighbor != null && weird < 100; weird++)
				board.requestAttack(possessions.get(i), mostThreateningNeighbor, this);
		}
	}

	public void reinforce()
	{
		ArrayList<Country> possessions = new ArrayList<Country>();
		Country[][] world = Risk.world;
		for (Country[] continent : world)
			for (Country current : continent)
				if (current.getOwner() == this)
					possessions.add(current);
		for (int i = 0; i < possessions.size() - 1; i++)
		{
			int biggest = i;
			for (int j = i + 1; j < possessions.size(); j++)
				if (getNeighborTroops(possessions.get(j)) > getNeighborTroops(possessions.get(biggest)))
					biggest = j;
			Country biggestCountry = possessions.get(biggest);
			possessions.set(biggest, possessions.get(i));
			possessions.set(i, biggestCountry);
		}
		Country c1 = possessions.get(0);
		for (int i = 0; i < possessions.size() - 1; i++)
		{
			int biggest = i;
			for (int j = i + 1; j < possessions.size(); j++)
				if (possessions.get(j).getTroops() > possessions.get(biggest).getTroops())
					biggest = j;
			Country biggestCountry = possessions.get(biggest);
			possessions.set(biggest, possessions.get(i));
			possessions.set(i, biggestCountry);
		}
		Country c2 = possessions.get(0);
		board.requestReinforce(c2, c1, c2.getTroops() - c1.getTroops(), this);
	}

	public int postAttackReinforce(Country conquered, Country conquerer)
	{
		return conquerer.getTroops() - 1;
	}

	private int getNeighborTroops(Country c)
	{
		Country[] neighbors = c.getNeighbors();
		int sum = 0;
		for (Country neighbor : neighbors)
			if (neighbor.getOwner() != this)
				sum += neighbor.getTroops();
		return sum;
	}

	private Country getMostThreateningNeighbor(ArrayList<Country> possessions, int index)
	{
		Country mostThreateningNeighbor = null;
		for (int i = 0; i < possessions.get(index).getNeighbors().length; i++)
		{
			if (possessions.get(index).getNeighbors()[i].getOwner() != this &&
					(mostThreateningNeighbor == null ||
					possessions.get(index).getNeighbors()[i].getTroops() > mostThreateningNeighbor.getTroops()))
			{
				mostThreateningNeighbor = possessions.get(index).getNeighbors()[i];
			}
		}
		return mostThreateningNeighbor;
	}
}