package cs601.project4.FrontEndService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.RequestResponseManager;
import cs601.project4.Configuration.Configuration;
import cs601.project4.EventService.CreateEvent;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.Events;

//POST /events/create 
@SuppressWarnings("serial")
public class CreateEventHandler extends HttpServlet
{
	HttpURLConnection httpConn;
	URL url;
	InputStream input;
	OutputStream output;
	Gson gson = new Gson();
	Configuration config =  Configuration.getInstance();
	RequestResponseManager remngr = new RequestResponseManager();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		System.out.println("in post..");
		//System.out.println(req.getPathInfo()+"create");
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
//		url = new URL(config.getEventserviceurl()+"create");
//		httpConn = (HttpURLConnection) url.openConnection();
//		httpConn.setRequestMethod("POST");
//		httpConn.setRequestProperty("Content-type", "application/json");
//		//read json data
//		//Events event = 
//		//make it json object
//		//pass it further with request
//		httpConn.setDoOutput(true);
//		OutputStream output = httpConn.getOutputStream();
//		CreateEvent event = gson.fromJson(remngr.readRequestBody(req).toString(), CreateEvent.class);
//		output.write(gson.toJson(event).getBytes("UTF-8"));
//		//output.write(gson.toJson(remngr.readRequestBody(req).toString(), CreateEvent.class).getBytes("UTF-8"));
//		httpConn.connect();
//		resp.setStatus(httpConn.getResponseCode());
//		//output.write((remngr.readRequestBody(req).toString()));
//		// false indicates this is a GET request
////		System.out.println(req.getAttribute("userid"));
////		System.out.println(req.getParameter("userid"));
////		int userid = Integer.parseInt(req.getParameter("userid"));//check this user id exist in user table or not
////		String eventName = req.getParameter("eventname");
////		int numtickets = Integer.parseInt(req.getParameter("numtickets"));
////		System.out.println(userid + eventName + numtickets);
////		////to pass body
////		OutputStream os = httpConn.getOutputStream();
////		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
////	//	osw.write("Just Some Text");
////		osw.flush();
////		osw.close();
////		os.close();
//		/////
//       // httpConn.connect();
//        if(resp.getStatus() == 200)
//        {
//        	input = httpConn.getInputStream();
//        }
//        else
//        {
//        	input = httpConn.getErrorStream();
//        }
//	}
}
