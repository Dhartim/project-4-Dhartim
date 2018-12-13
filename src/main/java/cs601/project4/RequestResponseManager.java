package cs601.project4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.Configuration.Configuration;
import cs601.project4.EventService.CreateEvent;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.EventList;
import cs601.project4.Events.EventPurchaseBean;
import cs601.project4.Events.EventResponseWithEventId;
import cs601.project4.Tickets.TransferTicketBuffer;

public class RequestResponseManager {

	HttpURLConnection httpConn;
	Configuration config = Configuration.getInstance();
	Gson gson = new Gson();
	// ready body part of request
	public StringBuffer readRequestBody(HttpServletRequest request) 
	{
		String line;
		StringBuffer buffer = new StringBuffer();
		// get body data from request
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				buffer.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Cant read data from request body");
		}
		System.out.println(buffer.toString());
		return buffer;
	}

	public void checkRequestURL(HttpServletRequest req, HttpServletResponse resp) {
		if (req.getRequestURI().split("/").length != 2) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else if (req.getRequestURI().split("/").length == 2) {
			if ((req.getRequestURI().split("/")[1] == null) || !(req.getRequestURI().split("/")[1].matches("\\d+"))) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
	
	public String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try {
			// create a url object
			URL url = new URL(theUrl);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
	public void createRequest(String uri, int requestid, HttpServletResponse resp)
	{
		URL url;
		try {
			resp.setStatus(HttpServletResponse.SC_OK);
			url = new URL(uri+requestid);
			System.out.println(url.toString());
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setDoOutput(false); // false indicates this is a GET request
			httpConn.connect();
			resp.setStatus(httpConn.getResponseCode());
			//int resp = httpConn.getResponseCode();
			//System.out.println("request send..." + resp);
			if(resp.getStatus() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			InputStream input = httpConn.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return resp;
	}
	
	public void createAddTicketRequest(String uri, EventPurchaseBean eventPurchase, HttpServletRequest req, HttpServletResponse resp)
	{
		try {
			System.out.println("inside create add ticket ...");
			URL url = new URL(config.getUserserviceurl() + eventPurchase.getUserid() + "/tickets/add");
			System.out.println(url.toString());
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			System.out.println(".....\n" +readRequestBody(req).toString());
			httpConn.setDoOutput(true); // false indicates this is a GET request
			///need to send request body with post request
			OutputStream out = httpConn.getOutputStream(); //getBytes("UTF-8")
			//pass eventid and tickets here...
			System.out.println(gson.toJson(eventPurchase));
			out.write(gson.toJson(eventPurchase).getBytes("UTF-8"));
			httpConn.connect();
			resp.setStatus(httpConn.getResponseCode());
			if(resp.getStatus() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			InputStream input = httpConn.getInputStream();
			out.flush();
			out.close();
			input.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//create request from frontend to backend
	public void createPurchaseRequest(String purchasepath, int eventid, EventPurchaseBean eventPurchase, HttpServletResponse resp, HttpServletRequest req)
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		try {
			URL url = new URL(config.getEventserviceurl()+purchasepath + eventid);
			System.out.println(url.toString());
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setDoOutput(true); // false indicates this is a GET request
			///need to send request body with post request
			OutputStream out = httpConn.getOutputStream(); //getBytes("UTF-8")
			//pass eventid and tickets here...
			
			out.write(gson.toJson(eventPurchase).getBytes("UTF-8"));
			httpConn.connect();
			resp.setStatus(httpConn.getResponseCode());
			if(resp.getStatus() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			InputStream input = httpConn.getInputStream();
			out.flush();
			out.close();
			input.close();
		} catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//transfer request
	public void createTransferRequest(URL url, TransferTicketBuffer ticketBuffer, HttpServletResponse resp, HttpServletRequest req)
	{
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setDoOutput(true);
			OutputStream out = httpConn.getOutputStream(); //getBytes("UTF-8")
			//pass eventid and tickets here...
			out.write(gson.toJson(ticketBuffer).getBytes("UTF-8"));
			httpConn.connect();
			resp.setStatus(httpConn.getResponseCode());
			if(resp.getStatus() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			InputStream input = httpConn.getInputStream();
			out.flush();
			out.close();
			input.close();
		} catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//create event 
	public void createEventRequest(URL url, CreateEventBuffer createEvent, HttpServletRequest req, HttpServletResponse resp)
	{
		StringBuffer buffer = new StringBuffer();
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setDoOutput(true);
			OutputStream out = httpConn.getOutputStream(); //getBytes("UTF-8")
			//pass request body in request
			out.write(gson.toJson(createEvent).getBytes("UTF-8"));
			httpConn.connect();
			if(httpConn.getResponseCode() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line;
			while((line = in.readLine()) != null) {
				buffer.append(line);
			}
			resp.setStatus(httpConn.getResponseCode());
			resp.getWriter().println(buffer.toString());
			out.flush();
			out.close();
			
			//resp.getWriter().append(buffer.toString());
		} catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// create request to get event details
		public EventResponseWithEventId getEventDetails(int eventid, HttpServletRequest req, HttpServletResponse resp) 
		{
			StringBuffer buffer = new StringBuffer();
			EventResponseWithEventId eWithEventId = new EventResponseWithEventId();
			URL url;
			try {
				url = new URL(config.getEventserviceurl() + eventid);
				httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setRequestMethod("GET");
				httpConn.setRequestProperty("Content-Type", "application/json");
				httpConn.setRequestProperty("Accept", "application/json");
				httpConn.setDoOutput(true);
				httpConn.connect();
				if (httpConn.getResponseCode() != 200) {
					System.out.println("here");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return null;
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					buffer.append(line);
					System.out.println(buffer.toString());
				}
				resp.setStatus(httpConn.getResponseCode());
				eWithEventId = gson.fromJson(buffer.toString(), EventResponseWithEventId.class);
				//String eventDetails = gson.toJson(eWithEventId);
				//resp.getWriter().println(eventDetails);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return eWithEventId;
		}
	
	//create request to get event details
//	public void getEventDetails(int eventid, HttpServletResponse resp)
//	{
//		StringBuffer buffer = new StringBuffer();
//		URL url;
//		try {
//			url = new URL(config.getEventserviceurl()+eventid);
//			httpConn = (HttpURLConnection) url.openConnection();
//			httpConn.setRequestMethod("GET");
//			httpConn.setRequestProperty("Content-Type", "application/json");
//			httpConn.setRequestProperty("Accept", "application/json");
//			httpConn.setDoOutput(true); 
//			httpConn.connect();
//			if(httpConn.getResponseCode() != 200)
//			{
//				System.out.println("here");
//				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				return;
//			}
//			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
//			String line;
//			while((line = in.readLine()) != null) {
//				buffer.append(line);
//				System.out.println(buffer.toString());
//			}
//			resp.setStatus(httpConn.getResponseCode());
//			System.out.println("?????"+buffer.toString());
//			String eventDetails = gson.toJson(buffer.toString(), EventResponseWithEventId.class);
//			resp.getWriter().println(eventDetails);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
