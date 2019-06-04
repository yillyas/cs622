package carRent;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Class is used to book the vehicles and keep track of the bookings
 * @author yasirilyas
 */
public class Booking { 

	private static int bookingNumber = 0;
	private Vehicle vehicle;
	private Account owner;
	private Account renter;
	private int noDays;
	private int bookingID;
	private String startDate;
	private String endDate;
	
	public Booking(Vehicle vehicle, Account owner, Account renter, String startDate, String endDate) {
		super();
		Booking.bookingNumber++; // auto generate the booking IDs
		this.bookingID = Booking.bookingNumber;
		this.vehicle = vehicle;
		this.owner = owner;
		this.renter = renter;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public Owner getOwner() {
		return (Owner) owner;
	}

	public Renter getRenter() {
		return (Renter) renter;
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
	
	/**
	 * @param vehicle
	 * @param owner
	 * @param renter
	 * @param startDate
	 * @param endDate
	 * @return bookingDetail
	 * 
	 * Method performs the following tasks
	 *  1. Marks the vehicle as booked.
	 *  2. Calculates the booking period based on start and end date.
	 *  3. return a booking object with booking details. 
	 */
	public static Booking book(Vehicle vehicle, Account owner, Account renter, String startDate, String endDate) {
		Booking bookingDetail = null;
	//	LocalDate currentStratDate = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("MM/dd/yyyy")); // convert string to date
	//	LocalDate lastTripEndDate = vehicle.getEndDate();
		//vehicle.getBookingHistory()
		//if (currentStratDate.isAfter(lastTripEndDate)) {  // date.isAfter(pastMonthDate) && date.isBefore(currentDate)
		if (!(vehicle.isBooked())) {
			vehicle.setBooked(true);
			bookingDetail = new Booking(vehicle, owner, renter, startDate, endDate);
			LocalDate bookingStartDate = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			LocalDate bookingEndDate = LocalDate.parse(endDate,DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			/*
			vehicle.setStartDate(bookingStartDate); // updated the vehicles dates based on current booking
			vehicle.setEndDate(bookingEndDate);
			*/
			
			Period period = Period.between(bookingStartDate , bookingEndDate);
			int days = period.getDays();
			bookingDetail.setNoDays(days);
		}else {
			System.out.println("Vehicle is already booked between these dates, pleaase pick a different date.");
		}
		
		return bookingDetail;
	}
	/**
	 * @param booking
	 * @return balance
	 * Method performs the following tasks
	 *  1. charges rent to renter based on vehicle rent and trip duration
	 *  2. updates the renter account to reflect the charges
	 *  3. calculates the owners share based on the type of vehicle insurance plan
	 *  4. updates the owners account balance
	 */
	public HashMap<String, Double> calculateCost() { 
		//double balance [] = new double[2];
		HashMap<String,Double> balance = new HashMap<>();
		int rent = this.getVehicle().getRent(); // get the rent
		int noOfDays = this.getNoDays(); // get the number of days
		InsurancePlan insurance = this.getVehicle().getInsurancePlan();
		int insuranceCostPerTrip = insurance.getCostPerTrip(); // Polymorphism
		double ownerBalance = (rent * noOfDays) - insuranceCostPerTrip;
		balance.put("ownerTripBalance", ownerBalance);
		double renterBalance = (rent * noOfDays);
		balance.put("renterTripBalance", renterBalance);
		return balance; // useful for unit testing
	}

}
