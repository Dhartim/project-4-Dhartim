package cs601.project4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.Configuration.Configuration;
import cs601.project4.Events.CreateEventBuffer;
import cs601.project4.Events.EventPurchaseBean;
import cs601.project4.Events.EventResponseWithEventId;
import cs601.project4.Tickets.TransferTicketBuffer;
/**
 * RequestResponseManager - It will handler Request and Response URL from one servlet to another
 * @author dhartimadeka
 *
 */
public class RequestResponseManager {

	private HttpURLConnection httpConn;
	private Configuration config = Configuration.getInstance();
	private Gson gson = new Gson();
	
	/**
	 * readRequestBody - it will read request body of any request passed to it
	 * @param request - HttpServletRequest pased from one servlet to another
	 * @return
	 */
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
	/**
	 * checkRequestURL - It will check length of URL 
	 * @param req - HttpServletRequest is request passed to it 
	 * @param resp - HttpServletResponse is response.
	 */
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
	/**
	 * getUrlContents - Used to read content in url
	 * @param theUrl - url is passed which is used for url connection
	 * @return it will return content read from url
	 */
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
	/**
	 * createRequest - will create simple request of type get
	 * @param uri - pass uri 
	 * @param requestid - any eventid or userid
	 * @param resp - to set response code in it
	 */
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
			if(resp.getStatus() != 200)
			{
				System.out.println("here");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			InputStream input = httpConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
	}
	/**
	 * createAddTicketRequest - request to user service to add ticket is made
	 * @param eventPurchase  EventPurchase class which holds event purchase userid in body
	 * @param req - httpServlet request
	 * @param resp - httpServlet response
	 */
	public void createAddTicketRequest(EventPurchaseBean eventPurchase, HttpServletRequest req, HttpServletResponse resp)
	{
		try {
			System.out.println("inside create add ticket ...");
			//url to user service with userid
			URL url = new URL(config.getUserserviceurl() + eventPurchase.getUserid() + "/tickets/add");
			System.out.println(url.toString());
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Accept", "application/json");
			System.out.println(".....\n" +readRequestBody(req).toString());
			httpConn.setDoOutput(true); 
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
	/**
	 * createPurchaseRequest - creates purchase request to event service api from frontend to backend
	 * @param purchasepath - holds path of purchase api
	 * @param eventid - holds event id for which request is
	 * @param eventPurchase - holds event id and tickets to be passed in request
	 * @param resp - Http servlet reponse
	 * @param req - Http Servlet request
	 */
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
			httpConn.setDoOutput(true); 
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
	/**
	 * createTransferRequest  - Create a httpurl connection request to ticket transfer api
	 * @param url - holds path of transfer ticket api
	 * @param ticketBuffer - holds body of transfer ticket api
	 * @param resp - sets httpservlet response status in it
	 * @param req - httpservlet request
	 */
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
			//pass tickets, target user and  event id
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
	
	/**
	 * createEventRequest - creates Event Request to from front end to backend to create event api
	 * @param url - holds create event api path
	 * @param createEvent - it holds create event request body
	 * @param req -  it holds httpservlet request 
	 * @param resp - it has httpservlet response code
	 */
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
			resp.setStatus(httpConn.getResponseCode());
			if(resp.getStatus() != 200)
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
	/**
	 * getEventDetails - it will give details of events by passing request to event service
	 *  from frontend to backend.
	 * @param eventid - holds event id to be passed in event api
	 * @param req - http servlet request
	 * @param resp - http servlet resp
	 * @return
	 */
	// create request to get event details
		public EventResponseWithEventId getEventDetails(int eventid, HttpServletRequest req, HttpServletResponse resp) 
		{
			resp.setStatus(HttpServletResponse.SC_OK);
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
				if (httpConn.getResponseCode() != 200) 
				{
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
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return eWithEventId;
		}

}
