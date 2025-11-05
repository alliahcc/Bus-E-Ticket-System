package ticket.info;

import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;

public class DestinationInfo {
	public static ArrayList<Double> kilometers;
	public static  ArrayList<String> originDestination;
	int destinationNumber;
	
	public double calculatedKilometers = 0;
	public static String origin;
	public static String destination;
	public static int presetNumber;
	public static String presetName;
	static int presetCollection = 2;
	
	public DestinationInfo() {
		
	}
	/*
	DestinationInfo(ArrayList<Double> kilometer, ArrayList<String> originDestination, String presetName) {
	}
	*/
	
	DestinationInfo(int origin, int destination) {
		if (origin > destination) {
			int errorCode = 2;
			System.out.println(possibleErrors(errorCode));
		}
		else if (origin == destination) {
			int errorCode = 1;
			System.out.println(possibleErrors(errorCode));
		}
		else {
			calculatedDistance(origin, destination);
			this.destinationNumber = destination;
		}
	}

	public void calculatedDistance(int origin, int destination) {
		this.origin = DestinationInfo.originDestination.get(origin);
		this.destination = DestinationInfo.originDestination.get(destination);
		for (; origin < destination; origin++) {
			calculatedKilometers += DestinationInfo.kilometers.get(origin);
		}
	}
	
	private String possibleErrors(int errorCode) {
		String errorCodeString = "";
		switch (errorCode) {
			case 1:
				errorCodeString = "The same origin and destination is not possible";
				break;
			case 2:
				errorCodeString = "The destination must not be lesser than the origin";
				break;
		}
		return errorCodeString;
	}
	
	//Destination Presets Setter
	static void setOriginDestination(ArrayList<Double> kilometers, ArrayList<String> originDestination, String presetName) {
		DestinationInfo.kilometers = kilometers;
		DestinationInfo.originDestination = originDestination;
		DestinationInfo.presetName = presetName;
	}

	public static String routeNames(int presetNumber) {
		switch (presetNumber) {
			case 0:
				return "Monumento - PITX Terminal";
			case 1:
				return "PITX Terminal - Monumento";
			default:
				return "Unknown Route";
		}
	}
	
	private static void destinationSelector(int presetNumber) {
		switch (presetNumber) {
			case 0:
				DestinationInfo.setOriginDestination(new ArrayList<>(Arrays.asList(
						0.6, 
						1.0, 
						0.8, 
						1.1, 
						1.5, 
						1.3, 
						1.0, 
						1.6, 
						0.8, 
						2.4, 
						2.4, 
						1.0, 
						1.8, 
						0.5, 
						1.5, 
						1.0, 
						1.5, 
						2.6, 
						1.0, 
						1.0, 
						0.8, 
						3.0)),
				new ArrayList<>(Arrays.asList(
						"Monumento", 
						"Bagong Barrio",
						"Balintawak", 
						"Kaingin", 
						"Roosevelt", 
						"North Avenue",
						"Quezon Avenue",
						"Mega-Q Mart",
						"Main Avenue",
						"Santolan", 
						"Ortigas", 
						"Guadalupe", 
						"Buendia", 
						"Ayala", 
						"Ayala Terminal",
						"Tramo", 
						"Taft Avenue",
						"Roxas Boulevard",
						"SM Mall Of Asia",
						"Macapagal Bradco Avenue",
						"City of Dreams",
						"Ayala Malls Manila Bay",
						"PITX Terminal")),
				"Monumento - PITX Terminal"
				);
			break;
		case 1:
			DestinationInfo.setOriginDestination(
				new ArrayList<>(Arrays.asList(
						3.0,
						0.8,
						1.0,
						1.0,
						2.6,
						1.5,
						1.0,
						1.5,
						0.5,
						1.8,
						1.0,
						2.4,
						2.4,
						0.8,
						1.6,
						1.0,
						1.3,
						1.5,
						1.1,
						0.8,
						1.0,
						0.6)),
				new ArrayList<>(Arrays.asList( 
						"PITX Terminal",
						"Ayala Malls Manila Bay",
						"City of Dreams",
						"Macapagal Bradco Avenue",
						"SM Mall Of Asia",
						"Roxas Boulevard",
						"Taft Avenue",
						"Tramo", 
						"Ayala Terminal",
						"Ayala",  
						"Buendia",
						"Guadalupe",
						"Ortigas",  
						"Santolan", 
						"Main Avenue",
						"Mega-Q Mart",
						"Quezon Avenue",
						"North Avenue",
						"Roosevelt", 
						"Kaingin",   
						"Balintawak",
						"Bagong Barrio",
						"Monumento")),
				"PITX Terminal - Monumento"
				);
			break;
		}
	}
		
	public static void presetSetter(int presetNumber) {
		if (presetNumber < presetCollection)
			DestinationInfo.presetNumber = presetNumber;
			destinationSelector(presetNumber);
	}
}
