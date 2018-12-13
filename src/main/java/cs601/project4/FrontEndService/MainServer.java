package cs601.project4.FrontEndService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.gson.Gson;

import cs601.project4.Configuration.Configuration;
/**
 * It is main Server which will connect front end service api
 * @author dhartimadeka
 *
 */
public class MainServer 
{
	public static void main(String[] args) 
	{
		Configuration config  = Configuration.getInstance();
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
