package carRentTest;

import java.time.LocalDate;

import carRent.Account;
import carRent.BasicPlan;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.PremiumPlan;
import carRent.Renter;
import carRent.StandardPlan;
import carRent.Vehicle;

public class carRentTest {

public static void main(String[] args) {	
		
		InsurancePlan plan1 = new BasicPlan();
		InsurancePlan plan2 = new PremiumPlan();
		InsurancePlan plan3 = new StandardPlan();
		
		//LocalDate currentDate = LocalDate.now();
		
		Account account = new Owner("John Doe", "Maryland", "Bethesda", 20814);
		Account account1 = new Renter("Jane Doe", "Maryland", "Rockville", 20852);
		//System.out.println(account);
		Vehicle vehicle1 = new Vehicle("John Doe","BMW", 2015, "328i", plan3, 70, 20814);
		Vehicle vehicle2 = new Vehicle("John Doe","Toyota", 2018, "RAV4", plan2, 50, 20815);
		Vehicle vehicle3 = new Vehicle("John Doe","Honda", 2017, "CRV", plan1, 50, 20815);
		
		if (account instanceof Owner) {
			if (account != null) {
				//System.out.println(account);
				((Owner)account).addVehicle(vehicle1);
				((Owner)account).addVehicle(vehicle2);
			}
		}
		
		
		((Owner)account).listVehicleforRent(vehicle1, 7); 
		((Owner)account).listVehicleforRent(vehicle2, 5);
		((Owner)account).listVehicleforRent(vehicle3, 4);
		
		//System.out.println("Vehicles listed by account(John)");
		
		
		System.out.println("Search available Vehicle in 20814");
		System.out.println(Account.searchVehicle(20814));
		
		System.out.println("Book Vehicle");
		Account.bookVehicle(vehicle1, ((Renter)account1)); 
		
		System.out.println("Search available Vehicle in 20815");
		System.out.println(Account.searchVehicle(20815));
		
		
		System.out.println("Vehicles Booked By account1");
		account.getBookingDetail(((Renter)account1).getId());

	}

}

