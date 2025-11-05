package ticket.info;

import java.util.ArrayList;

public class Fare {
	private static double totalRevenue = 0;
	public static double tripRevenue = 0;
	static ArrayList<Double> tripRevenueHistory = new ArrayList<Double>();
	
	public Fare() {
		
	}
	
	static void calculateFare(double fare) {
		tripRevenue += fare;
	}
	
	//Every after the trip is over, it will be put to the total revenue
	// and be logged
	static void endTrip() {
		totalRevenue += tripRevenue;
		tripRevenueHistory.add(tripRevenue);
	}
	
	public static void resetTotalRevenue() {
		totalRevenue = 0;
	}
	
	public static double getTotalRevenue() {
		return totalRevenue;
	}
}
