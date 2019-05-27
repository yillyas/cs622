package carRent;

import java.util.LinkedList;

public class Renter extends Account {
	private int ID;
	private String name;
	private String state;
	private String city;
	private int zipCode;
	private LinkedList<Vehicle> vehiclesRented;
	
	
	public Renter(String name, String state, String city, int zipCode) {
		super();
		Account.accountID ++; 
		this.ID = Account.accountID;
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

	public void updateUserRentHistory(Vehicle vehicle) { // user history: list of all the vehicles rented by the user
		if (this.vehiclesRented == null) {
			vehiclesRented = new LinkedList<Vehicle>(); 
		}
		vehiclesRented.add(vehicle);
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
