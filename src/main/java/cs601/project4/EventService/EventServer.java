package cs601.project4.EventService;

import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cs601.project4.Configuration.Configuration;
import cs601.project4.Configuration.ReadConfigurationFile;
import cs601.project4.Database.ConnectionDB;


public class EventServer 
{
	public static void main(String args[])
	{
//		if(args.length != 1)
//		{
//			System.out.println("Please provide configuration file");
//			System.exit(0);
//		}
//		String  configFileName = args[0];
		Configuration config = Configuration.getInstance();
		//config = new ReadConfigurationFile().readJsonFile(configFileName);
		//connect to database
		ConnectionDB conn = ConnectionDB.getInstance();
		Connection connect = conn.connectDatabase();
		//Connection connection = new ConnectionDB().connectDatabase();
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
