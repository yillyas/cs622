package carRentTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Database;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

class DatabaseTest {
	private Owner owner = new Owner("John", "MD", "Bethesda", 20814);
	private Renter renter = new Renter("Jane", "MD", "Rockville", 20851);
	private InsurancePlan insurance = new BasicPlan();
	private Vehicle vehicle = new Vehicle(owner, "BMW", 2015, "328i", insurance, 70, 20814);
	private Booking booking = Booking.book(vehicle, owner, renter, "05/16/2019", "05/18/2019");
	
	
	@BeforeEach
	public void setup() {
		Database.init();
	}

	
	@Test
	void testInsertAccount() {
		Database.insertAccount(owner);
		Map<String, Object> account = Database.selectAccount(owner.getId());
		assertEquals("John",account.get("NAME"));
	}


	@Test
	void testInsertVehicle() {
		Database.insertVehicle(vehicle);
		Map<String, Object> vehicleDetail = Database.selectVehicle(vehicle.getVehicleID());
		assertEquals(70,vehicleDetail.get("RENT"));
	}

	
	@Test
	void testInsertBooking() {
		Database.insertBooking(booking);
		Map<String, Object> bookingDetail = Database.selectBooking(booking.getBookingID());
		assertEquals("05/16/2019",bookingDetail.get("STARTDATE"));
	}

	
	@Test
	void testUpdateBooking() {
		Database.insertBooking(booking);
		Database.updateBooking(booking.getBookingID(), "05/12/2019", "05/16/2019");
		Map<String, Object> bookingDetail = Database.selectBooking(booking.getBookingID());
		assertEquals("05/12/2019",bookingDetail.get("STARTDATE"));
	}

	
	@Test
	void testDeleteBooking() {
		Database.insertBooking(booking);
		Database.deleteBooking(booking);
		Map<String, Object> bookingDetail = Database.selectBooking(booking.getBookingID());
		assertEquals(null,bookingDetail);
	}
	
	
	@Test
	void testSelectBooking() {
		Database.insertBooking(booking);
		Map<String, Object> bookingDetail = Database.selectBooking(booking.getBookingID());
		assertEquals("05/16/2019",bookingDetail.get("STARTDATE"));
	}
	 
}
