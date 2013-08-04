import java.util.ArrayList;

public class MMPRPlayer extends AIPlayer 
{
	private Country [] [] world;
	private ArrayList<ArrayList<Country>> mine;
	private int mineSize;
	private boolean aggro = true;


	public MMPRPlayer(Risk controller, int playerNum)
	{
		board = controller;
		world = controller.getWorld();
		this.playerNumber = playerNum;
	}

	public void placeTroops(int numTroopsAvailable) 
	{
		aggro = !aggro;
		if(aggro)
		{
			compileInfo();
			Country c = findBestCountry();
			board.requestTroopPlacement(c, numTroopsAvailable, this);
		}
		else
		{
			compileInfo();
			int troopsPerCountry = numTroopsAvailable/mineSize;
			int troopsLeft = numTroopsAvailable%mineSize;
			for(ArrayList<Country> a: mine)
			{
				for(Country c: a)
				{
					board.requestTroopPlacement(c, troopsPerCountry, this);
				}
			}
			if(troopsLeft > 0)
				board.requestTroopPlacement(findBestCountry(), troopsLeft, this);
		}
	}

	private Country findBestCountry() 
	{
		Country bC = getFirstBorderCountry();
		if(bC == null)
			return getFirstCountry();
		int bR = calculateTroopRatio(bC);
		for(ArrayList<Country> a: mine)
		{
			for(Country c: a)
			{
				int nR = calculateTroopRatio(c);
				if(nR < bR && borderCountry(c))
				{
					bR = nR;
					bC = c;
				}
			}
		}
		return bC;
	}

	private Country getFirstBorderCountry() 
	{
		for(ArrayList<Country> a: mine)
			for(Country c: a)
			{
				if(borderCountry(c))
					return c;
			}

		return null;
	}

	private Country getFirstCountry() 
	{
		for(ArrayList<Country> a: mine)
			for(Country c: a)
			{
				return c;
			}

		return null;
	}

	private boolean borderCountry(Country c) 
	{
		boolean border = false;
		for(Country n: c.getNeighbors())
			if(!n.getOwner().equals(this))
				border = true;
		return border;
	}

	private int calculateTroopRatio(Country c) 
	{
		int neighborCount = 0;
		for(Country n: c.getNeighbors())
		{
			if(!n.getOwner().equals(this))
				neighborCount += n.getTroops();
		}
		return neighborCount - c.getTroops();
	}

	private void compileInfo() 
	{
		mine = new ArrayList<ArrayList<Country>>();
		mineSize = 0;
		for (int i = 0; i < world.length; i++) 
		{
			mine.add(new ArrayList<Country>());
			for (int j = 0; j < world[i].length; j++) 
			{
				if(world[i][j].getOwner().equals(this))
				{
					mine.get(i).add(world[i][j]);
					mineSize++;
				}
			}
		}		
	}

	public void attack() 
	{
		int count = 0;
		boolean success;
		do
		{
			Country from = findBestCountry();
			if(borderCountry(from))
			{
				Country to = findBestTarget(from);
				success = board.requestAttack(from, to, this);
				count++;
			}
			else
				success = false;
		}
		while(success && count < 25);

	}

	private Country findBestTarget(Country from) 
	{		
		Country attackCountry = null;
		int least = 0;
		for(Country n: from.getNeighbors())
		{
			if(!n.getOwner().equals(this))
			{
				attackCountry = n;
				least = n.getTroops();
			}
		}

		for(Country n: from.getNeighbors())
		{
			if(!n.getOwner().equals(this))
			{
				int newValue = n.getTroops();
				if(newValue < least)
				{
					least = newValue;
					attackCountry = n;
				}				
			}
		}
		return attackCountry;
	}


	@Override
	public void reinforce() 
	{
		Country from = getStrongestCenterCountry();
		Country to = getWeakestBorderCountry();
		board.requestReinforce(from, to, from.getTroops() - 1, this);
	}

	private Country getWeakestBorderCountry() 
	{
		Country wBC = getFirstCountry();
		int lT = wBC.getTroops();

		for(ArrayList<Country> a: mine)
		{
			for(Country c: a)
			{
				if(borderCountry(c) && c.getTroops() < lT)
				{
					wBC = c;
					lT = c.getTroops();
				}
			}
		}
		return wBC;
	}

	private Country getStrongestCenterCountry() 
	{
		Country sCC = getFirstCountry();
		int mT = sCC.getTroops();

		for(ArrayList<Country> a: mine)
		{
			for(Country c: a)
			{
				if(!borderCountry(c) && c.getTroops() > mT)
				{
					sCC = c;
					mT = c.getTroops();
				}
			}
		}
		return sCC;
	}

	@Override
	public int postAttackReinforce(Country conquered, Country conquerer) 
	{
		//		System.out.println(conquerer.getTroops()*4/5);
		return conquerer.getTroops()/2;
	}

}
