package carRent;

import java.util.LinkedList;

public abstract class Account {
	protected static int accountID = 0;
	protected LinkedList<Vehicle> vehiclesRented;
	
	public static LinkedList<Vehicle> searchVehicle (int zipCode) {
		return VehicleRepository.vehicleByZip.get(zipCode);
	}
	
	public static void bookVehicle (Vehicle vehicle, Account account) { // changed the parameter from id to Account
		int id;
		if (!(vehicle.isBooked())) {
			vehicle.setBooked(true);
			LinkedList<Vehicle> bookingDetail = new LinkedList<Vehicle>();
			bookingDetail.add(vehicle);
			int zip = vehicle.getZipCode();
			LinkedList<Vehicle> updatedVehicleList = VehicleRepository.vehicleByZip.get(zip);
			updatedVehicleList.remove(vehicle); // remove from the list
			VehicleRepository.vehicleByZip.put(zip, updatedVehicleList); //update the old list in the map
			vehicle.setRenters(account); // update vehicle rent history
				if (account instanceof Owner) {
					id = ((Owner)account).getId();
					VehicleRepository.bookingDetail.put(id, bookingDetail);
					((Owner)account).updateUserRentHistory(vehicle); // update user rent history
				} else {
					id = ((Renter)account).getId();
					VehicleRepository.bookingDetail.put(id, bookingDetail);
					((Renter)account).updateUserRentHistory(vehicle); // update user rent history
				}
			}
	 }
	
	 public void getBookingDetail(int id) { // get the details of the vehicle booked from the HasMap
		LinkedList<Vehicle> vehiclesBooked = VehicleRepository.bookingDetail.get(id);
		for (Vehicle v : vehiclesBooked) {
		    		System.out.println(v);
		}
	 }
	
	 public abstract LinkedList<Vehicle> getUserRentHistory() ;
}
