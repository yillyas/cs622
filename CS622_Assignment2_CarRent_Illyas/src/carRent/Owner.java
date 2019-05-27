package carRent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;


public class Owner extends Account {
	private int ID;
	private double totalBalance;
	private String name;
	private String state;
	private String city;
	private int zipCode;
	private HashMap<Integer, Vehicle> vehiclesOwned;
	private LinkedList<Vehicle> vehiclesRented;
	private HashMap<Integer, Double> balancePerBooking; // <bookingID, changersPerbooking>
	
	public Owner(String name, String state, String city, int zipCode) {
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

	public void addVehicle(Vehicle vehicle) { // add vehicles to the account
		if (this.vehiclesOwned == null) {
			vehiclesOwned = new HashMap<Integer,Vehicle>(); 
		}
		vehiclesOwned.put(vehicle.getVehicleID(), vehicle);
	}
	public LinkedList<Vehicle> getVehicle() { // get the vehicles owned by an account
		LinkedList<Vehicle> vehicles =  new LinkedList<Vehicle>(vehiclesOwned.values());   
		return vehicles;
	}
	
	public void addRentalChangers(int bookingID, Double charges) {
		if (this.balancePerBooking == null) { // initialize the HashMap if it's empty.
			balancePerBooking = new HashMap<Integer,Double>();
		}
		totalBalance += charges; // add the current rental charges to total balance
		balancePerBooking.put(bookingID, charges);
	}
	
	public double getRentalChangers(int bookingID) {
		double charges = 0;
		if (this.balancePerBooking == null) { 
			charges = balancePerBooking.get(bookingID);	
		}
		return charges;
	}
	
	public Vehicle getVehicleByID(int id) {
		return vehiclesOwned.get(id);
	}
	public double getBalance() {
		return totalBalance;
	}

	public void setBalance(double balance) {
		this.totalBalance = balance;
	}

	public void listVehicleforRent (Vehicle vehicle, int numberOfDays) {
		if (!vehicle.isListed()) {
			LocalDate startDate = LocalDate.now();
			LocalDate endDate = startDate.plusDays(numberOfDays);
			int zip = vehicle.getZipCode();
			vehicle.setStartDate(startDate); // set the availability dates for the vehicle
			vehicle.setEndDate(endDate);
			VehicleRepository.allVehicle.add(vehicle); // add the vehicle to list of all vehicles.
			
			if (VehicleRepository.vehicleByZip.get(zip) != null) {
				LinkedList<Vehicle> vehiclesListedForCurrentZip = VehicleRepository.vehicleByZip.get(zip);
				vehiclesListedForCurrentZip.add(vehicle); // update the list for current zip code
				VehicleRepository.vehicleByZip.put(zip, vehiclesListedForCurrentZip);		
			} 
			else {
				LinkedList<Vehicle> vehiclesListedForCurrentZip = new LinkedList<Vehicle>();
				vehiclesListedForCurrentZip.add(vehicle); // update the list for current zip code
				VehicleRepository.vehicleByZip.put(zip, vehiclesListedForCurrentZip);		
			}
			vehicle.setListed(true);
			System.out.println("Vehicle is listed.");
		}
		else {
			System.out.println("Selected Vehicle is already Listed. Try another one.");
		}
	}
	
	
	public void updateUserRentHistory(Vehicle vehicle) { // user history: list of all the vehicles rented by the user
		if (this.vehiclesRented == null) {
			vehiclesRented = new LinkedList<Vehicle>(); 
		}
		vehiclesRented.add(vehicle);
	}
	
	
	@Override
	public LinkedList<Vehicle> getUserRentHistory() { // return the list of vehicles rented by the user
		
		LinkedList<Vehicle> ownedVehicle = new LinkedList<Vehicle>(vehiclesOwned.values());
		LinkedList<Vehicle> vehicle = new LinkedList<Vehicle>();
		if (ownedVehicle != null) { // Vehicles rented to other users
			for (Vehicle v : ownedVehicle) { 
				if (v.getRenters().size() !=0) { //only include the vehicles that were booked in past
					vehicle.add(v); 
				}
			}
		}
		if (vehiclesRented != null) { // vehicles rented by the Owner from the other users
			for (Vehicle v : vehiclesRented) {
				vehicle.add(v); // add all the rented vehicle to the list
			}
		}
		return vehicle;
	}

	@Override
	public String toString() {
		return "Owner [ID=" + ID + ", name=" + name + ", state=" + state + ", city=" + city + ", zipCode=" + zipCode
				+ ", vehiclesOwned=" + vehiclesOwned + "]";
	}	

}
