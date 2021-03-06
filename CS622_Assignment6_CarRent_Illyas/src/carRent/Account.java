package carRent;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public abstract class Account implements Comparable<Account>, Serializable {
	protected static int accountID = 0;
	protected LinkedList<Vehicle> vehiclesRented;
	private final static String BOOKINGDETAILS = "bookingdetail.txt";
	private final static String BOOKINGOBJECTS = "bookingObjectFile.dat";
	
	public static LinkedList<Vehicle> searchVehicle (int zipCode) {
		return VehicleRepository.vehicleByZip.get(zipCode);
	}
	public LinkedList<Vehicle> getVehiclesRented() {
		return vehiclesRented;
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
	
	public static Booking newbooking (Vehicle vehicle, Account owner, Account renter, 
								String startDate, String endDate) throws IncorrectAccountException {
		Booking booking;
		if ((owner instanceof Owner) && (renter instanceof Renter)) {
			booking = Booking.book(vehicle, owner, renter, startDate, endDate);
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
			owner.storeBookingInformation(booking);          // Save booking object
			
			vehicle.addBookingHistory(booking.getStartDate(), booking);            // update vehicle booking history
			((Owner)owner).addToUserRentHistory(vehicle);	// update user rent history
			((Renter)renter).addToUserRentHistory(vehicle);
			vehicle.setBooked(true);
			
		} else {
			throw new IncorrectAccountException("Incorrent account type");
		}
		return booking;
		
	}
	
	/** 
	 * @param booking
	 * @return renterBalance
	 * 
	 * Cancels an exiting booking and updates the account balance
	 */
	
	public static double cancelBooking(Booking booking) {
		booking.getOwner().removeRentalChangers(booking.getBookingID());
		System.out.println("Updated the Owner Account balance.");
		booking.getRenter().removeRentalChangers(booking.getBookingID());
		System.out.println("Updated the Renter Account balance.");
		booking.getOwner().removeFromUserRentHistory(booking.getVehicle());
		booking.getRenter().removeFromUserRentHistory(booking.getVehicle());
		booking.getVehicle().setBooked(false);
		return booking.getRenter().getBalance();  // return the updated renter balance
	}
	
	/**
	 * @param booking
	 * Store Booking object in a binary file
	 */
	public int storeBookingInformation (Booking booking) {
		try {
			try (ObjectOutputStream outfile = new ObjectOutputStream(new FileOutputStream(BOOKINGOBJECTS));) {
				outfile.writeObject(booking);
			}
		}
		catch (IOException ex)
	     {
	         System.out.println("IOException");
	         ex.printStackTrace();    
	     }
		return booking.getBookingID();
	}
	
	/**
	 * @param BookingID
	 * Get the Booking information from a binary file
	 */
	public static Booking showBookingInformation(int bookingID) {
		Booking bookingDetail = null;
		try {
			HashMap<Integer, Booking> bookingInfo = new HashMap<>();
			try (ObjectInputStream infile = new ObjectInputStream(new FileInputStream(BOOKINGOBJECTS));) {
				while (true) {
					Booking b = (Booking) (infile.readObject());
					int id = b.getBookingID();
					bookingInfo.put(id, b);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (EOFException ex) {
				bookingDetail = bookingInfo.get(bookingID);
				if (bookingDetail != null) {
					System.out.println(bookingDetail);
				}else {
					System.out.println(bookingInfo);
					System.out.println("Booking ID: " + bookingID + " not found.");
				}
		     }
		}
		catch (IOException ex)
	     {
	         System.out.println("IOException");
	         ex.printStackTrace();    
	     }
		return bookingDetail;
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
	
 	/**
 	 * @param aBookingID
 	 * @param a Amount
 	 * Create a new Claim, updates the account balance.
 	 */
	
	public Claim<Account, Booking> fileClaim(int aBookingID, double aAmount) {
		Booking booking = VehicleRepository.trackBooking.get(aBookingID); // update to get from the file ??
		Claim<Account, Booking> claim = new Claim<>(this, booking);
		claim.updateBalance(aAmount); // adjust the account balance
		return claim;
	}
	
	
	public LinkedList<Vehicle> vehiclesRentedInPastMonth() {
		LinkedList<Vehicle> pastMonthRentals = new LinkedList<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate pastMonthDate = currentDate.minusMonths(1);
		if (vehiclesRented != null) {
			pastMonthRentals = new LinkedList<>();
			for (Vehicle v : vehiclesRented) {                  // only has start dates ??? 06/07/2019
				Set<String> vehicleBookDates = v.getBookingHistory().keySet(); // all the dates that vehicle was booked on
				for (String d : vehicleBookDates) {
					LocalDate date = LocalDate.parse(d,DateTimeFormatter.ofPattern("MM/dd/yyyy")); // convert string to date
					if (date.isAfter(pastMonthDate) && date.isBefore(currentDate)) { // verify the booking date is between past 30 days
						pastMonthRentals.add(v);
					}
				}
			}
			
		}
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
	public abstract double addVoucher(double voucher);
}
