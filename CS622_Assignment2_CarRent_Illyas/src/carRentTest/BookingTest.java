/**
 * 
 */
package carRentTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carRent.Account;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

/**
 * @author yasir illyas
 *
 */
class BookingTest {
	private Owner owner;
	private Renter renter;
	private InsurancePlan insurance;
	private Vehicle vehicle;
	private String startDate = "06/01/2019";
	private String endDate = "06/05/2019";
	private Booking booking;
	
	@BeforeEach
	public void setup() {
		owner = new Owner("John", "MD", "Bethesda", 20814);
		renter = new Renter("Jane", "MD", "Rockville", 20851);
		insurance = new BasicPlan();
		vehicle = new Vehicle(owner, "BMW", 2015, "328i", insurance, 70, 20814);
		owner.addVehicle(vehicle);
		booking = Booking.book(vehicle,owner,renter, startDate, endDate);
		
	}

	/**
	 * Test method for {@link carRent.Booking#book(carRent.Vehicle, carRent.Account, carRent.Account, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testBook() {
		assertEquals(4,booking.getNoDays());
	}

	/**
	 * Test method for {@link carRent.Booking#calculateCost(carRent.Booking)}.
	 */
	@Test
	void testCalculateCost() {
        double[] balance = Booking.calculateCost(booking);
		assertEquals(255.0,balance[0]);
	}

}
