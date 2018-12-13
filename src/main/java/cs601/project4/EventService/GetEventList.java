package cs601.project4.EventService;

import java.io.IOException;
import java.io.PrintWriter;
import cs601.project4.Database.DBManager;

import cs601.project4.Events.EventList;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
//GET /list
/**
 * GetEventList - it gives list of events from evnet service
 * @author dhartimadeka
 *
 */
@SuppressWarnings("serial")
public class GetEventList extends HttpServlet
{
	//GET /list
	private Gson gson = new Gson();
	DBManager dbMngr = new DBManager();
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
		List<EventList> result = new ArrayList<EventList>();
//		// fetch list of events from database
		result = dbMngr.getEventList();
		if(result == null )
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		//converting it into json object and pass back to frontend
		//System.out.println("Event Details");
		String json = gson.toJson(result);
		//send response back to front api
		PrintWriter writer = resp.getWriter().append(json);
		//writer.println();
		//System.out.println(json);

	}
}
