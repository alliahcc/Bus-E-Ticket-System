package ticket.info;

import java.util.ArrayList;

public class Fare {
	public static double tripRevenue = 0;
	
	public Fare() {
		
	}
	public static void addRevenue(double ticketFare) {
		tripRevenue += ticketFare;
	}

	// Method to get the current total trip revenue
	public static double getTripRevenue() {
		return tripRevenue;
	}

	// Method to reset the trip revenue (call when a trip ends)
	public static void resetTripRevenue() {
		tripRevenue = 0.0;
	}

	static void calculateFare(double fare) {
		tripRevenue += fare;
	}
}
