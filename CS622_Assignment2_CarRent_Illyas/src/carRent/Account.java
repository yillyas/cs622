package carRent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public abstract class Account {
	protected static int accountID = 0;
	//protected LinkedList<Vehicle> vehiclesRented;
	private static String BOOKINGDETAILS = "bookingdetail.txt";
	
	public static LinkedList<Vehicle> searchVehicle (int zipCode) {
		return VehicleRepository.vehicleByZip.get(zipCode);
	}
	
	
	public static void newbooking (Vehicle vehicle, Account owner, Account renter, String startDate, String endDate) throws IncorrectAccountException {
		if ((owner instanceof Owner) && (renter instanceof Renter)) {
			Booking booking = Booking.book(vehicle, owner, renter, startDate, endDate);
			int zip = vehicle.getZipCode();
			LinkedList<Vehicle> updateVehicleList = VehicleRepository.vehicleByZip.get(zip); // vehicles currently listed in this zip code
			updateVehicleList.remove(vehicle); // remove the booked vehicle from the list
			VehicleRepository.vehicleByZip.put(zip, updateVehicleList); //update the old list in the map
			//VehicleRepository.bookingDetail.put(id, bookingDetail);  // write the booking detail to file
			String bookingDetail = " [  BookingID: " + booking.getBookingID()  + ", Owner: " + ((Owner)owner).getName() + ", Renter: " + ((Renter)renter).getName() +  
					", noOfDays: " + booking.getNoDays() + ", InsurancePlan: " + booking.getVehicle().getInsurancePlan()
					+ ", StartDate: " + booking.getStartDate()  + ", EndDate: " + booking.getEndDate() + " ]"; 
			//System.out.println(bookingDetail);
			Booking.calculateCost(booking);
			appendToBookingDetail(bookingDetail); // write to file
			((Owner)owner).updateUserRentHistory(vehicle); // update user rent history
			((Renter)renter).updateUserRentHistory(vehicle);
			
		} else {
			throw new IncorrectAccountException("Incorrent account type");
		}
		
	}	
	
	public static void appendToBookingDetail(String newBooking) {
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("./" + BOOKINGDETAILS, true)); // (append flag)
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
}
