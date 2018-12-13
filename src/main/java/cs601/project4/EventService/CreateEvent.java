package cs601.project4.EventService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sun.javafx.fxml.builder.URLBuilder;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;
import cs601.project4.Database.DBManager;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.CreateEventResponse;
import cs601.project4.Events.Events;


//POST /create
@SuppressWarnings("serial")
public class CreateEvent extends HttpServlet
{
	RequestResponseManager reqRespMgr = new RequestResponseManager();
	DBManager dbMngr = new DBManager();
	Configuration config = Configuration.getInstance();
	Gson gson = new Gson();
	PrintWriter writer;
	HttpURLConnection httpConn;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		//httpConn.setRequestMethod("POST");
		StringBuffer readbodyBuffer;
		// get data from request body
		readbodyBuffer = reqRespMgr.readRequestBody(req);
		// convert json object into java string;
		CreateEventBuffer eventBufferJson = gson.fromJson(readbodyBuffer.toString(), CreateEventBuffer.class);
		//create request to user service
		//URI uri = new URIBuilder().setScheme("http").setHost("mcvm042.cs.usfca.edu:7072").setPath("/list");
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
		writer.println();
	}

}
