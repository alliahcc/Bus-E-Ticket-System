package ticket.info;

public class BusInfo {
	public String vehicleNumber;
	public String driverName;
	public String conductorName;
	public static int seatCount = 30;
	
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}
}
