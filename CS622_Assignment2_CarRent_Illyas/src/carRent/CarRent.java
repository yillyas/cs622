package carRent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CarRent {
	
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>(); // To track the list of accounts being used in the current session.
	final static String USERINFO    = "userinfo.txt";
	final static String OWNERINFO   = "ownerinfo.txt";
	final static String RENTERINFO  = "renterinfo.txt";
	final static String VEHICLEINFO = "vehicleinfo.txt";
	
	/*
	 * Main menu for text user interface
	 * Lets the user select different options and invokes the appropriate methods. 
	 */
	
	public void mainMenu() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n"); // input scanner object with return key as delimiter.
		System.out.println("Welcome to the Car Rent system Assignment 2");
		while (true) {
			System.out.println("Choose an option:"); 
			System.out.println("   1. Create an Account");
			System.out.println("   2. Add a Vehicle (Existing User Only)");
			System.out.println("   3. List a Vehicle for Rent (Existing User Only)");
			System.out.println("   4. Book a Vehicle (Existing User Only)");
			System.out.println("   5. Show Rentel History (Existing User Only)");
			System.out.println("   6. Show Account Balance");
			System.out.println("   7. Exit");
			int selection;
			if(input.hasNextInt()){ // check integer is entered
				selection = input.nextInt();
			}
			else {
				System.out.println("Number not entered, try again.");
				input.next();//bad input must be discarded
				continue;
			}
			if( selection < 1 || selection > 7){ //check valid selection
				System.out.println("Please select a number between 1 and 5, inclusive.");
				continue;
			}
			if ( selection == 1 ){ 
				selectAccountCreationMethod(); // Add manually or load from file
			} else if (selection == 2 ) { // list a vehicle for rent
				selectVehicleAdditionMethod(); 
			} else if (selection == 3) { // List a vehicle
				listForRent();
			}else if (selection == 4) { // Book a vehicle
				rentVehicle(); 
			}else if (selection == 5) { // Account Rent History
				rentHistory();
			}else if (selection == 6) { // Get Account Balance
				getAccountBalance();
			}
			else if (selection == 7) {
				System.exit(0);
			}
		}
		
	}
	
	/*
	 * Sub menu for presenting options to user on account creation
	 * User can choose either of the following option:
	 * 	1. Automatically Load From File 
	 * 	2. Enter the details manually
	 * Based on user selection invokes createAccountFromInputFile() or createAccountManually()
	 */
	public void selectAccountCreationMethod() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Select Account Create Method: 1. Automatically Load From File 2. Enter the details manually");
			int selection = input.nextInt();
			if ( selection == 1 ){ 
				createAccountFromInputFile();
				done = true;
			}else if ( selection == 2 ){ 
				createAccountManually();
				done = true;
			}else {
				System.out.println("Please enter 1 or 2");
			}
		}
	}
	
	/*
	 * Sub menu for presenting option to user on how to add new vehicles to existing account
	 * User can choose either of the following option:
	 * 	1. Automatically Load From File 
	 * 	2. Enter the details manually
	 * Based on user selection invokes addVehicleToAccountFromInputFile() or addVehicleToAccountManually()
	 */
	public void selectVehicleAdditionMethod() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("How to add Vehicle: 1. Automatically Load From File 2. Enter the details manually");
			int selection = input.nextInt();
			if ( selection == 1 ){ 
				addVehicleToAccountFromInputFile();
				done = true;
			}else if ( selection == 2 ){ 
				addVehicleToAccountManually();
				done = true;
			}else {
				System.out.println("Please enter 1 or 2");
			}
		}
	}
	
	/*
	 * Allow the user to check their current account balance.
	 * A -ve balance indicates that the user owes money.
	 */
	public double getAccountBalance() {
		double balance = 0;
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		System.out.println("Enter an Account ID");
		int id = input.nextInt();
		Account account = accounts.get(id);
		if (account != null){
				balance = account.getBalance(); 
				System.out.println("Your current balance is: " + balance);
			} 
		else {
			System.out.println("The id: " + id + " doesnot exist.");
		}
		return balance;
	}
	
	/*
	 * Reads the text files ownerinfo.txt and ownerinfo.txt to create Owner and Renter accounts respectively.
	 * The method throws FileNotFoundException exception if the above mentioned files are not found.
	 */
	public void createAccountFromInputFile() {
		try {
			FileReader userInfo = new FileReader(OWNERINFO); // create owner account
			Scanner userInfoInput = new Scanner(userInfo);
			while (userInfoInput.hasNextLine()) { // keep iterating until all the lines are read
		        String name = userInfoInput.next();
		        name += " " + userInfoInput.next();
		        String state = userInfoInput.next();
		        String city = userInfoInput.next();
		        String zipCode = userInfoInput.next();
		        Account owner = new Owner(name, state, city, Integer.parseInt(zipCode)); // Create an Owner account
		        System.out.println(owner);
		        accounts.put(((Owner)owner).getId(), owner); // add the owner to the account map
			} userInfoInput.close();		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			FileReader userInfo = new FileReader(RENTERINFO); // create renter account
			Scanner userInfoInput = new Scanner(userInfo);
			while (userInfoInput.hasNextLine()) { // keep iterating until all the lines are read
		        String name = userInfoInput.next();
		        name += " " + userInfoInput.next();
		        String state = userInfoInput.next();
		        String city = userInfoInput.next();
		        String zipCode = userInfoInput.next();
		        Account renter = new Renter(name, state, city, Integer.parseInt(zipCode)); // Create a Renter account
		        System.out.println(renter);
		        accounts.put(((Renter)renter).getId(), renter); // add the renter to the account map
			} userInfoInput.close();		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}	
	
	/*
	 * Allows the user with Valid Owner account ID to list their vehicle for rent.
	 * User makes a selection from a list of vehicles liked to their account that are currently not listed.
	 * User also selects the time period in days for how long they want to list the vehicle.
	 * 
	 */
	public void listForRent() {  
		System.out.println("An Owner Account ID is needed to List a vehicle for rent.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account account = accounts.get(id); // search the account in the account Map
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
					Vehicle vehicle = ((Owner)account).getVehicleByID(vehicleID); 
					if (vehicle != null) {
						((Owner)account).listVehicleforRent(vehicle, days);
						System.out.println(vehicle);
						done = true;
					} 
					else {
						System.out.println("Your entered ID doesnot exist, please try again");
					}
				}
			}
		}
	}
	
	/*
	 * Creates user account by getting the information interactively from the user
	 * User can create following two types of accounts:
	 *  	  1- Owner Account
	 *    2- Renter Account
	 */
	public void createAccountManually() {
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
	
	public void addVehicleToAccountManually() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		
		while (!done) {
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
				Account account = accounts.get(ownerID); // retrieve the account from the map based on account ID
				if (account instanceof Owner) {
						Vehicle v = new Vehicle((Owner)account, make, year, model, insuranceplan, rent, zipCode);
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
				Account account = accounts.get(ownerID);
				if (account instanceof Owner) {
						//System.out.println(account);
						Vehicle v = new Vehicle((Owner)account, make, year, model, insuranceplan, rent, zipCode);
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
				Account account = accounts.get(ownerID);
				if (account instanceof Owner) {
						Vehicle v = new Vehicle((Owner)account, make, year, model, insuranceplan, rent, zipCode);
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

	public Vehicle addVehicleToAccountFromInputFile() { // adds vehicle to owner account by reading the inputs from the file
		Vehicle v = null;
		try {
			FileReader vehicleInfo = new FileReader(VEHICLEINFO); // create owner account
			Scanner vehicleInfoInput = new Scanner(vehicleInfo);
			while (vehicleInfoInput.hasNextLine()) { // keep iterating until all the lines are read
				String id = vehicleInfoInput.next();
				int accountID = Integer.parseInt(id);
				Account account = accounts.get(accountID);
				String make = vehicleInfoInput.next();
				String model = vehicleInfoInput.next();
				String year = vehicleInfoInput.next();
				String rent = vehicleInfoInput.next();
				String zipCode = vehicleInfoInput.next();
				String insurance = vehicleInfoInput.next();
				InsurancePlan insurancePlan;
				if (insurance.equals("Basic")) {  // validate insurance paln info
					insurancePlan = new BasicPlan();
				} else if (insurance.equals("Standard")) {
					insurancePlan = new StandardPlan();
				}else {
					insurancePlan = new PremiumPlan();
				}
				v = new Vehicle((Owner)account, make, Integer.parseInt(year), model,  insurancePlan, Integer.parseInt(rent), Integer.parseInt(zipCode));
				((Owner)account).addVehicle(v);
				System.out.println("Added the Vehicle: " + v.getMake() + " " + v.getModel()
									+ " to owner account: " + ((Owner)account).getName());
			} vehicleInfoInput.close();
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return v;
	}
	
	public void rentVehicle() {
		System.out.println("An Account ID is needed to book a vehicle.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account renter = accounts.get(id);
			if (renter != null) {
				System.out.println("Enter the zipCode to Search vehicle:");
				int zipCode = input.nextInt();
				LinkedList<Vehicle> vehiclesByZip = Account.searchVehicle(zipCode); // get all vehicle in the user selected zip code
				if (vehiclesByZip != null) {
					for (Vehicle v: vehiclesByZip) {  // Display list of available vehicles in the user selected zip code
						System.out.println("vehicle ID = " + v.getVehicleID() + ":" + v);
					}
					System.out.println("Choose a vehicle ID to Book: ");
					int vehicleID = input.nextInt();
					Vehicle selectedVehicle = null;
					for (Vehicle v: vehiclesByZip) { // search for user entered vehicleID in the list
						if (v.getVehicleID() == vehicleID) { 
							selectedVehicle = v;
							Account owner = selectedVehicle.getOwner();
							System.out.println("Booking Start Date: (mm/dd/yyyy)");
							String startDate = input.next();
							System.out.println("Booking End Date: (mm/dd/yyyy)");
							String endDate = input.next();
							try {
								Account.newbooking(selectedVehicle, owner, renter, startDate, endDate);
								System.out.println("You Have booked the vehicle: " + selectedVehicle);
								done = true;
							} catch (IncorrectAccountException e) {
								e.printStackTrace();
							}
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
					System.out.println("============= Vehicles Rented by this account ============");
					for (Vehicle v: vehicleHistory) {
						System.out.println(v);
						}
					done = true;
					}	
				else if (account instanceof Renter ) { 
					System.out.println("============= Vehicles Booked by this account ============");
					for (Vehicle v: vehicleHistory) {
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
