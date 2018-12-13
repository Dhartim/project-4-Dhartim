package cs601.project4.EventService;

import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cs601.project4.Configuration.Configuration;
import cs601.project4.Database.ConnectionDB;

/**
 * Eventserver - it is an event server and maps servlet with respective path
 * @author dhartimadeka
 *
 */
public class EventServer 
{
	public static void main(String args[])
	{
		Configuration config = Configuration.getInstance();
		ConnectionDB conn = ConnectionDB.getInstance();
		Connection connect = conn.connectDatabase();
		Server server = new Server(config.getEventserviceport());
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(CreateEvent.class, "/create");
		handler.addServletWithMapping(GetEventList.class, "/list");
		handler.addServletWithMapping(PurchaseEventTickets.class, "/purchase/*");
		handler.addServletWithMapping(GetEventListWithEventId.class, "/*");
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
