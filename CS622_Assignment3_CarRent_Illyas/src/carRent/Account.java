package carRent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public abstract class Account implements Comparable<Account> {
	protected static int accountID = 0;
	protected LinkedList<Vehicle> vehiclesRented;
	private static String BOOKINGDETAILS = "bookingdetail.txt";
	
	public static LinkedList<Vehicle> searchVehicle (int zipCode) {
		return VehicleRepository.vehicleByZip.get(zipCode);
	}
	/**
	 * @param vehicle
	 * @param owner
	 * @param renter
	 * @param startDate
	 * @param endDate
	 * @throws IncorrectAccountException
	 * 
	 * 1- Validates the inputs before invoking Booking.book method to book a vehicle.
	 * 2- Update the vehicle repository by removing the booked vehicle from the list of available vehicles.
	 * 3- Invokes Booking.calculateCost to calculate the charges and add the to Owner and Renter accounts.
	 * 4- Write the booking details to a file "bookingdetail.txt".
	 * 5- Updates Owner and Renters rent history.
	 */
	
	public static void newbooking (Vehicle vehicle, Account owner, Account renter, 
								String startDate, String endDate) throws IncorrectAccountException {
		if ((owner instanceof Owner) && (renter instanceof Renter)) {
			Booking booking = Booking.book(vehicle, owner, renter, startDate, endDate);
			int zip = vehicle.getZipCode();
			LinkedList<Vehicle> updateVehicleList = VehicleRepository.vehicleByZip.get(zip);  // vehicles currently listed in this zipcode
			updateVehicleList.remove(vehicle); 			// remove the booked vehicle from the list
			VehicleRepository.vehicleByZip.put(zip, updateVehicleList); 				//update the old list in the map
			VehicleRepository.trackBooking.put(booking.getBookingID(), booking); // add to booking tracking 
			String bookingDetail = " [  BookingID: " + booking.getBookingID()  
										+ ", Owner: " + ((Owner)owner).getName() 
										+ ", Renter: " + ((Renter)renter).getName()  
										+ ", noOfDays: " + booking.getNoDays() 
										+ ", InsurancePlan: " + booking.getVehicle().getInsurancePlan()
										+ ", StartDate: " + booking.getStartDate()  
										+ ", EndDate: " + booking.getEndDate() + " ]"; 
			HashMap<String,Double> tripRent = booking.calculateCost();        	// calculate charges for both owner and renter
			booking.getOwner().addRentalChangers(booking.getBookingID(), tripRent.get("ownerTripBalance"));
			System.out.println("Updated the Owner Account balance.");
			booking.getRenter().addRentalChangers(booking.getBookingID(), tripRent.get("renterTripBalance"));
			System.out.println("Updated the Renter Account balance.");
			appendToBookingDetail(bookingDetail); 			//  write booking details to file
			vehicle.addBookingHistory(booking.getStartDate(), booking);            // update vehicle booking history
			((Owner)owner).updateUserRentHistory(vehicle); 	// update user rent history
			((Renter)renter).updateUserRentHistory(vehicle);
			
		} else {
			throw new IncorrectAccountException("Incorrent account type");
		}
		
	}
	
	/**
	 * @param newBooking
	 *  Helper method to write the booking details to a file.
	 */
	public static void appendToBookingDetail(String newBooking) {
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(BOOKINGDETAILS, true)); // (true = append flag)
				bw.write(newBooking); 
				System.out.println(newBooking + "<--written to bookingdetail");  
				bw.newLine(); 
				bw.flush();
			} 
			catch (IOException e) {
				System.out.println("Could not append to bookingdetail.txt");
				e.printStackTrace();
			} 
			finally { // (close file)
				if (bw != null) 
					try {
						bw.close();
					} 
				catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
	
	
	public Claim<Account, Booking> fileClaim(int aBookingID, double aAmount) {
		Booking booking = VehicleRepository.trackBooking.get(aBookingID);
		Claim<Account, Booking> claim = new Claim<>(this, booking);
		claim.updateBalance(aAmount); // adjust the account balance
		return claim;
	}
	
	
	public LinkedList<Vehicle> vehiclesRentedInPastMonth() {
		LinkedList<Vehicle> pastMonthRentals = null;
		int noOfVehicle = 0;
		LocalDate currentDate = LocalDate.now();
		LocalDate pastMonthDate = currentDate.minusMonths(1);
		if (vehiclesRented != null) {
			pastMonthRentals = new LinkedList<>();
			for (Vehicle v : vehiclesRented) {
				Set<String> vehicleBookDates = v.getBookingHistory().keySet(); // all the dates that vehicle was booked on
				System.out.println("vehicleBookDates.size() :" + vehicleBookDates.size());
				for (String d : vehicleBookDates) {
					LocalDate date = LocalDate.parse(d,DateTimeFormatter.ofPattern("MM/dd/yyyy")); // convert string to date
					if (date.isAfter(pastMonthDate) && date.isBefore(currentDate)) { // verify the booking date is between past 30 days
						pastMonthRentals.add(v);
						noOfVehicle ++;
						System.out.println("Adding to past month rentals");
					}
				}
			}
			
		}
		System.out.println("vehiclesRentedInPastMonth() return:" + pastMonthRentals.size());
		System.out.println("noOfVehicle" + noOfVehicle);
		return pastMonthRentals;
	}
	
	@Override
	public int compareTo(Account o) {
		int current = this.vehiclesRentedInPastMonth().size();
		int other = o.vehiclesRentedInPastMonth().size();
		return Integer.compare(current,other);
	}
	
	public abstract LinkedList<Vehicle> getUserRentHistory();
	public abstract void addRentalChangers(int bookingID, Double charges);
	public abstract double getBalance();
	public abstract double addBalanceFromCoupon(double voucher);
}
