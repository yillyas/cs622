package carRent;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Renter extends Account {
	private int ID;
	private double totalBalance;
	private double cupon;
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
	
	public void removeRentalChangers(int bookingID) {
		if (this.balancePerBooking != null) { 
			balancePerBooking.remove(bookingID);	// remove the booking from user account
		}
	}
	
	/**
	 * @param vehicle
	 * updates user rent history by adding the vehicle to a list of vehicles rented by user
	 */
	public void addToUserRentHistory(Vehicle vehicle) { 
		if (this.vehiclesRented == null) {
			vehiclesRented = new LinkedList<Vehicle>(); 
		}
		vehiclesRented.add(vehicle);
	}
	
	public void removeFromUserRentHistory(Vehicle vehicle) {
		if (this.vehiclesRented != null) {
			vehiclesRented.remove(vehicle);
		}
	}
	
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
		balancePerBooking.put(bookingID, charges);
	}
	
	@Override
	public double getBalance() {
		double balance = cupon;
		if (balancePerBooking != null) {
		Collection<Double> chargerPerBooking = balancePerBooking.values();
			for (double b : chargerPerBooking) {
				balance += b; 
			}
		}
		return balance;
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
	public double addVoucher(double voucher) {
		return this.cupon -= voucher; // the balance shows what user owes, so the coupon should reduce the balance in this case
	}

	@Override
	public String toString() {
		return "Renter [ID=" + ID + ", totalBalance=" + this.getBalance() + ", name=" + name + ", state=" + state + ", city="
				+ city + ", zipCode=" + zipCode + "]";
	}

}
