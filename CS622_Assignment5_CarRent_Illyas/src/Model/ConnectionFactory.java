package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private static Connection conn;
	
	public static Connection getConnectionToDerby(){
		if(conn == null){
			try
	        {
	            //Get a connection
	            conn = DriverManager.getConnection("jdbc:derby:derbyDB;create=true;user=cs622;password=cs622"); 
	        }
	        catch (Exception except)
	        {
	            except.printStackTrace();
	        }
		}
		
		return conn;
	}
	
	public static void shutdownDerby()
    {
        try
        {          
            if (conn != null)
            {
            	conn.close();
                DriverManager.getConnection("jdbc:derby:derbyDB;shutdown=true;user=me;password=mine");                
            }           
        }
        catch (SQLException sqlExcept)
        {
        		sqlExcept.printStackTrace();
        }

    }
}

