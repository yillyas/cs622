package carRent;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Booking { // batter make it private

	private static int bookingNumber = 0;
	private Vehicle vehicle;
	private Account owner;
	private Account renter;
	private int noDays;
	private int bookingID;
	private String startDate;
	private String endDate;
	//private int vehicleID;
	
	public Booking(Vehicle vehicle, Account owner, Account renter, String startDate, String endDate) {
		super();
		bookingNumber ++;
		this.bookingID = Booking.bookingNumber;
		this.vehicle = vehicle;
		this.owner = owner;
		this.renter = renter;
		this.startDate = startDate;
		this.endDate = endDate;
		//this.noDays = noDays;
		// LocalDate.parse(startDate,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public Account getOwner() {
		return owner;
	}

	public Account getRenter() {
		return renter;
	}

	public int getNoDays() {
		return noDays;
	}

	public int getBookingID() {
		return bookingID;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	} 
	
	public void setNoDays(int days) {
		this.noDays = days;
	}

	public static Booking book(Vehicle vehicle, Account owner, Account renter, String startDate, String endDate) {
		Booking bookingDetail = null;
		if (!(vehicle.isBooked())) {
			vehicle.setBooked(true);
			bookingDetail = new Booking(vehicle, owner, renter, startDate, endDate);
			LocalDate bookingStartDate = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			LocalDate bookingEndDate = LocalDate.parse(endDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			Period period = Period.between(bookingStartDate , bookingEndDate);
			int days = period.getDays();
			//System.out.println("Start Date: " + bookingStartDate);
			//System.out.println("End Date: " + bookingEndDate);
			//System.out.println("Days Booked: " + days);
			bookingDetail.setNoDays(days);
		}
		return bookingDetail;
	}
	
	public static double [] calculateCost (Booking booking) { // incomplete // add a map with Trip id and cost to account
		double balance [] = new double[2];
		int rent = booking.getVehicle().getRent();
		int noOfDays = booking.getNoDays();
		InsurancePlan insurance = booking.getVehicle().getInsurancePlan();
		int insuranceCostPerTrip = insurance.getCostPerTrip(); // Polymorphism
		
		/*
			if (insurance instanceof BasicPlan) { // add getCostPerTrip() as abstract method in the Insurance class
				insuranceCostPerTrip = ((BasicPlan)insurance).getCostPerTrip();
			}
			else if (insurance instanceof StandardPlan) {
				insuranceCostPerTrip = ((StandardPlan)insurance).getCostPerTrip();
			}
			else {
				insuranceCostPerTrip = ((PremiumPlan)insurance).getCostPerTrip();
			}
			
		*/
	
		double ownerBalance = (rent * noOfDays) - insuranceCostPerTrip;
		balance[0] = ownerBalance;
		booking.getOwner().addRentalChangers(booking.bookingID, ownerBalance);  // Polymorphism
		System.out.println("Updated the Owner Account balance.");
		double renterBalance = (rent * noOfDays);
		balance[1] = renterBalance;
		booking.getRenter().addRentalChangers(booking.bookingID, renterBalance);
		System.out.println("Updated the Renter Account balance.");
		//return ownerBalance;
		return balance;
	}

}
