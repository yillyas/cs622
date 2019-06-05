package carRent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Renter extends Account /*implements Comparable<Renter>*/ {
	private int ID;
	private double totalBalance;
	private String name;
	private String state;
	private String city;
	private int zipCode;
	//private LinkedList<Vehicle> vehiclesRented;
	private HashMap<Integer, Double> balancePerBooking; // <bookingID, changersPerbooking>
	
	
	public Renter(String name, String state, String city, int zipCode) {
		super();
		Account.accountID ++; 
		this.ID = Account.accountID;
		this.totalBalance = 0;
		this.name = name;
		this.state = state;
		this.city = city;
		this.zipCode = zipCode;
		this.vehiclesRented = new LinkedList<Vehicle>();
		
	}
	
	public int getId() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public void setBalance(double balance) {
		this.totalBalance = balance;
	}

	public double getRentalChangers(int bookingID) {
		double charges = 0;
		if (this.balancePerBooking == null) { 
			charges = balancePerBooking.get(bookingID);	
		}
		return charges;
	}
	
	public void setRentalChangers(int bookingID, double rent) {
		if (this.balancePerBooking != null) { 
			balancePerBooking.put(bookingID, rent);	// override the existing value
		}
	}
	
	/**
	 * @param vehicle
	 * updates user rent history by adding the vehicle to a list of vehicles rented by user
	 */
	public void updateUserRentHistory(Vehicle vehicle) { 
		if (this.vehiclesRented == null) {
			vehiclesRented = new LinkedList<Vehicle>(); 
		}
		vehiclesRented.add(vehicle);
	}
	
	/*
	public LinkedList<Vehicle> vehiclesRentedInPastMonth() {
		LinkedList<Vehicle> pastMonthRentals = new LinkedList<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate pastMonthDate = currentDate.minusMonths(1);
		if (vehiclesRented != null) {
			for (Vehicle v : vehiclesRented) {
				Set<LocalDate> vehicleBookDates = v.getBookingHistory().keySet(); // all the dates that vehicle was booked on
				for (LocalDate d : vehicleBookDates) {
					if (d.isAfter(pastMonthDate) && d.isBefore(currentDate)) { // verify the booking date is between past 30 days
						pastMonthRentals.add(v);
					}
				}
			}
			
		}
		return pastMonthRentals;
	}
	*/
	
	/**
	 * @param bookingID
	 * @param charges
	 * updated the total account balance and also added the booking ID and charges to user account.
	 */
	@Override
	public void addRentalChangers(int bookingID, Double charges) {
		if (this.balancePerBooking == null) { // initialize the HashMap if it's empty.
			balancePerBooking = new HashMap<Integer,Double>();
		}
		totalBalance += charges; // renter owes this money.
		balancePerBooking.put(bookingID, charges);
	}
	
	@Override
	public double getBalance() {
		return totalBalance;
	}
	/**
	 * @return vehicles
	 * returns the list of vehicles rented by the renter
	 */
	@Override
	public LinkedList<Vehicle> getUserRentHistory() {
		return this.vehiclesRented;
	}

	@Override
	public String toString() {
		return "Renter [ID=" + ID + ", name=" + name + ", state=" + state + ", city=" + city + ", zipCode=" + zipCode
				+ "]";
	}
	
	@Override
	public double addBalanceFromCoupon(double voucher) {
		return this.totalBalance -= voucher; // the balance shows what user owes, so the coupon should reduce the balance in this case
	}

	/*
	@Override
	public int compareTo(Renter other) {
		return Integer.compare(this.vehiclesRentedInPastMonth().size(), other.vehiclesRentedInPastMonth().size());
	}		

	*/
}
