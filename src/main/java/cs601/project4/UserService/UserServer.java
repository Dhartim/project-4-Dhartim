package cs601.project4.UserService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cs601.project4.Configuration.Configuration;
/**
 * UserServer = handles user service api 
 * @author dhartimadeka
 *
 */
public class UserServer 
{
	public static void main(String args[]) 
	{
		Configuration config = Configuration.getInstance();
		
		Server server = new Server(config.getUserserviceport());
		System.out.println(config.getUserserviceport());
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(CreateUser.class, "/create");
		handler.addServletWithMapping(GetUserListAndTickets.class, "/*");
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			try {
				server.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
