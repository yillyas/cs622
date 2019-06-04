package carRentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carRent.BasicPlan;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

class CarRentTest {
	private Owner owner;
	private Renter renter;
	private InsurancePlan insurance;
	private Vehicle vehicle;
	
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

}
