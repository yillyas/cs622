package carRent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CarRent {
	
	//private LinkedList<Account> accounts = new LinkedList<Account>();
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	//private LinkedList<InsurancePlan> insurancePlans = new LinkedList<InsurancePlan>();
	//private LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();
	final static String USERINFO = "userinfo.txt";
	final static String OWNERINFO = "ownerinfo.txt";
	final static String RENTERINFO = "renterinfo.txt";
	final static String VEHICLEINFO = "vehicleinfo.txt";
	
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
			if( selection < 1 || selection > 7){ //check valid selection
				System.out.println("Please select a number between 1 and 5, inclusive.");
				continue;
			}
			if ( selection == 1 ){ 
				createAccountFromInputFile(); // create an new account
			} else if (selection == 2 ) { // list a vehicle for rent
				addVehicleToAccountFromInputFile();
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
			//input.close();
		}
		
	}
	
	public double getAccountBalance() {
		double balance = 0;
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		System.out.println("Enter an Account ID");
		int id = input.nextInt();
		Account account = accounts.get(id);
		if (account != null){
			if (account instanceof Owner) {
				balance = ((Owner)account).getBalance();
				System.out.println("Your current balance is: " + balance);
				//return balance;
			}
			else if (account instanceof Renter) {
				balance = ((Renter)account).getBalance();
				System.out.println("Your current balance is: " + balance);
			}
		} 
		else {
			System.out.println("The id: " + id + " doesnot exist.");
		}
		return balance;
	}
	
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
					Vehicle vehicle = ((Owner)account).getVehicleByID(vehicleID); // Null pointer exception if vehicle ID does not exist
					((Owner)account).listVehicleforRent(vehicle, days);
					System.out.println(vehicle);
					done = true;
				}
			}
			
		}
		//input.close();
	}
	
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
	
	public void addVehicleToAccount() {
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		//System.out.println("Owner Account Name (saperted by space):"); // may have introduced bug by moving this out of while.
		//String ownerName = input.next();
		
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
				//Vehicle v = new Vehicle(ownerName, make, year, model, insuranceplan, rent, zipCode);
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
				//insurancePlans.add(plan);
				//Vehicle v = new Vehicle(ownerName, make, year, model, insuranceplan, rent, zipCode);
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
		// Account.bookVehicle(vehicle2, ((Renter)account1).getId());
		System.out.println("An Account ID is needed to book a vehicle.");
		Scanner input = new Scanner(System.in).useDelimiter("\\n");
		boolean done = false;
		while (!done) {
			System.out.println("Entre a valid Account ID: ");
			int id = input.nextInt();
			Account renter = accounts.get(id);
			if (renter != null) {
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
							Account owner = selectedVehicle.getOwner();
							System.out.println("Booking Start Date: (mm/dd/yyyy)");
							String startDate = input.next();
							System.out.println("Booking End Date: (mm/dd/yyyy)");
							String endDate = input.next();
							try {
								Account.newbooking(selectedVehicle, owner, renter, startDate, endDate);
								//System.out.println("You Have booked the vehicle: " + selectedVehicle);
								done = true;
							} catch (IncorrectAccountException e) {
								e.printStackTrace();
							}
							//Account.bookVehicle(selectedVehicle, renter); // changed parameter from account ID to Account
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
