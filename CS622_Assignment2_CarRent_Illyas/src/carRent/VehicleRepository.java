package carRent;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Used to keep track of different objects during the application execution.
 * Will be replaced by database in future.
 * @author yasirilyas
 */
public class VehicleRepository {
	
	public static LinkedList<Vehicle> allVehicle = new LinkedList<Vehicle>();
	public static HashMap<Integer, LinkedList<Vehicle>> vehicleByZip = new HashMap<Integer, LinkedList<Vehicle>>();
	public static HashMap<String, LinkedList<Vehicle>> bookedVehicle = new HashMap<String, LinkedList<Vehicle>>();
	}
