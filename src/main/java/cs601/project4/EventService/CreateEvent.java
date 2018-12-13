package cs601.project4.EventService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;
import cs601.project4.Database.DBManager;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.CreateEventResponse;

/**
 * CreateEvent - logic to create an event
 * @author dhartimadeka
 *
 */
//POST /create
@SuppressWarnings("serial")
public class CreateEvent extends HttpServlet
{
	private RequestResponseManager reqRespMgr = new RequestResponseManager();
	private DBManager dbMngr = new DBManager();
	private Configuration config = Configuration.getInstance();
	private Gson gson = new Gson();
	private PrintWriter writer;
	private HttpURLConnection httpConn;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		//httpConn.setRequestMethod("POST");
		StringBuffer readbodyBuffer;
		// get data from request body
		readbodyBuffer = reqRespMgr.readRequestBody(req);
		// convert json object into java string;
		CreateEventBuffer eventBufferJson = gson.fromJson(readbodyBuffer.toString(), CreateEventBuffer.class);
		//create request to user service
		reqRespMgr.createRequest(config.getUserserviceurl(), eventBufferJson.getUserid(), resp);
		System.out.println(resp.getStatus());
		//check userid exist or not with response code
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if(eventBufferJson.getNumtickets() == 0)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		//user exists insert event into database;
		CreateEventResponse event =  dbMngr.createEvent(eventBufferJson);
		// convert it into json body
		String json = gson.toJson(event);
		// send response back to front api
		writer = resp.getWriter().append(json.toLowerCase());
		//writer.println();
	}

}
