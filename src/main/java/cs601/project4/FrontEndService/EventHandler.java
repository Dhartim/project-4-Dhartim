package cs601.project4.FrontEndService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.project4.Configuration.Configuration;
/**
 * EventHandler - it handles get events list
 * @author dhartimadeka
 *
 */
//GET /events
@SuppressWarnings("serial")
public class EventHandler extends HttpServlet 
{
	private HttpURLConnection httpConn;
	private InputStream input;
	private Configuration config = Configuration.getInstance();
	private URL url;
	//GET /events
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	{	
		resp.setStatus(HttpServletResponse.SC_OK);
		System.out.println(req.getPathInfo());
        try {
        	url =  new URL(config.getEventserviceurl()+ "list");
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Accept", "application/json");
	        httpConn.setDoOutput(true); // false indicates this is a GET request
	        httpConn.connect();
	        //get response from backend and write it back on main server
	        input = httpConn.getInputStream();
	        resp.setStatus(httpConn.getResponseCode());
	        if(resp.getStatus() != HttpServletResponse.SC_OK )
	        {
	        	resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        	return;
	        }
	       BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
	        StringBuffer responsebuffer = new StringBuffer();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	        	responsebuffer.append(line);
	        	responsebuffer.append("\n"); 
	        }
	        reader.close();
	        PrintWriter writer = resp.getWriter().append(responsebuffer);
	        writer.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
