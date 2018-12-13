package cs601.project4.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cs601.project4.Configuration.Configuration;

/**
 * ConnectionDB - it will establish a connection with database 
 * @author dhartimadeka
 *
 */
public class ConnectionDB 
{
	private static ConnectionDB instance;
	private Connection con;
	private Configuration config = Configuration.getInstance();
    //private constructor to avoid client applications to use constructor
    private ConnectionDB() 
    {
    	//connectDatabase();
		// TODO Auto-generated constructor stub
	}
    public static ConnectionDB getInstance()
    {
    	if(instance ==  null)
    	{
    		instance = new ConnectionDB();
    	}
        return instance;
    }
    /**
     * connectDatabase - it will establish connection to database
     * @return
     */
	public Connection connectDatabase()
	{
		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			System.exit(1);
		}
		String urlString = config.getDatabaseurl() + config.getDatabase();
		System.out.println(urlString);
		//Must set time zone explicitly in newer versions of mySQL.
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		try {
			System.out.println("going to connect");
			con = DriverManager.getConnection(urlString+timeZoneSettings,config.getUsername(),config.getPassword());
			System.out.println("connected...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
