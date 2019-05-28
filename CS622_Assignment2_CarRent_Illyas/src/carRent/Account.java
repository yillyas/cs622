package carRent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public abstract class Account {
	protected static int accountID = 0;
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
	 * 
	 */
	
	public static void newbooking (Vehicle vehicle, Account owner, Account renter, 
										String startDate, String endDate) throws IncorrectAccountException {
		if ((owner instanceof Owner) && (renter instanceof Renter)) {
			Booking booking = Booking.book(vehicle, owner, renter, startDate, endDate);
			int zip = vehicle.getZipCode();
			LinkedList<Vehicle> updateVehicleList = VehicleRepository.vehicleByZip.get(zip);  // vehicles currently listed in this zip code
			updateVehicleList.remove(vehicle); 												// remove the booked vehicle from the list
			VehicleRepository.vehicleByZip.put(zip, updateVehicleList); 						//update the old list in the map
			String bookingDetail = " [  BookingID: " + booking.getBookingID()  + ", Owner: " + ((Owner)owner).getName() + ", Renter: " + ((Renter)renter).getName() +  
					", noOfDays: " + booking.getNoDays() + ", InsurancePlan: " + booking.getVehicle().getInsurancePlan()
					+ ", StartDate: " + booking.getStartDate()  + ", EndDate: " + booking.getEndDate() + " ]"; 
			Booking.calculateCost(booking);        			// calculate charges
			appendToBookingDetail(bookingDetail); 			//  write to file
			((Owner)owner).updateUserRentHistory(vehicle); 	// update user rent history
			((Renter)renter).updateUserRentHistory(vehicle);
			
		} else {
			throw new IncorrectAccountException("Incorrent account type");
		}
		
	}
	
	/**
	 * 
	 * @param newBooking
	 * 
	 *  Helper method to write the booking details to a file.
	 */
	
	private static void appendToBookingDetail(String newBooking) {
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(BOOKINGDETAILS, true)); // (append flag)
				bw.write(newBooking);  // C3
				System.out.println(newBooking + "<--written to bookingdetail");  
				bw.newLine(); // C4
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
	
	
	public abstract LinkedList<Vehicle> getUserRentHistory();
	public abstract void addRentalChangers(int bookingID, Double charges);
	public abstract double getBalance();
}
