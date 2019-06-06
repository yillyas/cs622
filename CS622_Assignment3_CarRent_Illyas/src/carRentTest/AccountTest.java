package carRentTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carRent.Account;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.Claim;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

class AccountTest {
	private Owner owner;
	private Renter renter;
	private InsurancePlan insurance;
	private Vehicle vehicle;
	private Booking booking;
	private Claim<Account, Booking> claim;
	
	@BeforeEach
	public void setup() {	
		owner = new Owner("John", "MD", "Bethesda", 20814);
		renter = new Renter("Jane", "MD", "Rockville", 20851);
		insurance = new BasicPlan();
		vehicle = new Vehicle(owner, "BMW", 2015, "328i", insurance, 70, 20814);
	}
	
	@Test
	void testNewBooking() {
		booking = Booking.book(vehicle, owner, renter, "05/13/2019", "05/15/2019");
		vehicle.addBookingHistory(booking.getStartDate(), booking);
		HashMap<String,Double> tripRent = booking.calculateCost();
		double balance = tripRent.get("ownerTripBalance");
		assertEquals(115,balance);
	}
	
	
	@Test
	void testFileClaim() {
		booking = Booking.book(vehicle, owner, renter, "05/16/2019", "05/18/2019");
		HashMap<String,Double> tripRent = booking.calculateCost();
		vehicle.addBookingHistory(booking.getStartDate(), booking); 
		booking.getOwner().addRentalChangers(booking.getBookingID(), tripRent.get("ownerTripBalance"));
		booking.getRenter().addRentalChangers(booking.getBookingID(), tripRent.get("renterTripBalance"));
		claim = new Claim<Account, Booking>(owner, booking);
		claim.updateBalance(20.0);
		assertEquals(135,owner.getBalance());
	}
	
	@Test
	void testVehiclesRentedInPastMonth() {
		Booking booking1 = Booking.book(vehicle, owner, renter, "05/16/2019", "05/18/2019");
		vehicle.addBookingHistory(booking1.getStartDate(), booking1);
		renter.updateUserRentHistory(vehicle);
		
		
		Vehicle vehicle2 = new Vehicle(owner, "Audi", 2015, "A4", insurance, 70, 20815);
		Booking booking2 = Booking.book(vehicle2, owner, renter, "05/22/2019", "05/24/2019");
		vehicle2.addBookingHistory(booking2.getStartDate(), booking2);
		renter.updateUserRentHistory(vehicle2);
		
		LinkedList<Vehicle> pastMonthRentals = renter.vehiclesRentedInPastMonth();
		assertEquals(2,pastMonthRentals.size());
	}
	

}
