package ticket.info;

import java.util.ArrayList;
// import java.text.DecimalFormat; // Remove if not used in this class

public class GroupedPassenger {
	// Change the list to hold Passenger objects instead of just fares
	// This gives you more detailed info about each passenger
	ArrayList<Passenger> passengerList = new ArrayList<>();

	public GroupedPassenger() {
		// Constructor can remain empty or initialize the list
	}

	// Method to add a Passenger object to the group
	public void addPassenger(Passenger passenger) {
		if (passenger != null) {
			passengerList.add(passenger);
		}
	}

	// Method to remove a Passenger object (e.g., the last one added)
	public void removeLastPassenger() {
		if (!passengerList.isEmpty()) {
			passengerList.remove(passengerList.size() - 1);
		}
	}

	// Method to get the list of passengers
	public ArrayList<Passenger> getPassengerList() {
		return passengerList;
	}

	// Method to get the total fare from all passengers in the list
	// This iterates through the list of Passenger objects
	public double getTotalFare() {
		double totalFare = 0;
		for (Passenger passenger : passengerList) {
			totalFare += passenger.getFare(); // Sum up the individual passenger fares
		}
		return totalFare;
	}
}
// You can keep your old totalFare method if you prefer passing an instance,
// but the new getTotalFare() method is more aligned with this class's role.
// public double totalFare(GroupedPassenger groupOfPassengers) {
//     double totalFare = 0;
//     for (double temp