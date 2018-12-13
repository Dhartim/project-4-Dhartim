package cs601.project4.FrontEndService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import cs601.project4.Events.EventPurchaseBean;

//GET /events/{eventid}
//POST /events/{eventid}/purchase/{userid}
@SuppressWarnings("serial")
public class GetEventsHandler extends HttpServlet
{
	HttpURLConnection httpConn;
	InputStream input;
	Configuration config = Configuration.getInstance();
	//GET /events/{eventid}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		if(req.getPathInfo().split("/").length != 2)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		//gives eventid from path given
		String eventid = req.getPathInfo().split("/")[1];
		System.out.println(req.getPathInfo());
		//check for eventid
		if(eventid == null || eventid.isEmpty() || !(eventid.matches("\\d+")) || (req.getPathInfo().split("/").length != 2))
		{
			System.out.println("here");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		URL url = new URL(config.getEventserviceurl() + eventid);
		System.out.println(url.toString());
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("GET");
		httpConn.setRequestProperty("Content-Type", "application/json");
		httpConn.setRequestProperty("Accept", "application/json");
		httpConn.setDoOutput(false); // false indicates this is a GET request
		httpConn.connect();
		resp.setStatus(httpConn.getResponseCode());
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		System.out.println("send request");
		input = httpConn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		StringBuffer responsebuffer = new StringBuffer();
		String line="";
		while ((line = reader.readLine()) != null) 
		{
			responsebuffer.append(line);
			responsebuffer.append("\n");
			// System.out.println(line); 
		}
		reader.close();
		PrintWriter writer = resp.getWriter().append(responsebuffer+"\n");
		writer.println();
	}

	//POST /events/{eventid}/purchase/{userid}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setStatus(HttpServletResponse.SC_OK);
		Gson gson = new Gson();
		RequestResponseManager remngr = new RequestResponseManager();
		String requestUrl[]= req.getPathInfo().split("/");
		//check incorrect url
		if(requestUrl.length != 4 || !(requestUrl[1].matches("\\d+") && requestUrl[2].equals("purchase") && requestUrl[3].matches("\\d+")))
		{
			System.out.println("incorrect request" + req.getPathInfo());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		EventPurchaseBean eventPurchase = new EventPurchaseBean();
		//set event and user id and tickets
		eventPurchase = gson.fromJson(remngr.readRequestBody(req).toString(), EventPurchaseBean.class);
		eventPurchase.setUserid(Integer.parseInt(requestUrl[3]));
		eventPurchase.setEventid(Integer.parseInt(requestUrl[1]));
		//create request and pass it to event service
		remngr.createPurchaseRequest(requestUrl[2]+"/", Integer.parseInt(requestUrl[1]), eventPurchase, resp, req);
		System.out.println("response" + resp.getStatus());
		if(resp.getStatus() != HttpServletResponse.SC_OK)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
	}
}
