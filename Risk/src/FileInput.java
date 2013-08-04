import java.io.*;
import java.util.*;



public class FileInput
{
	private static final String fileName = "RiskCountries";
	public static Country[][] getCountriesFromFile(Country[][] countries)
	{
		try
		{
			Scanner in = new Scanner(new BufferedReader(new FileReader(fileName)));
			Map<Country, String[]> countryToNeighbors = new HashMap<Country, String[]>();
			Map<String, Country> stringToCountry = new HashMap<String, Country>();
			int continent = 0, country = 0;
			while (in.hasNext() && continent < countries.length)
			{
				String blah = in.nextLine();
				if (blah.isEmpty())
				{
					continent++;
					country = 0;
				}
				else
				{
					String[] stringArray = blah.split(" ");
					String[] borderCountries = new String[stringArray.length - 3];
					for (int i = 3; i < stringArray.length; i++)
					{
						borderCountries[i - 3] = stringArray[i];
					}
					countries[continent][country] = new Country(null, 0, null, stringArray[0], Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]));
					stringToCountry.put(stringArray[0], countries[continent][country]);
					countryToNeighbors.put(countries[continent][country], borderCountries);
					country++;
				}
			}
			for (Country c : countryToNeighbors.keySet())
			{
				String[] neighborStrings = countryToNeighbors.get(c);
				ArrayList<Country> neighbors = new ArrayList<Country>();
				for (String s : neighborStrings)
				{
					neighbors.add(stringToCountry.get(s));
				}
				c.addNeighbors(neighbors.toArray(new Country[0]));
			}
			in.close();
			return countries;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
