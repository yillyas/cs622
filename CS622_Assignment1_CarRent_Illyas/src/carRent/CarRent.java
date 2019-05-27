package carRent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CarRent {
	
	//private LinkedList<Account> accounts = new LinkedList<Account>();
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	//private LinkedList<InsurancePlan> insurancePlans = new LinkedList<InsurancePlan>();
	private LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();
	
	public void mainMenu() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n"); // input scanner object with return key as delimiter.
		System.out.println("Welcome to the Car Rent system");
		while (true) {
			System.out.println("Choose an option:"); 
			System.out.println("   1. Create an Account");
			System.out.println("   2. Add a Vehicle (Existing User Only)");
			System.out.println("   3. List a Vehicle for Rent (Existing User Only)");
			System.out.println("   4. Book a Vehicle (Existing User Only)");
			System.out.println("   5. Show Rentel History (Existing User Only)");
			System.out.println("   6. Exit");
			// add vehicle/ list vehicle
			int selection;
			if(input.hasNextInt()){ // check integer is entered
				selection = input.nextInt();
			}
			else {
				System.out.println("Number not entered, try again.");
				input.next();//bad input must be discarded
				continue;
			}
			if( selection < 1 || selection > 6){ //check valid selection
				System.out.println("Please select a number between 1 and 5, inclusive.");
				continue;
			}
			if ( selection == 1 ){ 
				createAccount(); // create an new account
			} else if (selection == 2 ) { // list a vehicle for rent
				addVehicleToAccount();
			} else if (selection == 3) { // List a vehicle
				listForRent();
			}else if (selection == 4) { // Book a vehicle
				rentVehicle(); 
			}else if (selection == 5) { // Account Rent History
				rentHistory();
			}
			else if (selection == 6) {
				System.exit(0);
			}
			//input.close();
		}
		
	}
	
	public void listForRent() { 
		System.out.println("An Owner Account ID is needed to List a vehicle for rent.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account account = accounts.get(id);
			if (account != null) {
				if (account instanceof Owner ) {
					System.out.println("You have following vehicles that are not listed yet, select the vehicle ID that you want to list.");
					LinkedList<Vehicle> vehicles = ((Owner)account).getVehicle(); 
					for (Vehicle v: vehicles) { // only print the vehicles that are currently not listed
						if (!v.isListed()) {
							System.out.println("vehicle ID: " + v.getVehicleID() + " " + v);
						}
					}
					System.out.println("Entre a vehicle ID: "); 
					int vehicleID = input.nextInt();
					System.out.println("For How may days vehicle should be listed: (e.g: 3,4) ");
					int days = input.nextInt();
					Vehicle vehicle = ((Owner)account).getVehicleByID(vehicleID); // Null pointer exception if vehicle ID does not exist
					((Owner)account).listVehicleforRent(vehicle, days);
					done = true;
				}
			}
			
		}	
	}
	
	public void createAccount() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Please enter the full name (saperted by space):");
			String name = input.next();
			System.out.println("Account Type: 1. Renter 2. Owner");
			int accountType = input.nextInt();
			System.out.println("Entre State:");
			String state = input.next();
			System.out.println("Entre City:");
			String city = input.next();
			System.out.println("Zip Code:");
			int zipCode = input.nextInt();
			if (accountType == 1) {
				Account account = new Renter(name, state, city, zipCode);
				accounts.put(((Renter)account).getId(), account);
				System.out.println("An Account has been added.");
				System.out.println(account);
				done = true;
			} else if (accountType == 2) {
				Account account = new Owner(name, state, city, zipCode);
				accounts.put(((Owner)account).getId(), account);
				System.out.println("An Account has been added.");
				System.out.println(account);
				done = true;
			} else {
				System.out.println("Something went wrong, Try again.");
				//continue;
			}	
		}
	}
	
	public void addVehicleToAccount() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		System.out.println("Owner Account Name (saperted by space):"); // may have introduced bug by moving this out of while.
		String ownerName = input.next();
		//System.out.println("Owner Account ID:");
		
		while (!done) {
			// Vehicle vehicle1 = new Vehicle("John Doe","BMW", 2015, "328i", plan3, 70, 20814);
			//((Owner)account).listVehicleforRent(vehicle1, 7); 
			//System.out.println("Owner Account Name (saperted by space):");
			//String ownerName = input.next();
			System.out.println("Owner Account ID:");
			int ownerID = input.nextInt();
			System.out.println("Vehicle Make:");
			String make = input.next();
			System.out.println("Vehicle Model:");
			String model = input.next();
			System.out.println("Vehicle Year:");
			int year = input.nextInt();
			System.out.println("Vehicle Rent:");
			int rent = input.nextInt();
			System.out.println("Vehicle ZipCode:");
			int zipCode = input.nextInt();
			System.out.println("Select Insurance Plan: 1. Basic Plan 2. Standard Plan 3. Premium Plan");
			int insurancePlanType = input.nextInt();
			if (insurancePlanType == 1) {
				InsurancePlan insuranceplan = new BasicPlan();
				//insurancePlans.add(insuranceplan);
				Vehicle v = new Vehicle(ownerName, make, year, model, insuranceplan, rent, zipCode);
				Account account = accounts.get(ownerID);
				if (account instanceof Owner) {
						//System.out.println(account);
						((Owner)account).addVehicle(v);
						System.out.println("A Vehicle has been added to your account.");
						System.out.println(account);
						done = true;
				} else {
						System.out.println("You need to have an Owner Account type to add the vehicle.");
				}
			} 
			else if (insurancePlanType == 2) {
				InsurancePlan insuranceplan = new StandardPlan();
				//insurancePlans.add(plan);
				Vehicle v = new Vehicle(ownerName, make, year, model, insuranceplan, rent, zipCode);
				Account account = accounts.get(ownerID);
				if (account instanceof Owner) {
						//System.out.println(account);
						((Owner)account).addVehicle(v);
						System.out.println("A Vehicle has been added to your account.");
						System.out.println(account);
						done = true;
				} else {
						System.out.println("You need to have an Owner Account type to add the vehicle.");
				}
			}
			else if (insurancePlanType == 3) {
				InsurancePlan insuranceplan = new PremiumPlan();
				//insurancePlans.add(plan);
				Vehicle v = new Vehicle(ownerName, make, year, model, insuranceplan, rent, zipCode);
				Account account = accounts.get(ownerID);
				if (account instanceof Owner) {
						//System.out.println(account);
						((Owner)account).addVehicle(v);
						System.out.println("A Vehicle has been added to your account.");
						System.out.println(account);
						done = true;
				} else {
						System.out.println("You need to have an Owner Account type to add the vehicle.");
				}
			} 
			else {
				System.out.println("Something went wrong, Try again.");
			}
		}	
	}

	public void rentVehicle() {
		// Account.bookVehicle(vehicle2, ((Renter)account1).getId());
		System.out.println("An Account ID is needed to book a vehicle.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account account = accounts.get(id);
			if (account != null) {
					//Account.bookVehicle(vehicle2, ((Renter)account1).getId()); // need a list of vehicles to choose from
				System.out.println("Enter the zipCode to Search vehicle:");
				int zipCode = input.nextInt();
				LinkedList<Vehicle> vehiclesByZip = Account.searchVehicle(zipCode); // get all vehicle in the user selected zip code
				if (vehiclesByZip != null) {
					for (Vehicle v: vehiclesByZip) {
						System.out.println("vehicle ID = " + v.getVehicleID() + ":" + v);
					}
					System.out.println("Choose a vehicle ID to Book: ");
					int vehicleID = input.nextInt();
					Vehicle selectedVehicle = null;
					for (Vehicle v: vehiclesByZip) { // search for user entered vehicleID in the list
						if (v.getVehicleID() == vehicleID) { 
							selectedVehicle = v;
							Account.bookVehicle(selectedVehicle, account); // changed parameter from account ID to Account
							System.out.println("You Have booked the vehicle: " + selectedVehicle);
							done = true;
						}
					} if (selectedVehicle == null) {
						System.out.println("Your entered vehicle ID is not in the list. Try Again.");
					}	
				} else {
					System.out.println("No listed vehicles for zipCode: " + zipCode + ". Try Again");
					done = true;
				}
			}
			else {
				System.out.println("Account with ID " + id + "does not exist.");
			}
		}
	}
	
	public void rentHistory() {
		// Account.bookVehicle(vehicle2, ((Renter)account1).getId());
		System.out.println("An Account ID is needed to see the rent history.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account account = accounts.get(id);
			LinkedList<Vehicle> vehicleHistory = account.getUserRentHistory();
			if (account != null && vehicleHistory != null) {
				if (account instanceof Owner ) {
					for (Vehicle v: vehicleHistory) {
						if (((Owner)account).getVehicleByID(v.getVehicleID()).getVehicleID() == v.getVehicleID()) {
							System.out.println("============= Vehicles Rented by this account ============");
							System.out.println(v);
						} else {
							System.out.println("============= Vehicles Booked by this account ============");
							System.out.println(v);
							}	
						}
						done = true;
					} 
				else if (account instanceof Renter ) { 
					for (Vehicle v: vehicleHistory) {
						System.out.println("============= Vehicles Booked by this account ============");
						System.out.println(v);
						}
					done = true;
				}
			}
		}	
	}
	
	public static void main(String[] args) {
		CarRent carRent = new CarRent();
		carRent.mainMenu();
	}

}
