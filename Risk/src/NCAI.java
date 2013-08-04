

public class NCAI extends AIPlayer
{

	Risk controller;
	private Country [] [] world;

	private int attackConstant = 2;
	private int reinforceConstant = 20;
	private int numericalPriority = 3;
	private int continentOwnedPriority = 1;
	private int numBordersPriority = 1;

	public NCAI()
	{

	}

	public NCAI (Risk controller, int playerNum)
	{
		this.controller = controller;
		world = controller.getWorld();
		this.playerNumber = playerNum;
	}



	public void placeTroops(int numTroopsAvailable) 
	{
		//		System.out.println("NCAI Placement");
		Country highest = null;
		for (Country current : controller.getCountriesOfPlayer(this))
		{
			int priority = calculateTroopPlacementValue(current);
			if (highest == null || priority > calculateTroopPlacementValue(highest))
			{
				highest = current;
			}
		}
		controller.requestTroopPlacement (highest, 1, this);
	}

	private int calculateTroopPlacementValue(Country current) 
	{
		int enemyCount = 0;
		for (Country currentCountry : current.getNeighbors())
		{
			if (currentCountry.getOwner() != this)
			{
				enemyCount += currentCountry.getTroops();
			}
		}
		return (enemyCount - current.getTroops());
	}



	public void attack() 
	{
		//									System.out.println("NCAI Attack");
		for (Country currentCountry : controller.getCountriesOfPlayer(this))
		{
			for (Country enemy: currentCountry.getNeighbors())
			{
				if (enemy.getOwner() != this)
				{
					if (calculateAttackValue(currentCountry, enemy) > attackConstant)
					{
//						System.out.println("Attacking");
						boolean legalAttack = true;
						
						while (calculateAttackValue(currentCountry, enemy) > attackConstant && legalAttack)
						{
							legalAttack = controller.requestAttack(currentCountry, enemy, this);
						}
					}
				}
			}
		}
	}

	private int calculateAttackValue(Country from, Country to)
	{
		if (to.getOwner() == this)
		{
			return 0;
		}
		if (from.getOwner() != this)
		{
			return 0;
		}
		int attackValue = 0;
		if (numericalPriority <= 0)
		{
			numericalPriority = 1;
		}
		attackValue += from.getTroops();
		attackValue -= to.getTroops();
		attackValue *= numericalPriority;
		attackValue += (continentBonus(to) * continentOwnedPriority);
		attackValue -= (numBordersPriority * to.getNeighbors().length);
		return attackValue;
	}

	private int continentBonus(Country checkingCountry)
	{
		int returnVal = 0;
		for (int i = 0; i < controller.NUM_CONTS; i ++)
		{
			if (leftOnContinent(world [i]) != 0)
			{
				returnVal += (controller.CONTINENT_BONUSES[i] / leftOnContinent(world [i]));
			}
		}
		if (returnVal < 0)
		{
			return 0;
		}
		return returnVal;
	}

	//	private boolean lastOnContinent(Country [] continent, Country checkingCountry)
	//	{
	//		boolean contained = false;
	//		for (int i = 0; i < continent.length; i ++)
	//		{
	//			Country currentCountry = continent [i];
	//			if (currentCountry == checkingCountry)
	//			{
	//				contained = true;
	//			}
	//		}
	//		
	//		
	//		if (! contained)
	//		{
	//			return false;
	//		}
	//		
	//		Country [] modifiedContinent = new Country [continent.length - 1];
	//		
	//		int u = 0;
	//		for (int i = 0; i < continent.length; i ++)
	//		{
	//			Country currentCountry = continent [i];
	//			if (currentCountry != checkingCountry)
	//			{
	//				modifiedContinent [u] = continent [i];
	//				u ++;
	//			}
	//		}
	//		
	////		for(Country currentCountry : modifiedContinent)
	////		{
	////			System.out.println(currentCountry);
	////		}
	//		
	//		if (modifiedContinent != null && modifiedContinent.length != 0)
	//		{
	//			if (controller.continentOwned(modifiedContinent, this))
	//			{
	//				return true;
	//			}
	//		}
	//		return false;
	//	}


	private int leftOnContinent(Country [] continent)
	{
		int i = 0;
		for (Country currentCountry : continent)
		{
			if (currentCountry.getOwner() != this)
			{
				i ++;
			}
		}
		return i;
	}


	public void reinforce() 
	{
//		System.out.println("NCAI Reinforce");
		Country lowest = null, highest = null;
		for (Country currentCountry : controller.getCountriesOfPlayer(this))
		{
			if (lowest == null)
			{
				lowest = currentCountry;
			}
			if (highest == null)
			{
				highest = currentCountry;
			}
			if (calculateTroopPlacementValue(currentCountry) < calculateTroopPlacementValue(lowest))
			{
				lowest = currentCountry;
			}
			if (calculateTroopPlacementValue(currentCountry) > calculateTroopPlacementValue(highest))
			{
				highest = currentCountry;
			}
		}
		if (calculateTroopPlacementValue(highest) - calculateTroopPlacementValue(lowest) > reinforceConstant)
		{
			controller.requestReinforce(lowest, highest, lowest.getTroops() - 1, this);
		}
	}


	public int postAttackReinforce(Country conquered, Country conquerer) 
	{
//		System.out.println("NCAI postAttackReinforce");
		int numTroopsAvailable = conquerer.getTroops() - 1;
		int toValue = calculateTroopPlacementValue(conquered);
		int fromValue = calculateTroopPlacementValue(conquerer);
		double ratio = 1.0 * toValue / (fromValue + toValue);
		int returnVal = (int) ratio * numTroopsAvailable;
		if (returnVal <= 0)
		{
			returnVal = 1;
		}
		if (returnVal >= numTroopsAvailable)
		{
			returnVal = numTroopsAvailable - 1;
		}
		if (returnVal <= 0)
		{
			returnVal = 1;
		}
		return returnVal;
	}

	void setReinforce(int reinforceIn)
	{
		reinforceConstant = reinforceIn;
	}

	void setAttack(int attackIn)
	{
		attackConstant = attackIn;
	}

	void setContinentOwnedPriority(int continentOwnedPriorityIn)
	{
		continentOwnedPriority = continentOwnedPriorityIn;
	}

	void setNumericalPriority(int numericalPriorityIn)
	{
		numericalPriority = numericalPriorityIn;
	}

	void setNumBordersPriority(int numBordersPriorityIn)
	{
		numBordersPriority = numBordersPriorityIn;
	}

}
