package ticket.info;

class TicketInfo {
	abstract static class TicketTemplate {
		private static double discountRate = .2;
		private String passengerType;
		double finalPrice;
		
		TicketTemplate(String passengerType) {
			this.passengerType = passengerType;
		}
		
		void setFinalPrice(double finalPrice) {
			this.finalPrice = finalPrice;
		}
		
		double discountCalculator(double discountRate, double fare) {
			double discountedPrice = discountRate * fare;
			double finalPrice = fare - discountedPrice;
			return finalPrice;
		}
		
		static double getDiscountRate() {
			return discountRate;
		}
		
		public String getPassengerType() {
			return passengerType;
		}
	}
	
	static class ChildPassenger extends TicketTemplate {
		
		ChildPassenger(double fare) {
			super("Child");
			finalPrice = discountCalculator(TicketTemplate.getDiscountRate(), fare);
			setFinalPrice(finalPrice);
		}
		
	}
	
	static class StudentPassenger extends TicketTemplate {
		
		StudentPassenger(double fare) {
			super("Student");
			finalPrice = discountCalculator(TicketTemplate.getDiscountRate(), fare);
			setFinalPrice(finalPrice);
		}
	}
	
	static class SeniorCitizenPWDPassenger extends TicketTemplate {
		
		SeniorCitizenPWDPassenger(double fare) {
			super("SeniorCitizen / PWD");
			finalPrice = discountCalculator(TicketTemplate.getDiscountRate(), fare);
			setFinalPrice(finalPrice);
		}
	}
}