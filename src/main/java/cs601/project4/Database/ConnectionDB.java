package cs601.project4.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cs601.project4.Configuration.Configuration;
import cs601.project4.Configuration.ReadConfigurationFile;
public class ConnectionDB 
{
	private static ConnectionDB instance;// = new ConnectionDB();
	private String username = "user03";
	private String password = "user03";
	private String db = "user03";
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
		String urlString = config.getDatabaseurl() + config.getDatabase();//"jdbc:mysql://127.0.0.1:3306/"+db;
		System.out.println(urlString);
		//Must set time zone explicitly in newer versions of mySQL.
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		try {
			System.out.println("going to connect");
			con = DriverManager.getConnection(urlString+timeZoneSettings,config.getUsername(),config.getPassword());//username,password);
			System.out.println("connected...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
