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
		Vehicle vehicle1 = new Vehicle(owner, "Audi", 2015, "A4", insurance, 70, 20814);
		Renter renter1 = new Renter("Smith", "MD", "Rockville", 20851);
		Booking booking1 = Booking.book(vehicle1, owner, renter1, "05/16/2019", "05/18/2019");
		vehicle1.addBookingHistory(booking1.getStartDate(), booking1);
		renter1.addToUserRentHistory(vehicle1);		
		
		Booking booking2 = Booking.book(vehicle1, owner, renter1, "05/19/2019", "05/21/2019");
		vehicle1.addBookingHistory(booking2.getStartDate(), booking2);
		assertEquals(2,renter1.vehiclesRentedInPastMonth().size());
	}
	
	@Test
	void testStoreBookingInformation () {
		booking = Booking.book(vehicle, owner, renter, "05/16/2019", "05/18/2019");
		int id = owner.storeBookingInformation(booking);
		assertEquals(5,id);
	}
	
	@Test
	void testShowBookingInformation () {
		Booking bookingInfo1 = Account.showBookingInformation(5);
		Booking bookingInfo2 = Account.showBookingInformation(5);
		assertEquals(bookingInfo1.getBookingID(),bookingInfo2.getBookingID());
	}
	

}
