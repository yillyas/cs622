package Model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import carRent.Account;
import carRent.Booking;
import carRent.Owner;
import carRent.Renter;
import carRent.Vehicle;

public class Database {
	private final static String ACCOUNTTABLE = "accounts";
	private final static String VEHICLETABLE = "vehicles";
	private final static String BOOKINGTABLE = "bookings";
	private final static String accountDDL = "CREATE TABLE accounts (id int PRIMARY KEY, balance DECIMAL(5, 2), "
																	+ "name varchar(200), state varchar(100), city varchar(100), zipcode int)";
	
	private final static String vehicleDDL = "CREATE TABLE vehicles (vehicleID int PRIMARY KEY, ownerID int, "
													+ "insurancePlanID int, make varchar(200), model varchar(200), vehicleYear int, rent int, "
													+ "lstngStartDate varchar(200), lstngEndDate varchar(200), bkngStartDate varchar(200), "
													+ "bkngEndDate varchar(200), isBooked BOOLEAN, isListed BOOLEAN, zipcode int)";
	
	private final static String bookingDDL = "CREATE TABLE bookings (bookingID int PRIMARY KEY, vehicleID int, "
																+ "ownerID int, renterID int, "
																+ "startDate varchar(200), endDate varchar(200), noDays int)";
	
	public static void insertAccount(Account account)
    {
		int id;
		double balance;
		String name; 	 
		String state; 	 
		String city; 	  	 
		int zipcode;
		
		if (account instanceof Owner) {
			id 		= ((Owner)account).getId();
			balance = ((Owner)account).getBalance();
			name 	= ((Owner)account).getName();
			state  	= ((Owner)account).getState();
			city 	= ((Owner)account).getCity();
			zipcode = ((Owner)account).getZipCode();
		} else {
			id 		= ((Renter)account).getId();
			balance = ((Renter)account).getBalance();
			name 	= ((Renter)account).getName();
			state  	= ((Renter)account).getState();
			city 	= ((Renter)account).getCity();
			zipcode = ((Renter)account).getZipCode();
		}
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "INSERT INTO accounts VALUES (" + id + "," + balance + ",'" + name + "','" + state + "','" + city + "'," + zipcode + ")";
        stmt.execute(query);
        System.out.println("Inserted account into database, AccountID: " + id);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
	
	public static void insertVehicle(Vehicle vehicle)
    {
		int id = vehicle.getVehicleID();
		int ownerID = vehicle.getOwner().getId();
		int insurancePlanID = vehicle.getInsurancePlan().getPolicyNumber();
		String make = vehicle.getMake();	 
		String model = vehicle.getModel();
		int vehicleYear = vehicle.getYear();
		int rent = vehicle.getRent();
		String lstngStartDate = vehicle.getStartDate();
		String lstngEndDate = vehicle.getEndDate();
		String bkngStartDate = vehicle.getBkngStartDate();
		String bkngEndDate = vehicle.getBkngEndDate();
		boolean isBooked = vehicle.isBooked();
		boolean isListed = vehicle.isListed();
		int zipcode = vehicle.getZipCode();
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "INSERT INTO "  + VEHICLETABLE + "(vehicleID, ownerID, insurancePlanID, make, model, vehicleYear, rent, "
        												+ "lstngStartDate, lstngEndDate, bkngStartDate, bkngEndDate, "
        												+ "isBooked, isListed, zipcode) VALUES"
													+ "( " + id + "," + ownerID + "," + insurancePlanID + ",'" + make + "','"+ model + "',"+ vehicleYear + "," + rent + ",'"
													+ lstngStartDate + "','" + lstngEndDate + "','" + bkngStartDate + "','" + bkngEndDate + "',"
													+ isBooked + "," + isListed + "," + zipcode + ")";
        stmt.execute(query);
        System.out.println("Inserted Vehicle into database, vehicleID: " + id);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

	public static void insertBooking(Booking booking) {
		int bookingID = booking.getBookingID();
		int vehicleID = booking.getVehicle().getVehicleID();
		int ownerID =  booking.getOwner().getId();
		int renterID =  booking.getRenter().getId();
		int noDays = booking.getNoDays();
		String startDate = booking.getStartDate();
		String endDate = booking.getEndDate();
		
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "INSERT INTO "  + BOOKINGTABLE + "(bookingID, vehicleID, ownerID, renterID, startDate, endDate, noDays) VALUES"
													+ "(" + bookingID + "," + vehicleID + "," + ownerID + "," + renterID + ",'" + startDate + "','" + endDate + "',"+ noDays + ")";
            stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		
	}

	public static void updateAccount(Account account)
    {
		int id;
		String name; 	 
		String state; 	 
		String city; 	  	 
		int zipcode;
		
		if (account instanceof Owner) {
			id 		= ((Owner)account).getId();
			name 	= ((Owner)account).getName();
			state  	= ((Owner)account).getState();
			city 	= ((Owner)account).getCity();
			zipcode = ((Owner)account).getZipCode();
		} else {
			id 		= ((Renter)account).getId();
			name 	= ((Renter)account).getName();
			state  	= ((Renter)account).getState();
			city 	= ((Renter)account).getCity();
			zipcode = ((Renter)account).getZipCode();
		}
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "UPDATE "  + ACCOUNTTABLE + "SET  name='" + name + "',"+ "state = '" + state + "'," + "city = '" + city + "'," + "zipcode = "+ zipcode + ","+ " WHERE id = " + id ;
            stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

	public static void updateVehicle(Vehicle vehicle)
    {
		int id = vehicle.getVehicleID();
		int ownerID = vehicle.getOwner().getId();
		int insurancePlanID = vehicle.getInsurancePlan().getPolicyNumber();
		String make = vehicle.getMake();	 
		String model = vehicle.getModel();
		int year = vehicle.getYear();
		int rent = vehicle.getRent();
		String lstngStartDate = vehicle.getStartDate();
		String lstngEndDate = vehicle.getEndDate();
		String bkngStartDate = vehicle.getBkngStartDate();
		String bkngEndDate = vehicle.getBkngEndDate();
		boolean isBooked = vehicle.isBooked();
		boolean isListed = vehicle.isListed();
		int zipcode = vehicle.getZipCode();
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "UPDATE "  + VEHICLETABLE + " SET ownerID = " + ownerID + "," + "insurancePlanID = " + insurancePlanID + "," 
        											+ "make = '" + make + "',"+ "model = '"+ model + "'," + "vehicleYear = "+ year + "," + "rent = " + rent + "," 
												+ "lstngStartDate = '" + lstngStartDate + "'," + "lstngEndDate = '" + lstngEndDate + "'," 
        											+ "bkngStartDate = '" + bkngStartDate + "'," + "bkngEndDate = '" + bkngEndDate + "',"
												+ "isBooked = " + isBooked + "," + "isListed = " + isListed + ","
												+ "zipcode = " + zipcode + " WHERE vehicleID = " + id;
        	
        System.out.println("Updated the row");
        	stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

	public static void updateBooking(int bookingID, String startDate, String endDate) {
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "UPDATE "  + BOOKINGTABLE +  " SET startDate = '" + startDate + "'," + "endDate = '" + endDate + "' WHERE bookingID = " + bookingID;
        	System.out.println(query);
        	stmt.execute(query);
        System.out.println("Udated row in database.");
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		
	}

	public static void deleteAccount(Account account)
    {
		int id;
		if (account instanceof Owner) {
			id 		= ((Owner)account).getId();
		} else {
			id 		= ((Renter)account).getId();
		}
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "DELETE FROM "  + ACCOUNTTABLE + " WHERE id = " + id ;
            stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

	public static void deleteVehicle(Vehicle vehicle)
    {
		int id = vehicle.getVehicleID();
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "DELETE FROM "  + VEHICLETABLE + " WHERE id = " + id;
            stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
	
	public static void deleteBooking(Booking booking) {
		int bookingID = booking.getBookingID();
		
        try(Statement stmt = ConnectionFactory.getConnectionToDerby().createStatement())
        {
        	String query = "DELETE FROM "  + BOOKINGTABLE + " WHERE bookingID = " + bookingID;
            stmt.execute(query);
                    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		
	}

	public static Map<String, Object> selectAccount(int id)
    {
		Map<String, Object> row = null;
		try(Statement myStmt = ConnectionFactory.getConnectionToDerby().createStatement();
        	ResultSet results = myStmt.executeQuery("SELECT * FROM " + ACCOUNTTABLE + " WHERE id = " + id);)
        {
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
           
            while(results.next())
            {
            		row = new HashMap<String, Object>();
            			for (int i = 1; i <= numberCols; i++) {
            				row.put(rsmd.getColumnName(i), results.getObject(i));
                }
            }
            
            if (row != null) {
            		System.out.println(row.keySet());
            		System.out.println("\n-----------------------------------------------------------------------");
            		System.out.println(row.values());
            }
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return row;
    }

	public static Map<String, Object> selectVehicle(int vehicleID)
    {
		Map<String, Object> row = null;
		try(Statement myStmt = ConnectionFactory.getConnectionToDerby().createStatement();
        	ResultSet results = myStmt.executeQuery("SELECT * FROM " + VEHICLETABLE + " WHERE vehicleID =" + vehicleID);)
        {
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
           
            while(results.next())
            {
            		row = new HashMap<String, Object>();
            			for (int i = 1; i <= numberCols; i++) {
            				row.put(rsmd.getColumnName(i), results.getObject(i));
                }
            }
            
            if (row != null) {
            		System.out.println(row.keySet());
            		System.out.println("\n---------------------------------------------------------------------------------------------");
            		System.out.println(row.values());
            }
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return row;
    }

	
	public static Map<String, Object> selectBooking(int bookingID)
    {
		Map<String, Object> row = null;
		try(Statement myStmt = ConnectionFactory.getConnectionToDerby().createStatement();
        	ResultSet results = myStmt.executeQuery("SELECT * FROM " + BOOKINGTABLE + " WHERE bookingID =" + bookingID);)
        {
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
           
            while(results.next())
            {
            		row = new HashMap<String, Object>();
            			for (int i = 1; i <= numberCols; i++) {
            				row.put(rsmd.getColumnName(i), results.getObject(i));
                }
            }
            
            if (row != null) {
            		System.out.println(row.keySet());
            		System.out.println("\n------------------------------------------------------------------------");
            		System.out.println(row.values());
            }
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return row;
    }
	
	
	
	public static Map<Integer, LinkedList<Object>> selectBookingByOwnerID(int accountID)
    {
		Map<Integer, LinkedList<Object>> rows = new HashMap<Integer, LinkedList<Object>>();
		try(Statement myStmt = ConnectionFactory.getConnectionToDerby().createStatement();
        	ResultSet results = myStmt.executeQuery("SELECT * FROM " + BOOKINGTABLE + " WHERE ownerID = " + accountID + " ORDER BY startDate DESC");)  
		{
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");
            int rowNo = 1;
            while(results.next())
            {
            		LinkedList<Object> currentRow = new LinkedList<>();
            		int vehicleId = results.getInt(1);
            		int ownerId = results.getInt(2);
            		int renterId = results.getInt(3);
                String startDate = results.getString(4);
                String endDate = results.getString(5);
                String noDays = results.getString(6);
                currentRow.add(vehicleId);
                currentRow.add(ownerId);
                currentRow.add(startDate);
                currentRow.add(endDate);
                currentRow.add(noDays);
                System.out.println(vehicleId + "\t\t" + ownerId + "\t\t" + renterId + "\t\t" + startDate + "\t\t" + endDate + "\t\t" + noDays);
                rows.put(rowNo, currentRow);
                rowNo ++;
            }
            
            System.out.println("Selected rows: " + rows.values().size());
            
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return rows;
    }
	
	
	public static void init() {
    	// We create a table...
	String [] tableDDL = new String[3];
	String [] table = new String[3];
	tableDDL[0] = accountDDL; table[0] = ACCOUNTTABLE;
	tableDDL[1] = vehicleDDL; table[1] = VEHICLETABLE;
	tableDDL[2] = bookingDDL; table[2] = BOOKINGTABLE;
	
	for (int i=0; i< tableDDL.length; i++) {
	    	try(Statement myStmt = ConnectionFactory.getConnectionToDerby().createStatement())
	        {
	    		myStmt.execute("DROP TABLE " + table[i]); 
	    		myStmt.execute(tableDDL[i]);
	    		System.out.println("Created table:  " + table[i]);

	        }
	    	catch (SQLException sqlExcept)
	        {
	    			if(tableAlreadyExists(sqlExcept) ) {
	    				System.out.println("Table alread exists");
	    			} 
	    			else {
	    				sqlExcept.printStackTrace();
	    			}
	        }
		}
    }
	
	private static boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) { // http://db.apache.org/derby/docs/10.8/ref/rrefexcept71493.html
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
	
}
