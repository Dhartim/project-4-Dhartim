package cs601.project4.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * 
 * @author dhartimadeka
 * ReadConfigurationFile - will read configuration.json file in 
 *
 */
public class ReadConfigurationFile 
{
	private final static Logger logger =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	private Configuration configuration;
	private Gson gson = new Gson();
	/**
	 * readJsonFiles - read configuration json file and convert it into java objects.
	 * @param filename - name of file to read from
	 * @return
	 */
	public  Configuration readJsonFile(String filename)
	{
		System.out.println(filename);
		if(filename != null)
		{
			try {
				JsonReader reader = new JsonReader(new FileReader(filename));
				configuration = gson.fromJson(reader, Configuration.class);
				logger.log(Level.INFO, "Read json files");
			} catch (FileNotFoundException e) 
			{
				System.exit(0);
			}
		}
		return configuration;
	}
}
