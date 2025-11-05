package ticket.info;

import java.util.ArrayList; 
import java.text.DecimalFormat;

public class GroupedPassenger {
	ArrayList<Double> group = new ArrayList<Double>();
	
	public GroupedPassenger() {
		
	}
	
	public class AddPassenger {

		//DestinationInfo
		public String origin;
		public String destination;
		public double distance;
		
		//Ticket Discount Type
		public String passengerTypeDiscount;
		public double fare;
		
		//Student = 1
		//Senior Citizen = 2
		//PWD = 3
		
		public AddPassenger(int origin, int destination, int passengerTypeCode) {
			
			//Destination
			DestinationInfo distance = new DestinationInfo(origin, destination);
			this.distance = distance.calculatedKilometers;
			this.origin = distance.origin;
			this.destination = distance.destination;
			
			double tempHolderKilometers = distance.calculatedKilometers;
			
			this.fare = standardFare(tempHolderKilometers);
			
			RealTimeData tempHolder = new RealTimeData();
			RealTimeData.DropOffQueue info = tempHolder.new DropOffQueue();
			
			//Ticket Type
			switch(passengerTypeCode) {
				case 0:
					Fare.calculateFare(this.fare);
					passengerTypeDiscount = "Standard";
					break;
				case 1:
					TicketInfo.TicketTemplate child = new TicketInfo.ChildPassenger(this.fare);
					passengerTypeDiscount = child.getPassengerType();
					this.fare = child.finalPrice;
					Fare.calculateFare(this.fare);
					break;
				case 2:
					TicketInfo.TicketTemplate student = new TicketInfo.StudentPassenger(this.fare);
					passengerTypeDiscount = student.getPassengerType();
					this.fare = student.finalPrice;
					Fare.calculateFare(this.fare);
					break;
				case 3:
					TicketInfo.TicketTemplate seniorCitizenPWD = new TicketInfo.SeniorCitizenPWDPassenger(this.fare);
					passengerTypeDiscount = seniorCitizenPWD.getPassengerType();
					this.fare = seniorCitizenPWD.finalPrice;
					Fare.calculateFare(this.fare);
					break;
			}
			info.currentLocation = distance.destination;
			info.runningListNumber = distance.destinationNumber;
			info.dropOffAdder();
			RealTimeData.DropOffQueue.dropOffSorter();
			
			group.add(this.fare);
		}
		
		double standardFare(double tempHolderKilometers) {
			
			double fare = 0;
			int base1Kilometer = 1;
			int succeedingKilometer = 2;

			double rounded = Math.round(tempHolderKilometers);
			
			if (rounded >= base1Kilometer) {
				rounded -= base1Kilometer;
				fare += 10;
				
				for (int i = 0; rounded >= base1Kilometer; i++) {
					fare += succeedingKilometer;
					rounded -= base1Kilometer;
				}
			}
			else if (rounded < base1Kilometer) {
				rounded -= base1Kilometer;
				fare += 10;
			}
			
			return fare;
		}
	}
	
	public double totalFare(GroupedPassenger groupOfPassengers) {
		double totalFare = 0;
		for (double temp : groupOfPassengers.group) {
			totalFare += temp;
		}
		return totalFare;
	}
}
