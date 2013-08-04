import java.awt.Color;

abstract public class AIPlayer 
{	
	protected final Color[] COLORS = new Color[]{Color.red , Color.cyan , Color.blue , Color.black , Color.magenta , Color.LIGHT_GRAY , Color.green};
	protected Risk board;
	protected int playerNumber;
	Color color;
	
	public abstract void placeTroops(int numTroopsAvailable);
	public abstract void attack();
	public abstract void reinforce();
	public abstract int postAttackReinforce(Country conquered, Country conquerer);
	
	public int getPlayerNum()
	{
		return playerNumber;
	}
	
	public boolean equalTo(AIPlayer input)
	{
		return input.getPlayerNum() == playerNumber;
		
	}
	
	public void setColor()
	{

		if(playerNumber < COLORS.length )
		{
			color = COLORS[playerNumber];
		}
		else {color = new Color((int) (255 * Math.abs(Math.sin(playerNumber))), (int) (255 * Math.abs(Math.cos(playerNumber))), (int) (255 * Math.exp(-playerNumber)));
		}
	}
	
	public Color getColor()
	{
		return color; 
	}
}