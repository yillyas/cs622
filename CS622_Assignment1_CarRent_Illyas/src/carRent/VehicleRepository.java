package carRent;

import java.util.HashMap;
import java.util.LinkedList;

public class VehicleRepository {
	
	public static LinkedList<Vehicle> allVehicle = new LinkedList<Vehicle>();
	public static HashMap<Integer, LinkedList<Vehicle>> vehicleByZip = new HashMap<Integer, LinkedList<Vehicle>>();
	public static HashMap<String, LinkedList<Vehicle>> bookedVehicle = new HashMap<String, LinkedList<Vehicle>>();
	public static HashMap<Integer, LinkedList<Vehicle>> bookingDetail = new HashMap<Integer, LinkedList<Vehicle>>();

	}
