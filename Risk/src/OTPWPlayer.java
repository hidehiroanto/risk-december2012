
import java.awt.Color;
import java.util.Random;

/**
 * 
 * INTACT - 11/13/12 11:37 WWWWWWWWWWWWWWWWW
 *
 */


public class OTPWPlayer extends AIPlayer
{
	private Country heroCountry;
	private int randomDeterminateVariable = 1;

	public OTPWPlayer(Risk board, int playerNum)
	{
		this.board = board;
		this.playerNumber = playerNum;
	}

	public void attack()
	{
		if(board.getCountriesOfPlayer(this).length < 7)
		{
			/**
			 * * REMEBER FOR SHOW MATCH    ####################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 * * REMEBER FOR SHOW MATCH
			 **/
//			BOSSMODE();
		}
		if(board.getCountriesOfPlayer(this).length == board.NUM_COUNTRIES || board.getCountriesOfPlayer(this).length == 0)
		{
			return;
		}
		if(heroCountry != null)
		{
			Country[] neighbors = heroCountry.getNeighbors();

			Random gen = new Random();
			int index = gen.nextInt(neighbors.length);
			int tries = 0 ;
			while((neighbors[index] == null || neighbors[index].getOwner() == this) && !(neighbors[index].getOwner() instanceof AntoPlayer) &&neighbors[index].getTroops() == randomDeterminateVariable && tries < 50)
			{
				index = gen.nextInt(neighbors.length);
				tries ++;
			}
			
			boolean success = board.requestAttack(heroCountry, neighbors[index], this);
			while(success)
			{
				success = board.requestAttack(heroCountry, neighbors[index], this);
			}
			if(neighbors[index] == heroCountry)
			{
				attack();
			}
		}
		else
		{
			fillHeroCountry();
		}
	}
	
	public void BOSSMODE()
	{
		System.out.println("OTPWPlayer Went SSJ11%3+9001!!!!!");

		int go = 0;
		while(go < 5000000)
		{
//			setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
			board.repaint();
			go ++;
		}
		setColor();
	}


	public void reinforce()
	{
		Country[] ourCountries = board.getCountriesOfPlayer(this);
		boolean reinforced = false;
		for(int i = 0 ; i < ourCountries.length ; i ++) 
		{
			if(ourCountries[i].getTroops() > 1 && !reinforced)
			{
				reinforced = true;
				board.requestReinforce(ourCountries[i], heroCountry, ourCountries[i].getTroops() - 1, this);
			}
		}
	}

	public void placeTroops(int numTroopsAvailable)
	{
		fillHeroCountry();
		board.requestTroopPlacement(heroCountry, numTroopsAvailable, this);
	}

	public int postAttackReinforce(Country conquered, Country conquerer) 
	{
		heroCountry = conquered;
		return conquerer.getTroops() - 1;
	}

	private void fillHeroCountry()
	{
		if(board.getCountriesOfPlayer(this).length == board.NUM_COUNTRIES || board.getCountriesOfPlayer(this).length == 0)
		{
			heroCountry = board.getCountriesOfPlayer(this)[0];
		}
		else if(heroCountry == null || heroCountry.getOwner() != this)
		{		
			heroCountry = null;
			Country[] ourCountries = board.getCountriesOfPlayer(this);
			
			int tries = 0;
			do
			{
				Random gen = new Random();
				int index = gen.nextInt(ourCountries.length);
				Country[] neighbors = ourCountries[index].getNeighbors();
				for(Country curr : neighbors)
				{
					if((curr != null) && curr.getOwner() instanceof AntoPlayer && curr.getTroops() == randomDeterminateVariable)
					{
						heroCountry = ourCountries[index];
					}
				}
				tries ++;
			}while(tries < 50 && heroCountry == null);
		}

		else
		{
			Country[] var = heroCountry.getNeighbors();
			boolean stay = false;
			for(Country var2 : var)
			{
				if(var2 != null && var2.getOwner() != this)
				{
					stay = true;
				}
			}
			if(!stay)
			{
				heroCountry = null;
				fillHeroCountry();
			}
		}

		if(heroCountry == null)
		{
			Country[] ourCountries = board.getCountriesOfPlayer(this);

			Random gen = new Random();
			int index = gen.nextInt(ourCountries.length);
			Country[] neighbors = ourCountries[index].getNeighbors();
			for(Country curr : neighbors)
			{
				if(curr.getOwner() != this)
				{
					heroCountry = ourCountries[index];
				}
			}
		}
	}
}