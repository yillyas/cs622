package carRent;

import java.time.LocalDate;
import java.util.LinkedList;


public class Vehicle {
	private static int id = 0;
	private int vehicleID;
	private String owner;
	private String make;
	private int year;
	private String model;
	private InsurancePlan insurancePlan;
	private int rent;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isBooked = false;
	private boolean isListed = false;
	private int zipCode;
	private LinkedList<Account> rentHistory = new LinkedList<Account>();
	
	public Vehicle(String owner, String make, int year, String model, InsurancePlan insurancePlan, int rent, 
			 int zipCode) {
		super();
		this.owner = owner;
		this.make = make;
		this.year = year;
		this.model = model;
		this.insurancePlan = insurancePlan;
		this.rent = rent;
		this.zipCode = zipCode;
		Vehicle.id ++;
		this.setVehicleID(Vehicle.id);
	}
	
	public String getOwner() {
		return owner;
	}

	public String getMake() {
		return make;
	}

	public int getYear() {
		return year;
	}

	public String getModel() {
		return model;
	}

	public InsurancePlan getInsurancePlan() {
		return insurancePlan;
	}

	public int getRent() {
		return rent;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public int getZipCode() {
		return zipCode;
	}
	
	public int getVehicleID() {
		return vehicleID;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setMake(String make) {
		this.make = make;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setInsurancePlan(InsurancePlan insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public void setStartDate(LocalDate startdate) {
		this.startDate = startdate;
	}

	public void setEndDate(LocalDate enddate) {
		this.endDate = enddate;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public void setVehicleID(int vehicleID) {
		this.vehicleID = vehicleID;
	}

	public boolean isListed() {
		return isListed;
	}

	public void setListed(boolean isListed) {
		this.isListed = isListed;
	}

	public LinkedList<Account> getRenters() {
		return rentHistory;
	}
	
	public String printRents() { // print selected information from renter account
		//return rentHistory; 
		String renters = "";
		for (Account a : rentHistory) {
			if (a instanceof Owner ) {
				renters += " RenterID: " + ((Owner)a).getId() + " RenterName: " + ((Owner)a).getName();
			}else {
				renters += " RenterID: " + ((Renter)a).getId() + " RenterName: " + ((Renter)a).getName();
			}	
		}
		return renters;
	}

	public void setRenters(Account renter) {
		if (this.rentHistory == null) {
			rentHistory = new LinkedList<Account>(); 
		}
		rentHistory.add(renter);
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleID=" + vehicleID + ", owner=" + owner + ", make=" + make + ", year=" + year + ", model="
				+ model + ", insurancePlan=" + insurancePlan + ", rent=" + rent + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", isBooked=" + isBooked + ", isListed=" + isListed + ", zipCode=" + zipCode
				+ ", renters=" + "[" + printRents() + "]" + "]";
	}	
}
