package ticket.info;

import java.text.DecimalFormat; // Keep if needed, but not used in this snippet

public class Passenger {

    // DestinationInfo
    public String origin;
    public String destination;
    public double distance;

    // Ticket Discount Type
    public String passengerTypeDiscount;
    public double fare;

    private int passengerTypeCode;

    // Student = 1
    // Senior Citizen = 2
    // PWD = 3
    // Standard = 0

    // Constructor - Now a regular constructor for the standalone Passenger class
    public Passenger(int originIndex, int destinationIndex, int passengerTypeCode) {

        // Destination: Use the indices to get info and calculate distance via DestinationInfo
        DestinationInfo distanceInfo = new DestinationInfo(originIndex, destinationIndex);
        this.distance = distanceInfo.calculatedKilometers;
        // Assuming DestinationInfo constructor sets static origin/destination which you access
        this.origin = distanceInfo.origin;
        this.destination = distanceInfo.destination;
        this.passengerTypeCode = passengerTypeCode;

        double tempHolderKilometers = this.distance; // Use the instance variable

        // Calculate standard fare first
        this.fare = standardFare(tempHolderKilometers);

        // Apply discounts based on passengerTypeCode
        switch (passengerTypeCode) {
            case 0: // Standard
                // The standard fare is already calculated and assigned to this.fare
                passengerTypeDiscount = "Standard";
                // Remove Fare.calculateFare(this.fare); - This should not be done per passenger
                break;
            case 1: // Child
                TicketInfo.TicketTemplate child = new TicketInfo.ChildPassenger(this.fare);
                passengerTypeDiscount = child.getPassengerType();
                this.fare = child.finalPrice;
                // Remove Fare.calculateFare(this.fare);
                break;
            case 2: // Student
                TicketInfo.TicketTemplate student = new TicketInfo.StudentPassenger(this.fare);
                passengerTypeDiscount = student.getPassengerType();
                this.fare = student.finalPrice;
                // Remove Fare.calculateFare(this.fare);
                break;
            case 3: // Senior Citizen / PWD
                TicketInfo.TicketTemplate seniorCitizenPWDPassenger = new TicketInfo.SeniorCitizenPWDPassenger(this.fare);
                passengerTypeDiscount = seniorCitizenPWDPassenger.getPassengerType();
                this.fare = seniorCitizenPWDPassenger.finalPrice;
                // Remove Fare.calculateFare(this.fare);
                break;
            // Add other cases if you have more passenger types
            default:
                passengerTypeDiscount = "Unknown"; // Handle unknown types
                this.fare = 0; // Set fare to 0 or handle as an error
                break;
        }

        // Remove RealTimeData logic - This doesn't belong in the passenger's fare calculation
        // RealTimeData tempHolder = new RealTimeData();
        // RealTimeData.DropOffQueue info = tempHolder.new DropOffQueue();
        // info.currentLocation = distanceInfo.destination;
        // info.runningListNumber = distanceInfo.destinationNumber;
        // info.dropOffAdder();
        // info.dropOffSorter();

        // REMOVE THIS LINE: This Passenger object itself will be added to a list elsewhere
        // group.add(this.fare); // This line is specific to the inner class and needs to be removed
    }

    // Keep your standardFare method - Make it private as it's an internal helper
    private double standardFare(double tempHolderKilometers) {
        double fare = 0;
        int base1Kilometer = 1;
        int succeedingKilometerRate = 2;

        double rounded = Math.round(tempHolderKilometers);

        if (rounded >= base1Kilometer) {
            fare += 10; // Base fare for the first kilometer
            rounded -= base1Kilometer;

            // Add fare for succeeding kilometers
            fare += rounded * succeedingKilometerRate;

        } else if (rounded < base1Kilometer && rounded > 0) {
            fare += 10; // Still apply base fare for any distance traveled above 0
        }

        return fare;
    }

    // Optional: Add getters for properties if you want to access them from outside
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public double getDistance() { return distance; }
    public String getPassengerTypeDiscount() { return passengerTypeDiscount; }
    public double getFare() { return fare; }

    public int getPassengerTypeCode() {
        return this.passengerTypeCode;
    }

    // Optional: Implement Serializable or Parcelable if you need to pass Passenger objects via Intent
    // public class Passenger implements java.io.Serializable { ... }
}