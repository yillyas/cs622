package carRentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carRent.Account;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.CarRent;
import carRent.IncorrectAccountException;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;
import carRent.VehicleRepository;

class CarRentTest {
	private Owner owner1;
	private Renter renter;
	private InsurancePlan insurance;
	private Vehicle vehicle;
	final static double COUPON = 20.0;
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	@BeforeEach
	public void setup() {
		owner1 = new Owner("John", "MD", "Bethesda", 20814);
		renter = new Renter("Jane", "MD", "Rockville", 20851);
		insurance = new BasicPlan(); // $25 per trip
		vehicle = new Vehicle(owner1, "BMW", 2015, "328i", insurance, 70, 20814);
		owner1.addVehicle(vehicle);
		VehicleRepository.allVehicle.add(vehicle);
		LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();
		vehicles.add(vehicle);
		VehicleRepository.vehicleByZip.put(20814, vehicles);
		
	}

	@Test
	void testGetAccountBalance() throws IncorrectAccountException {
		Account.newbooking(vehicle, owner1, renter, "05/16/2019", "05/17/2019");
		double balance = owner1.getBalance();
		assertEquals(45.0,balance,"Initial account total was not zero");
	}
	
	@Test
	void accountBalanceIncrementsWhenAmountIsAdded() {
		owner1.addRentalChangers(1, 70.0);
		double balance = ((Owner)owner1).getBalance();
		assertEquals(70.0,balance,"Account Balance amount was not correct");
	}


	@Test
	void testAddVehicleToAccount() {
		owner1.addVehicle(vehicle);
		assertEquals(1,owner1.getVehicle().size());
	}
	
	@Test
	void testUserOftheMonth () {
		accounts.put(owner1.getId(), owner1);
		accounts.put(renter.getId(), renter);
		Owner owner2 = new Owner("Will", "MD", "CheveyChase", 20815);
		accounts.put(owner2.getId(), owner2);
		
		Booking booking1 = Booking.book(vehicle, owner2, renter, "05/16/2019", "05/18/2019");
		HashMap<String,Double> tripRent1 = booking1.calculateCost();  
		booking1.getOwner().addRentalChangers(booking1.getBookingID(), tripRent1.get("ownerTripBalance"));
		booking1.getRenter().addRentalChangers(booking1.getBookingID(), tripRent1.get("renterTripBalance"));
		vehicle.addBookingHistory(booking1.getStartDate(), booking1);
		renter.addToUserRentHistory(vehicle);
		owner2.addToUserRentHistory(vehicle);
		
		
		Vehicle vehicle2 = new Vehicle(owner1, "Audi", 2015, "A4", insurance, 80, 20815);
		Booking booking2 = Booking.book(vehicle2, owner1, renter, "05/22/2019", "05/24/2019");
		HashMap<String,Double> tripRent2 = booking2.calculateCost();  
		booking1.getOwner().addRentalChangers(booking2.getBookingID(), tripRent2.get("ownerTripBalance"));
		booking1.getRenter().addRentalChangers(booking2.getBookingID(), tripRent2.get("renterTripBalance"));
		vehicle2.addBookingHistory(booking2.getStartDate(), booking2);
		renter.addToUserRentHistory(vehicle2);
		owner1.addToUserRentHistory(vehicle);
		Account currentWinnerAccount = CarRent.userOftheMonth(accounts.values());
		
		
		assertEquals(280.0,(currentWinnerAccount.getBalance()));
	}
	
	
}
