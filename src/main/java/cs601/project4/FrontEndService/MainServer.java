package cs601.project4.FrontEndService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.gson.Gson;

import cs601.project4.Configuration.Configuration;
import cs601.project4.Configuration.ReadConfigurationFile;
/**
 * It is main Server which will connect FrontEndService
 * @author dhartimadeka
 *
 */
public class MainServer 
{
	private Gson gson =  new Gson();
	public static void main(String[] args) 
	{
//		if(args.length != 1)
//		{
//			System.out.println("Please provide Configuration file");
//			System.exit(0);
//		}
//		String configFileName = args[0];
		//make configuration file and connectdb singleton
		Configuration config  = Configuration.getInstance();
		//config = new ReadConfigurationFile().readJsonFile(configFileName);
		//Configuration configuration = new ReadConfigurationFile().readJsonFile(ConfigFileName);
		//passing port into server
		System.out.println(config.getFrontendport());
		Server server = new Server(config.getFrontendport());
		ServletHandler handler = new ServletHandler();
		mapping(handler);
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				server.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void mapping(ServletHandler handler)
	{
		handler.addServletWithMapping(EventHandler.class, "/events");
		handler.addServletWithMapping(CreateEventHandler.class, "/events/create");
		handler.addServletWithMapping(GetEventsHandler.class, "/events/*");
		handler.addServletWithMapping(CreateUserHandler.class, "/users/create");
		handler.addServletWithMapping(GetUserHandler.class, "/users/*");
	}
}
