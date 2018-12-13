package cs601.project4.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Configuration 
{
	private static Gson gson = new Gson();
	private static Configuration instance;
	private final static Logger logger =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	private static final String filename = "configuration.json"; //final bcoz it will same throughout program
	private String frontendurl;
	private int frontendport;
	private String userserviceurl;
	private int userserviceport;
	private String eventserviceurl;
	private int eventserviceport;
	private String username;
	private String password;
	private String database;
	private String driver;
	private String databaseurl;
	
	private Configuration()
	{}
	
	public static Configuration getInstance()
	{
		if(instance ==  null)
		{
			instance = new Configuration();
			System.out.println(filename);
			if(filename != null)
			{
				try {
					JsonReader reader = new JsonReader(new FileReader(filename));
					instance = gson.fromJson(reader, Configuration.class);
					logger.log(Level.INFO, "Read json files");
				} catch (FileNotFoundException e) 
				{
					System.exit(0);
				}
			}
		}
        return instance;
    }
	public int getFrontendport() {
		return frontendport;
	}

	public void setFrontendport(int frontendport) {
		this.frontendport = frontendport;
	}

	public int getUserserviceport() {
		return userserviceport;
	}

	public void setUserserviceport(int userserviceport) {
		this.userserviceport = userserviceport;
	}

	public int getEventserviceport() {
		return eventserviceport;
	}

	public void setEventserviceport(int eventserviceport) {
		this.eventserviceport = eventserviceport;
	}

	public String getFrontendurl() {
		return frontendurl;
	}
	public void setFrontendurl(String frontendurl) {
		this.frontendurl = frontendurl;
	}
	public String getUserserviceurl() {
		return userserviceurl;
	}
	public void setUserserviceurl(String userserviceurl) {
		this.userserviceurl = userserviceurl;
	}
	public String getEventserviceurl() {
		return eventserviceurl;
	}
	public void setEventserviceurl(String eventserviceurl) {
		this.eventserviceurl = eventserviceurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDatabaseurl() {
		return databaseurl;
	}
	public void setDatabaseurl(String databaseurl) {
		this.databaseurl = databaseurl;
	}
	
}
