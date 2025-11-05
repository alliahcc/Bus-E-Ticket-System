package ticket.info;

public class BusInfo {
	public String busCompany;
	public String vehicleNumber;
	public String driverName;
	public String conductorName;
	public static int seatCount = 30;
	
	public BusInfo(String busCompany, String vehicleNumber, String driverName, String conductorName) {
		this.busCompany = busCompany;
		this.vehicleNumber = vehicleNumber;
		this.driverName = driverName;
		this.conductorName = conductorName;
	}
	
	public BusInfo getInfo() {
		return this;
	}
}
