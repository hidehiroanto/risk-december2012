import java.util.ArrayList;

/**
 * AntoPlayer is the best Risk AI ever. Because it uses the discontinuity in the spacetime continuum and the discrepancy
 * in the cosmic microwave background radiation to calculate the optimum Bose-Einstein condensate coefficient, the AI can
 * use the best algorithm to predict the future and ensure its success.
 * @author Hidehiro Anto
 * @version 17 December 2012
 */
public class AntoPlayer extends AIPlayer
{
	/**
	 * This constructor rips through the spacetime fabric and generates a black hole, creates an alternate universe in which
	 * the Mage of Omniscience resides, consults the Mage for the right decisions, and implements those decisions in the AI.
	 * @param board			the vessel through which the Tralfamadorian Constant becomes known
	 * @param playerNumber	42
	 */
	public AntoPlayer(Risk board, int playerNumber)
	{
		this.board = board;
		this.playerNumber = playerNumber; 
	}

	/**
	 * This method equips the Antonian troops with the latest gear and technology. All of our soldiers are armored with the
	 * most recent phasing technology, imbued with microportals that teleport bullets from the point of impact to the theoretical
	 * point at which the bullet exits the body, conserving momentum. Our helmets are equipped with visors that communicate all
	 * battle tactics and situational updates directly to the user. Our cavalry are riding on the most fearsome horses that have
	 * ever lived; their descendants will carry the four horsemen of the apocalypse. Any bullets that come within ten feet of these
	 * horses will be so scared that they decelerate and turn around toward the enemy to flee from their dreadful aura. All soldiers
	 * who face these horses will die from shock and burn with black fire. All artillery that try to fire on these cavalry will
	 * immediately malfunction and blow up, fearful of retribution. Speaking of artillery, the metallic shells of our tanks are so shiny
	 * that all enemies will be blinded by their majesty.
	 */
	public void placeTroops(int numTroopsAvailable)
	{
		ArrayList<Country> possessions = getPossessions();
		
		Country c = possessions.get((int) (Math.random() * possessions.size()));
		
//		Country c = possessions.get(0);
//		for (int index = 1; index < possessions.size(); index++)
//			if (c == null || possessions.get(index).getNeighbors().length < c.getNeighbors().length)
//				c = possessions.get(index);
		
		board.requestTroopPlacement(c, numTroopsAvailable, this);
	}
	
	/**
	 * Our troops have the best weapons of all time. Our soldiers carry gunblades imbued with the blessing of the Mage of Omniscience, so that
	 * those who are nicked by the blades will endure writhing pain until all their skin sloughs off and those who are struck by our bullets
	 * will suffer from hemorrhagic stroke, cardiac arrest, and hellish hallucinations before ending their suffering themselves. All our cavalry
	 * carry poisoned claymores, and the leader of each platoon carries a greatsword of Damascus steel. Our artillery fire once every second and
	 * have a blast radius of one hundred feet. All of our forces have an average speed of eight miles per hour, so that conquest is very fast.
	 * The troops with the best performance get gifts of laser, ion, Gauss, or railguns.
	 */
	public void attack()
	{
		ArrayList<Country> possessions = getPossessions();
		for (int index = 0; index < possessions.size(); index++)
		{
			Country weakestNeighbor = null;
			Country[] neighbors = possessions.get(index).getNeighbors();
			for (Country current : neighbors)
				if ((weakestNeighbor == null || current.getTroops() < weakestNeighbor.getTroops()) && current.getOwner() != this)
					weakestNeighbor = current;
			boolean valid = true;
			if (weakestNeighbor != null)
				while (valid)
					valid = board.requestAttack(possessions.get(index), weakestNeighbor, this);
		}
	}
	
	/**
	 * Our current teleportation development is in closed beta, but we have already granted the rights to use the prototype to the Antonian
	 * leadership. The basic idea behind large-scale teleportation is the same as the technology embedded in our infantry armor, but it requires
	 * more energy from the solar wind power in the universe of the Mage of Omniscience to sustain the larger portal. Our troops must enter a ship
	 * pointing towards the center of the earth, travel along the magnetic field lines that were duplicated in the Mage universe, and exit the
	 * corresponding portal. This entire process takes about 2.7182818284590 Planck times.
	 */
	public void reinforce()
	{
		ArrayList<Country> possessions = getPossessions();
		Country c1 = possessions.get(0), c2 = c1;
		for (int index = 1; index < possessions.size(); index++)
			if (c2 == null || getNeighborTroops(possessions.get(index)) > getNeighborTroops(c2))
				c2 = possessions.get(index);
		board.requestReinforce(c1, c2, c1.getTroops() - 1, this);
	}

	/**
	 * THE MAGE OF OMNISCIENCE WAVES HIS MAGIC SPARKLY WAND AND SUDDENLY TROOPS THAT WERE IN ONE COUNTRY ARE NOW IN ANOTHER.
	 */
	public int postAttackReinforce(Country conquered, Country conquerer)
	{
		return conquerer.getTroops() / 2;
	}

	private ArrayList<Country> getPossessions()
	{
		ArrayList<Country> possessions = new ArrayList<Country>(50);
		for (Country[] continent : Risk.world)
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
		return possessions;
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
}