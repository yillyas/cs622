package carRentTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

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

class ClaimTest {
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
		booking = Booking.book(vehicle, owner, renter, "05/10/2019", "05/12/2019");
		HashMap<String,Double> tripRent = booking.calculateCost();       
		booking.getOwner().addRentalChangers(booking.getBookingID(), tripRent.get("ownerTripBalance"));
		booking.getRenter().addRentalChangers(booking.getBookingID(), tripRent.get("renterTripBalance"));
	}
	
	@Test
	void testClaim() {
		claim = new Claim<>(renter, booking); // create a new claim instance
		assertEquals(renter.getId(),claim.getBooking().getRenter().getId());
	}
	
	@Test
	void testUpdateBalance() {
		double currentBalance = owner.getBalance();
		claim = new Claim<>(owner, booking);
		claim.updateBalance(40.0);
		assertEquals((currentBalance + 40), owner.getBalance());
	}
}
