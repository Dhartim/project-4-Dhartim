package cs601.project4.FrontEndService;

import java.io.IOException;

import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;

import cs601.project4.Events.CreateEventBuffer;

//POST /events/create 
/**
 * CreateEventHandler - handles events/create api 
 * @author dhartimadeka
 *
 */
@SuppressWarnings("serial")
public class CreateEventHandler extends HttpServlet
{
	
	private URL url;
	private Gson gson = new Gson();
	private Configuration config =  Configuration.getInstance();
	private RequestResponseManager remngr = new RequestResponseManager();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		//httpConn.setRequestMethod("POST");
		url = new URL(config.getEventserviceurl()+ "create");
		System.out.println(url.toString());
		//read request body
		CreateEventBuffer createEvent  = gson.fromJson(remngr.readRequestBody(req).toString(), CreateEventBuffer.class);
		//create request
		remngr.createEventRequest(url, createEvent, req, resp);
		//get response here
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
}
