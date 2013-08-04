import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Risk extends JFrame implements KeyListener
{

	private static final long serialVersionUID = 1L;

	public static final int NUM_CONTS = 6;

	public static final int ASIA = 0;
	public static final int AUSTRALIA = 1;
	public static final int AFRICA = 2;
	public static final int EUROPE = 3;
	public static final int SOUTH_AMERICA = 4;
	public static final int NORTH_AMERICA = 5;

	private static final int MAX_WIDTH = 1100;			// Window size
	private static final int MAX_HEIGHT = 800;			// Window size
	private static final int WINDOW_TOP = 12;
	private static Image riskMap;

	public static Country [][] world = new Country[6][];
	public static Country []    asia = new Country[12];
	public static Country []    australia = new Country[5];
	public static Country []    africa = new Country[6];
	public static Country []    europe = new Country[7];
	public static Country []    southAmerica = new Country[3];
	public static Country []    northAmerica = new Country[9];

	public static ArrayList <AIPlayer> players = new ArrayList <AIPlayer> ();

	public static final int [] CONTINENT_BONUSES = {7, 2, 3, 5, 2, 5};

	private static int numTroopsToDistribute = 0;

	private static Random gen = new Random();

	private static final int ATTACK_INDEX = 0;
	private static final int DEFEND_INDEX = 1;

	private static final int SETUP_PHASE = 0;
	private static final int TROOP_PLACEMENT_PHASE = 1;
	private static final int ATTACK_PHASE = 2;
	private static final int REINFORCE_PHASE = 3;
	private static final int REINFORCED = 4;

	private static int currentPhase = SETUP_PHASE;

	public static final int NUM_COUNTRIES = 42;

	private boolean step = true;

	public static void main(String[] args) 
	{
		world [0] = asia;
		world [1] = australia;
		world [2] = africa;
		world [3] = europe;
		world [4] = southAmerica;
		world [5] = northAmerica;

		int total = 0;
		int winH = 0;
		int winO = 0;
		int winN = 0;
		int winP = 0;
		int winS = 0;

		Risk r = new Risk();
		r.addKeyListener(r);
		for(total = 0 ; total < 1000; total ++)
		{


			for (int i = 0; i < NUM_COUNTRIES; i ++)
			{
				int firstValue = gen.nextInt(world.length);
				int secondValue = gen.nextInt(world[firstValue].length);

				world [firstValue] [secondValue].setOwner(null);
				world [firstValue] [secondValue].setTroops(0);
			}
			fillPlayers(r);

			generateWorld();

//			r.setVisible(true);

			riskMap = new ImageIcon("riskMap.jpg").getImage();	

			AIPlayer winner = r.goThroughGame();
			if(winner instanceof OTPWPlayer)
			{
				winO ++;
			}
			if(winner instanceof MMPRPlayer)
			{
				winP ++;
			}
			if(winner instanceof MorgenthalerPlayer)
			{
				winS ++;
			}
			if(winner instanceof NCAI)
			{
				winN ++;
			}
			if(winner instanceof AntoPlayer)
			{
				winH ++;
			}

		}

		System.out.println("AntoPlayer Wins: " + winH);
		System.out.println("OTPWPlayer Wins: " + winO);
		System.out.println("NCAI Wins: " + winN);
		System.out.println("MMPRPlayer Wins: " + winP);
		System.out.println("MorgenthalerPlayer Wins: " + winS);
	}

	public int getNumPlayers()
	{
		return players.size();
	}

	public static void wait (int n){
		long r0,t1;
		r0=System.currentTimeMillis();
		do{
			t1=System.currentTimeMillis();
		}
		while (t1-r0<n);
	}

	private AIPlayer goThroughGame()
	{
		while (!gameOver())
		{
			for (int i = 0 ; i < players.size() ; i++)
			{
				if (calculateNumCountriesOwned(players.get(i)) != 0 && calculateNumCountriesOwned(players.get(i)) != NUM_COUNTRIES /** && step **/)
				{
					currentPhase = TROOP_PLACEMENT_PHASE;
					numTroopsToDistribute = calculateNumReinforcments(players.get(i));

					while (numTroopsToDistribute != 0)
					{
						players.get(i).placeTroops(numTroopsToDistribute);
					}

					repaint();
//					wait(250);

					currentPhase = ATTACK_PHASE;

					players.get(i).attack();

					repaint();
					//					wait(250);

					currentPhase = REINFORCE_PHASE;

					players.get(i).reinforce();

					repaint();
//					wait(100);
					//					step = false;
					//					waitForSpace();
//					if (players.get(i).)
				}
				//				else if(!step)
				//				{
				//					i --;
				//				}
			}

			for(int i =  0 ; i < players.size(); i ++)
			{
				requestPlayerDeletion(players.get(i));
			}

		}

		return players.get(0);
	}

	public void waitForSpace() {
		final CountDownLatch latch = new CountDownLatch(1);
		KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
			// Anonymous class invoked from EDT
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					latch.countDown();
				return false;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
	}

	private static void fillPlayers(Risk r) 
	{
		players = new ArrayList<AIPlayer>();

		players.add(new MorgenthalerPlayer(r, players.size()));
		players.get(players.size() - 1).setColor();	

		players.add(new MMPRPlayer(r, 1));
		players.get(players.size() - 1).setColor();

		players.add(new NCAI(r, players.size()));
		players.get(players.size() - 1).setColor();

		players.add(new AntoPlayer(r, players.size()));
		players.get(players.size() - 1).setColor();

		players.add(new OTPWPlayer(r, players.size()));
		players.get(players.size() - 1).setColor();

		randomizePlayers();
	}

	private static void randomizePlayers()
	{
		ArrayList<AIPlayer> newPlayers = new ArrayList<AIPlayer>();
		for(int i = 0 ; i < players.size() ; i ++)
		{
			newPlayers.add(null);
		}

		for(AIPlayer blah : players)
		{
			Random gen = new Random();
			int newIndex = gen.nextInt(players.size());
			while(newPlayers.get(newIndex) != null)
			{
				newIndex = gen.nextInt(players.size());
			}
			newPlayers.set(newIndex, blah);
		}
		players = newPlayers;
	}

	public Risk()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(MAX_WIDTH, MAX_HEIGHT);
		world = FileInput.getCountriesFromFile(world);
	}

	private static void requestPlayerDeletion(AIPlayer player)
	{
		if (calculateNumCountriesOwned(player) == 0)
		{
			players.remove(player);
		}
	}

	private static void generateWorld()
	{
		world = FileInput.getCountriesFromFile(world);
		fillCountries(world);
	}


	private static void fillCountries(Country[][] world2) 
	{
		for (int i = 0; i < NUM_COUNTRIES; i ++)
		{
			AIPlayer currentPlayer = players.get(i % players.size());
			int firstValue = gen.nextInt(world.length);
			int secondValue = gen.nextInt(world[firstValue].length);


			while (world [firstValue] [secondValue].getOwner() != null)
			{
				firstValue = gen.nextInt(NUM_CONTS);
				secondValue = gen.nextInt(world[firstValue].length);
			}
			world [firstValue] [secondValue].setOwner(currentPlayer);
			world [firstValue] [secondValue].setTroops(1);
		}
	}


	private static boolean gameOver()
	{
		if(players.size() == 1)
		{
			return true;
		}
		return false;
	}


	public static int calculateNumCountriesOwned(AIPlayer player)
	{
		int count = 0;
		for (Country[] continent: world)
		{
			for (Country currentCountry: continent)
			{
				if (currentCountry.getOwner() == player)
				{
					count++;
				}
			}
		}
		return count;
	}

	public boolean continentOwned(Country [] continent, AIPlayer player)
	{
		for (Country currentCountry : continent)
		{
			if (currentCountry != null && currentCountry.getOwner() != player)
			{
				return false;
			}
		}
		return true;
	}


	public int calculateNumReinforcments(AIPlayer player)
	{
		int count;
		count = calculateNumCountriesOwned(player) / 3;

		for (int i = 0; i < NUM_CONTS; i ++)
		{
			if (continentOwned(world[i], player))
			{
				count += CONTINENT_BONUSES[i];
			}
		}

		return count;
	}

	public Country [] [] getWorld()
	{
		return world;
	}

	public boolean requestTroopPlacement(Country countryTo, int numTroops, AIPlayer caller)
	{
		if (currentPhase == TROOP_PLACEMENT_PHASE)
		{
			if (countryTo != null && countryTo.getOwner() == caller)
			{
				if (numTroops <= numTroopsToDistribute)
				{
					countryTo.setTroops(countryTo.getTroops() + numTroops);
					numTroopsToDistribute -= numTroops;
					return true;
				}
			}
		}

		return false;
	}

	public Country [] getCountriesOfPlayer(AIPlayer requested)
	{
		Country[] countriesOwned = new Country[42]; 
		int blah = 0 ;
		for (int i = 0 ; i < world.length ; i ++)
		{
			for (int j = 0 ; j < world[i].length ; j ++)
			{
				if (world[i][j].getOwner() == requested)
				{
					countriesOwned[blah] = world[i][j];
					blah ++;
				}
			}
		}
		Country[] blah2 = new Country[blah];
		System.arraycopy(countriesOwned, 0, blah2, 0, blah);

		return blah2;
	}

	private int [] battleOutcome(int attackers, int defenders)
	{	
		int a1 = 0;
		int a2 = 0;
		int a3 = 0;
		int d1 = 0;
		int d2 = 0;

		if (attackers > 0)
		{
			a1 = gen.nextInt(6) + 1;
		}

		if (attackers > 1)
		{
			a2 = gen.nextInt(6) + 1;
		}

		if (attackers > 2)
		{
			a3 = gen.nextInt(6) + 1;
		}

		if (defenders > 0)
		{
			d1 = gen.nextInt(6) + 1;
		}

		if (defenders > 1)
		{
			d2 = gen.nextInt(6) + 1;
		}

		if (a1 < a2)
		{
			a1 = a1 ^ a2;
			a2 = a1 ^ a2;
			a1 = a1 ^ a2;
		}

		if (a1 < a3)
		{
			a1 = a1 ^ a3;
			a3 = a1 ^ a3;
			a1 = a1 ^ a3;
		}

		if (a2 < a3)
		{
			a2 = a2 ^ a3;
			a3 = a2 ^ a3;
			a2 = a2 ^ a3;
		}

		if(d1 < d2)
		{
			d1 = d1 ^ d2;
			d2 = d1 ^ d2;
			d1 = d1 ^ d2;
		}

		if (a1 != 0 && d1 != 0 && a1 > d1)
		{
			defenders --;
		}
		else if (a1 != 0 && d1 != 0 && a1 <= d1)
		{
			attackers --;
		}

		if (a2 != 0 && d2 != 0 && a2 > d2)
		{
			defenders --;
		}
		else if (a2 != 0 && d2 != 0 && a2 <= d2)
		{
			attackers --;
		}

		int [] returnArray = {attackers, defenders};
		return returnArray;
	}

	public boolean requestAttack(Country from, Country to, AIPlayer caller)
	{
		if (currentPhase == ATTACK_PHASE)
		{
			if (from.getOwner() == caller && to.getOwner() != caller)
			{
				int numAttackers = 0;

				if (from.getTroops() <= 1)
				{
					return false;
				}
				else if (from.getTroops() == 2)
				{
					numAttackers = 1;
				}
				else if (from.getTroops() == 3)
				{
					numAttackers = 2;
				}
				else if (from.getTroops() >= 4)
				{
					numAttackers = 3;
				}

				int numDefenders = 0;

				if (to.getTroops() <= 0)
				{
					return false;
				}
				else if (to.getTroops() == 1)
				{
					numDefenders = 1;
				}
				else if (from.getTroops() >= 2)
				{
					numDefenders = 2;
				}

				int [] battleResults = battleOutcome(numAttackers, numDefenders);

				int survivingAttackers = battleResults[ATTACK_INDEX];
				int survivingDefenders = battleResults[DEFEND_INDEX];

				from.setTroops(from.getTroops() - numAttackers + survivingAttackers);
				to.setTroops(to.getTroops() - numDefenders + survivingDefenders);

				if (to.getTroops() == 0)
				{
					forcePostAttackReinforce(from, to, caller);
				}

				requestPlayerDeletion(to.getOwner());
				repaint();

				return true;
			}
		}
		return false;
	}

	private void forcePostAttackReinforce(Country from, Country to, AIPlayer player)
	{
		to.setOwner(player);

		boolean legalMoveMade = false;
		int numRequestedTroops;
		currentPhase = REINFORCE_PHASE;

		while (!legalMoveMade)
		{
			numRequestedTroops = player.postAttackReinforce(to, from);
			legalMoveMade = requestReinforce(from, to, numRequestedTroops, player);
		}
		currentPhase = ATTACK_PHASE;
	}

	public boolean requestReinforce(Country from, Country to, int numTroops, AIPlayer caller)
	{
		if (currentPhase == REINFORCE_PHASE)
		{
			if (from.getOwner() == caller && to.getOwner() == caller)
			{
				if (from.getTroops() > numTroops)
				{
					from.addTroops(-1 * numTroops);
					to.addTroops(numTroops);
					currentPhase = REINFORCED;
					return true;
				}
			}
		}

		return false;
	}

	public static ArrayList <AIPlayer> getPlayers()
	{
		return players;
	}

	public void paint(Graphics g)
	{
		g.drawImage(riskMap, 0, WINDOW_TOP, MAX_WIDTH, MAX_HEIGHT, this);
		for(int row = 0; row < world.length; row++)
		{
			for(int col = 0; col < world[row].length; col++)
			{
				world[row][col].draw(g);
			}
		}

		g.setColor(Color.WHITE);
		g.fillRect(370, 703, 342, 100);
		for(AIPlayer blah : players)
		{
			g.setColor(blah.getColor());
			g.setFont(new Font("comicsans",  Font.PLAIN, 12));
			g.fillRect(375, 710 + players.indexOf(blah) * 15, 10, 10);
			g.drawString(blah.toString(), 395, 720 + players.indexOf(blah) * 15);
		}
	}

	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			step = true;
		}
	}

	public void keyReleased(KeyEvent arg0) 
	{

	}

	public void keyTyped(KeyEvent arg0) 
	{

	}
}