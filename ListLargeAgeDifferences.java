package GedcomParse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListLargeAgeDifferences {
	
	ArrayList<String> tempIndi;
	ArrayList<String> tempFam;
	int husAge;
	int wifeAge;
	String comparison;
	int difference;
	SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd");
	
	static Double MillToYear = 24.0 * 60.0 * 60.0 * 1000.0 * 365.0;
	
	private HashMap<String, ArrayList<String>> indiHash = new HashMap<>();
	private HashMap<String, ArrayList<String>> famHash = new HashMap<>();
	
	public void ListLargeAgeDifferences(HashMap<String, ArrayList<String>> indiHash, HashMap<String, ArrayList<String>> famHash)
	{	
		System.out.println("\n******************** Karan's User story US34: List Large Age Differences **********************\n");
		
		this.indiHash = indiHash;
		this.famHash = famHash;
		
		ageDifferencesCompare();
	}
	
	public void ageDifferencesCompare() 
	{
		ArrayList<String> famInfo = new ArrayList<String>();
		String [] mDate;
		String monthNumber;
		String formattedMarriageDate;
		Date wedding = new Date();
		
		for (String famKey : this.famHash.keySet())
		{
			famInfo = this.famHash.get(famKey);
	
			String hID = famInfo.get(1);
			String wID = famInfo.get(3);
			
			String husName = famInfo.get(2);
			String wifeName = famInfo.get(4);
			
			String marriedDate = famInfo.get(5);
			
			if (!marriedDate.equals(""))
			{
				mDate = marriedDate.split(" ");
				
				monthNumber = dateNumber(mDate[1]);
				
				formattedMarriageDate = mDate[2] + "-" + monthNumber + "-" + mDate[0];
				
				try 
				{
					wedding = simpleDateFormat.parse(formattedMarriageDate);
					

					husAge = findAgeWhenMarried(hID, wedding);
						
					wifeAge = findAgeWhenMarried(wID, wedding);
					
					
					comparison = birthDateComparison(wifeAge, husAge);
								
					difference = returnAgeDifference(wifeAge, husAge);
								
					if (comparison.equals("Wife Too Old!"))
					{
						System.out.println("The age difference between Individuals " + hID + " and " + wID + " named " + husName + ", " + wifeName + " respectively is " + difference +". Where age of " + wifeName + " is more than double the age of " + husName + " when they were married!");
					}
								
					if (comparison.equals("Hus Too Old!"))
					{
						System.out.println("The age difference between Individuals " + hID + " and " + wID + " named " + husName + ", " + wifeName + " respectively is " + difference +". Where age of " + husName + " is more than double the age of " + wifeName + " when they were married!");
					}
					
				}
				
				catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public int findAgeWhenMarried(String Id, Date marriedDate)
	{
		ArrayList<String> indiInfo = new ArrayList<String>();
		int Age = 0;
		String bDate;
		String [] birthDate;
		String month;
		String formattedBirthDate;
		Date birthday;
		
		for (String indiKey : this.indiHash.keySet())
		{
			indiInfo = this.indiHash.get(indiKey);
			
			if (indiKey.equals(Id))
			{
				bDate = indiInfo.get(3);
				
				if (!bDate.equals(""))
				{
					birthDate = bDate.split(" ");
					
					month = dateNumber(birthDate[1]);
					
					formattedBirthDate = birthDate[2] + "-" + month + "-" + birthDate[0];
					
					try 
					{
						birthday = simpleDateFormat.parse(formattedBirthDate);
						
						if (!(birthday.getTime() > marriedDate.getTime()))
						{
							double diff = Math.abs(marriedDate.getTime() - birthday.getTime());
							
							double age = diff / MillToYear;
							
							Age = (int) age;
							
							return Age;
						}
					}

					catch (ParseException e)
					{
						e.printStackTrace();
						return 0;
					}
				}
			}
		}
		
		return Age;
	}
	
	public String birthDateComparison(int wifeAge, int husAge)
	{
		String tooOld = "";
		
		if (wifeAge > husAge)
		{
			
			int comparison = husAge*2;
			if (wifeAge > comparison)
			{
				tooOld = "Wife Too Old!";
			}
		}
		
		if (husAge > wifeAge)
		{
			int comparison = wifeAge*2;
			if (husAge > comparison)
			{
				tooOld = "Hus Too Old!";
			}
		}
		
		return tooOld;
	}
	
	public int returnAgeDifference(int wifeAge, int husAge)
	{
		int diff = 0;
		
		diff = Math.abs(wifeAge - husAge);
		
		return diff;
	}
	
	public static String dateNumber(String monthName)
	{
		switch (monthName)
		{
			case "JAN":
				return "01";
			case "FEB":
				return "02";
			case "MAR":
				return "03";
			case "APR":
				return "04";
			case "MAY":
				return "05";
			case "JUN":
				return "06";
			case "JUL":
				return "07";
			case "AUG":
				return "08";
			case "SEP":
				return "09";
			case "OCT":
				return "10";
			case "NOV":
				return "11";
			case "DEC":
				return "12";
			default:
				return "00";
		}
	}
}
