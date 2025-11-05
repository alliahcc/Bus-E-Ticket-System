package ticket.info;

import java.util.ArrayList;
import java.util.Arrays;

public class RealTimeData extends DestinationInfo {
	
	public static ArrayList<DropOffQueue> dropOffQueue = new ArrayList<DropOffQueue>();
	public static int totalPassengerCount = 0;
	public static int seatAvailability = BusInfo.seatCount;
	public static int currentDestinationCounter = 0;
	
	public RealTimeData() {
		
	}
	
	//Class ArrayList
	public class DropOffQueue {
		public int runningListNumber;
		public int passengerDroppingOff;
		public String currentLocation;
		
		public void dropOffAdder() {
			int tempIndex = -1;
			int i;
			
			for (i = 0; i < dropOffQueue.size(); i++) {
				DropOffQueue temp = dropOffQueue.get(i);
				if (runningListNumber == temp.runningListNumber) {
					tempIndex = i;
					break;
				}
			}
			
			if (tempIndex != -1) {
				dropOffQueue.get(tempIndex).passengerDroppingOff++;				
				seatAvailability--;
				totalPassengerCount++;
			}
			else {
				dropOffQueue.add(this);
				seatAvailability--;
				totalPassengerCount++;
				passengerDroppingOff++;
			}
		}
		
		public static void dropOffSorter() {
			for (int i = 0; i < dropOffQueue.size() - 1; i++) {
				int minIndex = i;
				for (int j = i + 1; j < dropOffQueue.size(); j++) {
					if (dropOffQueue.get(j).runningListNumber < dropOffQueue.get(minIndex).runningListNumber) {
						minIndex = j;
					}
				}
				if (minIndex != i) {
					DropOffQueue temp2 = dropOffQueue.get(i);
					dropOffQueue.set(i, dropOffQueue.get(minIndex));
					dropOffQueue.set(minIndex, temp2);
				}
			}
		}
		
		static void passengerDrop() {
			DropOffQueue passCountHolder =  dropOffQueue.get(0);
			
			seatAvailability += passCountHolder.passengerDroppingOff;
			totalPassengerCount -= passCountHolder.passengerDroppingOff;
			
			dropOffQueue.remove(0);
		}
		
		public static void currentDestinationIterator() {
			RealTimeData.currentDestinationCounter++;
			int tempCounter = RealTimeData.currentDestinationCounter;
			DropOffQueue tempObject = dropOffQueue.get(0);
			int tempCurrentDestination = RealTimeData.currentDestinationCounter;
			if (tempCurrentDestination == tempObject.runningListNumber) {
				passengerDrop();
			};
			if (tempCounter < DestinationInfo.originDestination.size()) {
				Fare.endTrip();
			}
		}
		
		public static void endTrip() {
			RealTimeData.currentDestinationCounter = 0;
		}
	}
}