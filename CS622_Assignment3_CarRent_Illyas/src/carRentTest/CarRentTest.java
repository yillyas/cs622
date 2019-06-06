package carRentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carRent.Account;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.CarRent;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

class CarRentTest {
	private Owner owner;
	private Renter renter;
	private InsurancePlan insurance;
	private Vehicle vehicle;
	final static double COUPON = 20.0;
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	@BeforeEach
	public void setup() {
		owner = new Owner("John", "MD", "Bethesda", 20814);
		renter = new Renter("Jane", "MD", "Rockville", 20851);
		insurance = new BasicPlan();
		vehicle = new Vehicle(owner, "BMW", 2015, "328i", insurance, 70, 20814);
		owner.addVehicle(vehicle);
		
	}

	@Test
	void testGetAccountBalance() {
		double balance = ((Owner)owner).getBalance();
		assertEquals(0.0,balance,"Initial account total was not zero");
	}
	
	@Test
	void accountBalanceIncrementsWhenAmountIsAdded() {
		owner.addRentalChangers(1, 70.0);
		double balance = ((Owner)owner).getBalance();
		assertEquals(70.0,balance,"Account Balance amount was not correct");
	}


	@Test
	void testAddVehicleToAccount() {
		owner.addVehicle(vehicle);
		assertEquals(1,owner.getVehicle().size());
	}
	
	@Test
	void testUserOftheMonth () {
		accounts.put(owner.getId(), owner);
		accounts.put(renter.getId(), renter);
		Owner owner2 = new Owner("Will", "MD", "CheveyChase", 20815);
		accounts.put(owner2.getId(), owner2);
		
		Booking booking1 = Booking.book(vehicle, owner, renter, "05/16/2019", "05/18/2019");
		vehicle.addBookingHistory(booking1.getStartDate(), booking1);
		renter.updateUserRentHistory(vehicle);
		
		Vehicle vehicle2 = new Vehicle(owner2, "Audi", 2015, "A4", insurance, 70, 20815);
		Booking booking2 = Booking.book(vehicle2, owner2, renter, "05/22/2019", "05/24/2019");
		vehicle2.addBookingHistory(booking2.getStartDate(), booking2);
		renter.updateUserRentHistory(vehicle2);
		
		Account currentWinnerAccount = CarRent.userOftheMonth(accounts.values());
		assertEquals(6,((Renter)currentWinnerAccount).getId());
	}

}
