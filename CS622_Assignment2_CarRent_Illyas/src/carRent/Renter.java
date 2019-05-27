package carRent;

import java.util.HashMap;
import java.util.LinkedList;

public class Renter extends Account {
	private int ID;
	private double totalBalance;
	private String name;
	private String state;
	private String city;
	private int zipCode;
	private LinkedList<Vehicle> vehiclesRented;
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
	
	public double getBalance() {
		return totalBalance;
	}

	public void setBalance(double balance) {
		this.totalBalance = balance;
	}

	public void updateUserRentHistory(Vehicle vehicle) { // user history: list of all the vehicles rented by the user
		if (this.vehiclesRented == null) {
			vehiclesRented = new LinkedList<Vehicle>(); 
		}
		vehiclesRented.add(vehicle);
	}
	
	public void addRentalChangers(int bookingID, Double charges) {
		if (this.balancePerBooking == null) { // initialize the HashMap if it's empty.
			balancePerBooking = new HashMap<Integer,Double>();
		}
		totalBalance -= charges; // (-) sign shows that the user owes the money.
		balancePerBooking.put(bookingID, charges);
	}
	
	public double getRentalChangers(int bookingID) {
		double charges = 0;
		if (this.balancePerBooking == null) { 
			charges = balancePerBooking.get(bookingID);	
		}
		return charges;
	}
	
	@Override
	public LinkedList<Vehicle> getUserRentHistory() { // return the list of vehicles rented by the user
		return this.vehiclesRented;
	}

	@Override
	public String toString() {
		return "Renter [ID=" + ID + ", name=" + name + ", state=" + state + ", city=" + city + ", zipCode=" + zipCode
				+ "]";
	}		

}
